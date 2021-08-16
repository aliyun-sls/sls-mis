'use strict';

const opentelemetry = require('@opentelemetry/api');
const {NodeTracerProvider} = require('@opentelemetry/node');
const {PinoInstrumentation} = require('@opentelemetry/instrumentation-pino');
const {registerInstrumentations} = require('@opentelemetry/instrumentation');
const {SimpleSpanProcessor, ConsoleSpanExporter} = require('@opentelemetry/tracing');
const grpc = require('@grpc/grpc-js');
const {CollectorTraceExporter} = require('@opentelemetry/exporter-collector-grpc');

const {ExpressInstrumentation} = require('@opentelemetry/instrumentation-express');
const {HttpInstrumentation} = require('@opentelemetry/instrumentation-http');

module.exports = (parameter) => {
    const provider = new NodeTracerProvider();
    provider.register();

    registerInstrumentations({
        instrumentations: [
            new HttpInstrumentation(),
            new ExpressInstrumentation({
                ignoreLayersType: ['middleware']
            }),
            new PinoInstrumentation({
                logHook: (span, record) => {
                    record['resource.service.name'] = provider.resource.attributes['service.name'];
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
    const collectorOptions = {
        serviceName: parameter.service_name,
        url: parameter.endpoint,
        credentials: grpc.credentials.createSsl(),
        metadata: meta
    };

    const exporter = new CollectorTraceExporter(collectorOptions);
    provider.addSpanProcessor(new SimpleSpanProcessor(exporter));
    provider.register();
    return opentelemetry.trace.getTracer(parameter.service_name);
};