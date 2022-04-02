package com.alibaba.sls.demo.grpc.server;

import com.alibaba.sls.demo.grpc.helloworld.HelloReply;
import com.alibaba.sls.demo.grpc.helloworld.HelloRequest;
import com.alibaba.sls.demo.grpc.helloworld.StreamingGreeterGrpc;
import io.grpc.stub.StreamObserver;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.extension.annotations.WithSpan;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import sun.tools.tree.ShiftLeftExpression;

public class HelloWorldImpl extends StreamingGreeterGrpc.StreamingGreeterImplBase {

    public static String AK = "";
    public static String SK = "";
    public static String ENDPOINT = "";

    private static String PROJECT = "";

    private static String INSTANCE = "";

    static {
        ENDPOINT = System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT");
        AK = System.getenv("ACCESS_KEY_ID");
        SK = System.getenv("ACCESS_SECRET");
        PROJECT = System.getenv("PROJECT");
        INSTANCE = System.getenv("INSTANCE");
    }

    @Override public void sayHelloStreaming(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        doOther(responseObserver);
        doCreateSpan();
    }

    @WithSpan public void doOther(StreamObserver<HelloReply> responseObserver) {
        responseObserver.onNext(HelloReply.newBuilder().setMessage("H").build());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("e").build());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("l").build());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("l").build());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("o").build());
        responseObserver.onCompleted();
    }

    public void doCreateSpan() {
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder().addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().setEndpoint(ENDPOINT).addHeader("x-sls-otel-project", PROJECT).addHeader("x-sls-otel-instance-id", INSTANCE).addHeader("x-sls-otel-ak-id", AK).addHeader("x-sls-otel-ak-secret", SK).build()).build()).setResource(Resource.create(Attributes.of(AttributeKey.stringKey("service.name"), "grpc-demo"))).build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider).setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance())).build();

        Context context = Context.current();
        Span span = openTelemetry.getTracer("test").spanBuilder("test-span").setParent(context).startSpan();

        span.end();
    }
}
