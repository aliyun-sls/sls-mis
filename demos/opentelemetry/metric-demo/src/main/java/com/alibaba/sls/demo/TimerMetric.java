package com.alibaba.sls.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import io.opentelemetry.api.metrics.ObservableDoubleMeasurement;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import io.opentelemetry.api.metrics.ObservableLongUpDownCounter;
import io.opentelemetry.api.trace.TraceId;
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
import java.util.function.Consumer;

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
        ObservableDoubleGauge doubleGauge = meter.gaugeBuilder("test-metric-2").setUnit("s").buildWithCallback(new Consumer<ObservableDoubleMeasurement>() {
            @Override public void accept(ObservableDoubleMeasurement measurement) {
                measurement.record(ThreadLocalRandom.current().nextDouble(100.00),
                    Attributes.of(AttributeKey.stringKey("traceId"), TraceId.fromLongs(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong())));
            }
        });

        ObservableLongUpDownCounter counter = meter.upDownCounterBuilder("test-metric-3").setUnit("a").buildWithCallback(new Consumer<ObservableLongMeasurement>() {
            @Override public void accept(ObservableLongMeasurement measurement) {
                measurement.record(ThreadLocalRandom.current().nextLong(2),
                    Attributes.of(AttributeKey.stringKey("traceId"), TraceId.fromLongs(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong())));
            }
        });

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        executor.scheduleWithFixedDelay(() -> {
            histogram.record(ThreadLocalRandom.current().nextDouble(10.00));
            System.out.println("[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] Add Metric success");
        }, 0, 1, TimeUnit.MILLISECONDS);
        Thread.currentThread().join();
    }
}
