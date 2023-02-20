package com.alibaba.sls.demo;

import static com.alibaba.sls.demo.utils.EnvUtils.getEnv;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 */
public class ManualAPIDemo {

	public static void main(String[] args) throws InterruptedException {
		String endpoint = getEnv("ENDPOINT");
		String project = getEnv("PROJECT");
		String instance = getEnv("INSTANCE_ID");
		String accessKeyId = getEnv("ACCESS_KEY_ID");
		String accessSecret = getEnv("ACCESS_SECRET");

		if (endpoint == null || project == null || instance == null || accessKeyId == null || accessSecret == null) {
			System.out.println(
				"Please set the environment variables: ENDPOINT, PROJECT, INSTANCE_ID, ACCESS_KEY_ID, ACCESS_SECRET");
			return;
		}

		Resource resource = Resource.getDefault()
			.merge(
				Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, getEnv("SERVICE_NAME", "test-service"))))
			.merge(Resource.create(
				Attributes.of(ResourceAttributes.SERVICE_NAMESPACE, getEnv("SERVICE_NAMESPACE", "namespace"))))
			.merge(
				Resource.create(Attributes.of(ResourceAttributes.SERVICE_VERSION, getEnv("SERVICE_VERSION", "1.0.0"))))
			.merge(Resource.create(Attributes.of(ResourceAttributes.HOST_NAME, getEnv("HOST_NAME", "host-name"))));

		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
			.addSpanProcessor(
				// 配置.setEndpoint参数时，必须添加https://，例如https://test-project.cn-hangzhou.log.aliyuncs.com:10010。
				BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder().setEndpoint(endpoint)
						.addHeader("x-sls-otel-project", project)
						.addHeader("x-sls-otel-instance-id", instance)
						.addHeader("x-sls-otel-ak-id", accessKeyId)
						.addHeader("x-sls-otel-ak-secret", accessSecret).build())
					.build())
			.setResource(resource).build();

		OpenTelemetry openTelemetry = OpenTelemetrySdk.builder().setTracerProvider(sdkTracerProvider)
			.buildAndRegisterGlobal();

		Tracer tracer = openTelemetry.getTracer("instrumentation-library-name", "1.0.0");
		Span parentSpan = tracer.spanBuilder("parent").startSpan();

		try {
			System.out.printf("Create Trace: %s%n", parentSpan.getSpanContext().getTraceId());
			Span childSpan = tracer.spanBuilder("child").setParent(Context.current().with(parentSpan)).startSpan();
			childSpan.setAttribute("test", "vllelel");
			childSpan.end();
		} finally {
			parentSpan.end();
		}

		Thread.sleep(TimeUnit.SECONDS.toMillis(60));
	}
}
