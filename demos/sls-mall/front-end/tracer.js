const opentelemetry = require('@opentelemetry/api');
const { SimpleSpanProcessor } = require('@opentelemetry/tracing');
const { NodeTracerProvider } = require('@opentelemetry/node');
const { CollectorTraceExporter } =  require('@opentelemetry/exporter-collector-grpc');

module.exports = (serviceName, url) => {
  const collectorOptions = {
    serviceName: serviceName,
    url: url // url is optional and can be omitted - default is localhost:4317
  };
  
  console.log("serviceName : " + serviceName + ", url : " + url)

  const exporter = new CollectorTraceExporter(collectorOptions);
  const provider = new NodeTracerProvider({
    plugins: {
      http: {
        enabled: true,
        path: '@opentelemetry/plugin-http',
        requestHook: (span, request) => {
          //console.log(span)
          //span.setAttribute("custom request hook attribute", "request");
        },
      },
      express: {
        enabled: true,
        path: '@opentelemetry/plugin-express',
      },
    }
  });
  provider.addSpanProcessor(new SimpleSpanProcessor(exporter));
  provider.register();
  return opentelemetry.trace.getTracer("front-end");
};