package com.alibaba.demo.otel.metric;


import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.MetricReaderFactory;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;

import java.time.Duration;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

public class Application {


    public static void main(String[] args) throws InterruptedException {
        Meter meter = initMeter();
        LongCounter counter = meter.counterBuilder("example_counter").build();
        LongHistogram recorder = meter.histogramBuilder("super_timer").ofLongs().setUnit("ms").build();
        for (int i = 0; i < 100; i++) {
            long startTime = System.currentTimeMillis();

            try {
                counter.add(1);
                Thread.sleep(1000);
            } finally {
                recorder.record(System.currentTimeMillis() - startTime);
            }
        }

        Thread.sleep(1000);
        System.out.println("Bye");
    }

    private static Meter initMeter() {
        String accessKeyId = System.getProperty("ACCESS_KEY_ID", "");
        String accessKeySecret = System.getProperty("ACCESS_KEY_SECRET", "");
        String endpoint = System.getProperty("ENDPOINT", "");

        Resource RESOURCE =
                Resource.create(Attributes.of(stringKey("resource_key"), "resource_value"));

        MetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
                .setEndpoint(endpoint)
                .addHeader("x-sls-otel-project", "qs-otel-demos")
                .addHeader("x-sls-otel-instance-id", "sls-test-123")
                .addHeader("x-sls-otel-ak-id", accessKeyId)
                .addHeader("x-sls-otel-ak-secret", accessKeySecret)
                .build();
        MetricReaderFactory readerFactory = PeriodicMetricReader.builder(metricExporter)
                .setInterval(Duration.ofSeconds(1)).newMetricReaderFactory();
        SdkMeterProvider sdkMeterProvider =
                SdkMeterProvider.builder()
                        .registerMetricReader(readerFactory)
                        .setResource(RESOURCE)
                        .build();
        return sdkMeterProvider.get(Application.class.getName());
    }


}
