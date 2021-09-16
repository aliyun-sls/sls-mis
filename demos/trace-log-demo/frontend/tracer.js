'use strict';

const opentelemetry = require('@opentelemetry/api');
const {registerInstrumentations} = require('@opentelemetry/instrumentation');
const {NodeTracerProvider} = require('@opentelemetry/sdk-trace-node');
const {Resource} = require('@opentelemetry/resources');
const {SemanticResourceAttributes} = require('@opentelemetry/semantic-conventions');
const {PinoInstrumentation} = require('@opentelemetry/instrumentation-pino');
const {SimpleSpanProcessor} = require('@opentelemetry/tracing');
const grpc = require('@grpc/grpc-js');
const {CollectorTraceExporter} = require('@opentelemetry/exporter-collector-grpc');

const {ExpressInstrumentation} = require('@opentelemetry/instrumentation-express');
const {HttpInstrumentation} = require('@opentelemetry/instrumentation-http');
const {hostname} = require("os");

module.exports = (parameter) => {
    const provider = new NodeTracerProvider({
        resource: new Resource({
            [SemanticResourceAttributes.SERVICE_NAME]: parameter.service_name,
            [SemanticResourceAttributes.DEPLOYMENT_ENVIRONMENT]: "production",
            [SemanticResourceAttributes.SERVICE_VERSION]: parameter.version,
            [SemanticResourceAttributes.HOST_NAME]: hostname()
        })
    });
    provider.register();
    registerInstrumentations({
        instrumentations: [
            new HttpInstrumentation(),
            new ExpressInstrumentation({
                ignoreLayersType: ['middleware']
            }),
            new PinoInstrumentation({
                logHook: (span, record) => {
                    record['resource.service.name'] = "frontend";
                },
            }),
        ],
        tracerProvider: provider,
    })
    var meta = new grpc.Metadata();
    meta.add('x-sls-otel-project', parameter.project);
    meta.add('x-sls-otel-instance-id', parameter.instance);
    meta.add('x-sls-otel-ak-id', parameter.access_key_id);
    meta.add('x-sls-otel-ak-secret', parameter.access_secret);
    meta.add('x-sls-otel-ak-secret', parameter.access_secret);
    const collectorOptions = {
        url: parameter.endpoint,
        credentials: grpc.credentials.createSsl(),
        metadata: meta
    };

    const exporter = new CollectorTraceExporter(collectorOptions);
    provider.addSpanProcessor(new SimpleSpanProcessor(exporter));
    provider.register();
    return opentelemetry.trace.getTracer(parameter.service_name);
};