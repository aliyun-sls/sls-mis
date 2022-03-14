'use strict';

const opentelemetry = require('@opentelemetry/api');
const {registerInstrumentations} = require('@opentelemetry/instrumentation');
const {NodeTracerProvider} = require('@opentelemetry/sdk-trace-node');
const {Resource} = require('@opentelemetry/resources');
const {SemanticResourceAttributes} = require('@opentelemetry/semantic-conventions');
const {PinoInstrumentation} = require('@opentelemetry/instrumentation-pino');
const {SimpleSpanProcessor, ConsoleSpanExporter} = require('@opentelemetry/tracing');
const grpc = require('@grpc/grpc-js');
const {CollectorTraceExporter} = require('@opentelemetry/exporter-collector-grpc');

const {ExpressInstrumentation} = require('@opentelemetry/instrumentation-express');
const {HttpInstrumentation} = require('@opentelemetry/instrumentation-http');
const {hostname} = require("os");
const {setSpanContext} = require("@opentelemetry/api/build/src/trace/context-utils");
const {trace, context} = require("@opentelemetry/api");

module.exports = (parameter) => {
    const provider = new NodeTracerProvider({
        resource: new Resource({
            [SemanticResourceAttributes.SERVICE_NAME]: parameter.service_name,
            [SemanticResourceAttributes.DEPLOYMENT_ENVIRONMENT]: "production",
            [SemanticResourceAttributes.SERVICE_VERSION]: parameter.version,
            [SemanticResourceAttributes.HOST_NAME]: hostname()
        })
    });
    registerInstrumentations({
        instrumentations: [
            new HttpInstrumentation(),
            new ExpressInstrumentation({
                ignoreLayersType: ['middleware']
            }),
        ],
        tracerProvider: provider,
    })
    var meta = new grpc.Metadata();
    meta.add('x-sls-otel-project', parameter.project);
    meta.add('x-sls-otel-instance-id', parameter.instance);
    meta.add('x-sls-otel-ak-id', parameter.access_key_id);
    meta.add('x-sls-otel-ak-secret', parameter.access_secret);
    const collectorOptions = {
        url: parameter.endpoint,
        credentials: grpc.credentials.createSsl(),
        metadata: meta
    };

    const exporter = new CollectorTraceExporter(collectorOptions);
    provider.addSpanProcessor(new SimpleSpanProcessor(exporter));
    provider.register();

    let t1 = provider.getTracer("traceA", "1.0.0")
    let clientSpan = t1.startSpan("testSpanA", {});
    let newContext = trace.setSpan(context.active(), clientSpan);

    // 默认以opentelemetry.context.active()作为parentContext
    let contextCarrier = opentelemetry.context.with(newContext, () => {
        let carrier = {}
        opentelemetry.propagation.inject(newContext, carrier);
        return carrier;
    })

    let newAnotherContext = opentelemetry.context.with(opentelemetry.context.active(), () => {
        return opentelemetry.propagation.extract(context.active(), contextCarrier);
    });

    let serverSpan = opentelemetry.context.with(newAnotherContext, ()=> {
        return t1.startSpan("serverSpan", {}, context.active());
    })

    serverSpan.end();
    clientSpan.end();
    return opentelemetry.trace.getTracer(parameter.service_name);
};
