<?php

require_once __DIR__ . '/../vendor/autoload.php';

use GuzzleHttp\HandlerStack;
use GuzzleHttp\Middleware;
use GuzzleHttp\Psr7\Request;
use Http\Adapter\Guzzle6\Client;
use OpenTelemetry\Sdk\Trace;
use Psr\Http\Client\ClientExceptionInterface;
use Psr\Http\Client\ClientInterface;
use Psr\Http\Client\NetworkExceptionInterface;
use Psr\Http\Client\RequestExceptionInterface;
use OpenTelemetry\Contrib\Zipkin\SpanConverter;

class Exporter implements Trace\Exporter
{
    private $endpointUrl;
    private $spanConverter;
    private $running = true;
    private $client;

    public function __construct(string $serviceName, string $endpointUrl, string $project, string $logstore, string $accessKeyId, string $accessSecret)
    {
        $this->endpointUrl = $endpointUrl;
        $this->client = $client ?? $this->createClient($project, $logstore, $accessKeyId, $accessSecret);
        $this->spanConverter = new SpanConverter($serviceName);
    }

    public function export(iterable $spans): int
    {
        if (!$this->running) {
            return Exporter::FAILED_NOT_RETRYABLE;
        }

        if (empty($spans)) {
            return Trace\Exporter::SUCCESS;
        }

        $convertedSpans = [];
        foreach ($spans as $span) {
            array_push($convertedSpans, $this->spanConverter->convert($span));
        }

        try {
            $json = json_encode($convertedSpans);
            $request = new Request('POST', $this->endpointUrl, array(), $json);
            $response = $this->client->sendRequest($request);
        } catch (RequestExceptionInterface $e) {
            return Trace\Exporter::FAILED_NOT_RETRYABLE;
        } catch (NetworkExceptionInterface | ClientExceptionInterface $e) {
            return Trace\Exporter::FAILED_RETRYABLE;
        }

        if ($response->getStatusCode() >= 400 && $response->getStatusCode() < 500) {
            return Trace\Exporter::FAILED_NOT_RETRYABLE;
        }

        if ($response->getStatusCode() >= 500 && $response->getStatusCode() < 600) {
            return Trace\Exporter::FAILED_RETRYABLE;
        }

        return Trace\Exporter::SUCCESS;
    }

    public function shutdown(): void
    {
        $this->running = false;
    }

    protected function createClient(string $project, string $logstore, string $accessKeyId, string $accessSecret): ClientInterface
    {
        $container = [];
        $history = Middleware::history($container);
        $stack = HandlerStack::create();
        $stack->push($history);

        return Client::createWithConfig([
            'handler' => $stack,
            'timeout' => 30,
            'headers' => [
                'x-sls-otel-project' => $project,
                'x-sls-otel-instance-id' => $logstore,
                'x-sls-otel-ak-id' => $accessKeyId,
                'x-sls-otel-ak-secret' => $accessSecret
            ]
        ]);
    }
}
