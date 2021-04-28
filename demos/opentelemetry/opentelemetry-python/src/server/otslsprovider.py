import socket
from opentelemetry import trace
from opentelemetry.exporter.otlp.trace_exporter import OTLPSpanExporter
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import BatchExportSpanProcessor

class OpenTelemetrySLSProvider(object):

    def __init__(self, access_key_id=None, access_secret=None, project=None, logstore=None,
                 endpoint=None,  service_name="opentelemetry-python", service_version="1.0.0"):
        print("Running parameteres: ")
        print("- accessKeyID: ", access_key_id)
        print("- accessSecret: ", access_secret)
        print("- project: ", project)
        print("- logstore: ", logstore)
        print("- endpoint: ", endpoint)
        print("- service name: ", service_name)
        print("- service version: ", service_version)

        self.endpoint = endpoint
        self.project = project
        self.access_key_id = access_key_id
        self.access_secret = access_secret
        self.logstore = logstore
        self.resource = Resource(attributes={
            "host.name": socket.gethostname(),
            "service.name": service_name,
            "service.version": service_version,
            "sls.otel.project": self.project,
            "sls.otel.akid": self.access_key_id,
            "sls.otel.aksecret": self.access_secret,
            "sls.otel.instanceid": self.logstore,
        })

    def initTracer(self):
        otlp_exporter = OTLPSpanExporter(endpoint=self.endpoint)
        trace_provider = TracerProvider(resource=self.resource)
        trace_provider.add_span_processor(BatchExportSpanProcessor(otlp_exporter))
        trace.set_tracer_provider(trace_provider)
