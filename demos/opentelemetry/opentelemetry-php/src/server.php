<?php

$autoloader = require __DIR__ . '/../vendor/autoload.php';
require_once __DIR__ . '/SLSExporter.php';

use OpenTelemetry\Sdk\Trace\Clock;
use OpenTelemetry\Sdk\Trace\SpanProcessor\SimpleSpanProcessor;
use OpenTelemetry\Sdk\Trace\TracerProvider;

$endpoint = getenv('ZIPKIN_ENDPOINT');
$project = getenv('PROJECT');
$instance = getenv('INSTANCE');
$accessKeyId = getenv('ACCESS_KEY_ID');
$accessSecret = getenv('ACCESS_SECRET');

$serviceName = getenv('SERVICE_NAME');
$serviceVersion = getenv('SERVICE_VERSION');

if ($serviceName == '') {
    $serviceName = 'Opentelemetry-PHP';
}

if ($serviceVersion == '') {
    $serviceVersion = 'v1.0.0';
}

$exporter = new Exporter($serviceName, $endpoint, $project, $instance, $accessKeyId, $accessSecret);
$tracer = (new TracerProvider())->addSpanProcessor(new SimpleSpanProcessor($exporter))->getTracer($serviceName);

$timestamp = Clock::get()->timestamp();
$span = $tracer->startAndActivateSpan($_SERVER['REQUEST_URI']);
sleep(rand(0, 3));
echo sprintf(
    PHP_EOL . 'Exporting Trace: %s, Parent: %s, Span: %s',
    $span->getContext()->getTraceId(),
    $span->getParent() ? $span->getParent()->getSpanId() : 'None',
    $span->getContext()->getSpanId()
);

$tracer->endActiveSpan();
