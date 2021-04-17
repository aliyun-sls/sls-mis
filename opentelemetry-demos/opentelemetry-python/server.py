import flask
from opentelemetry import trace
import requests
import socket

from opentelemetry import trace
from opentelemetry.sdk.resources import Resource
from opentelemetry.sdk.trace import TracerProvider
from opentelemetry.sdk.trace.export import (
    ConsoleSpanExporter,
    SimpleSpanProcessor,
)

class OpentelemetrySLSProvider(object):
    def __init__(self, accessKeyID=None, accesSecret=None, project=None, logStore=None, endpoint="", service="opentelemetry-python", version="1.0.0") -> None:
        self.access_key_id = accessKeyID
        self.access_secret = accesSecret
        self.project = project
        self.logstore = logStore
        self.endpoint = endpoint
        self.service = service
        self.version = version
        self.resource = Resource(attributes={
            "host.name": socket.gethostname(),
            "service.name": service,
            "service.version": version,
            "sls.otel.project": self.project,
            "sls.otel.akid": self.access_key_id,
            "sls.otel.aksecre": self.access_secret,
            "sls.otel.instanceid": self.logstore, 
        })
        
    def initTrace(self):
        trace.set_tracer_provider(TracerProvider())
        trace.get_tracer_provider().add_span_processor()


app = flask.Flask(__name__)

@app.route("/hello-world")
def hello():
    tracer = trace.get_tracer(__name__)
    with tracer.start_as_current_span("request_server"):
        requests.get("http://www.baidu.com")
    return "Hello World"


app.run(debug=True, port=8080)
