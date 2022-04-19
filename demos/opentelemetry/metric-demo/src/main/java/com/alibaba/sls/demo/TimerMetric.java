package com.alibaba.sls.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.common.Clock;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TimerMetric {

    public static String ENDPOINT = "";
    public static String AK = "";
    public static String SK = "";
    public static String PROJECT = "";
    public static String INSTANCE = "";

    static {
        ENDPOINT = System.getenv("ENDPOINT");
        AK = System.getenv("ACCESS_KEY");
        SK = System.getenv("ACCESS_SECRITY");
        PROJECT = System.getenv("PROJECT");
        INSTANCE = System.getenv("INSTANCE");

        OpenTelemetrySdk.builder()
            .setMeterProvider(SdkMeterProvider.builder()
                .registerMetricReader(PeriodicMetricReader.create(OtlpGrpcMetricExporter.builder()
                    .addHeader("x-sls-otel-project", PROJECT)
                    .addHeader("x-sls-otel-instance-id", INSTANCE)
                    .addHeader("x-sls-otel-ak-id", AK)
                    .addHeader("x-sls-otel-ak-secret", SK)
                    .setEndpoint(ENDPOINT)
                    .build()))
                .setResource(Resource.create(Attributes.of(AttributeKey.stringKey("test-metric"), "test")))
                .build()).buildAndRegisterGlobal();
    }

    public static void main(String[] args) throws InterruptedException {
        Meter meter = GlobalOpenTelemetry.getMeterProvider().meterBuilder("test-metric").build();
        DoubleHistogram histogram = meter.histogramBuilder("test-metric-1").setUnit("c").setDescription("").build();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        executor.scheduleWithFixedDelay(() -> {
            histogram.record(ThreadLocalRandom.current().nextDouble(1000.00));
            System.out.println("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] Add Metric success");
        }, 0, 500, TimeUnit.MILLISECONDS);
        Thread.currentThread().join();
    }
}
