'use strict';

const opentelemetry = require('@opentelemetry/api');
const {registerInstrumentations} = require('@opentelemetry/instrumentation');
const {NodeTracerProvider} = require('@opentelemetry/node');
const {SimpleSpanProcessor, ConsoleSpanExporter} = require('@opentelemetry/tracing');
const grpc = require('grpc');
const {CollectorTraceExporter} = require('@opentelemetry/exporter-collector-grpc');

const {ExpressInstrumentation} = require('@opentelemetry/instrumentation-express');
const {HttpInstrumentation} = require('@opentelemetry/instrumentation-http');

module.exports = (parameter) => {
    const provider = new NodeTracerProvider();
    registerInstrumentations({
        tracerProvider: provider,
        instrumentations: [
            HttpInstrumentation,
            ExpressInstrumentation,
        ],
    });
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