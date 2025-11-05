package com.example.iot.processor.service;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class IoTSimulator {

    private static final int NUM_DEVICES = 100_000;
    private static final String INGEST_URL = "http://localhost:8081/api/v1/devices/ingest";

    public static void main(String[] args) {

        WebClient webClient = WebClient.builder()
                .baseUrl(INGEST_URL)
                .build();

        // Generate Flux of device IDs
        Flux.range(1, NUM_DEVICES)
                .flatMap(deviceId -> {
                    double temperature = ThreadLocalRandom.current().nextDouble(20, 40);
                    int humidity = ThreadLocalRandom.current().nextInt(30, 90);
                    long timestamp = System.currentTimeMillis();
                    String json = String.format(
                            "{\"deviceId\":\"sensor-%d\",\"temperature\":%.2f,\"humidity\":%d,\"timestamp\":%d}",
                            deviceId, temperature, humidity, timestamp
                    );

                    return webClient.post()
                            .header("Content-Type", "application/json")
                            .bodyValue(json)
                            .retrieve()
                            .bodyToMono(String.class)
                            .doOnNext(response -> System.out.println("Device " + deviceId + " sent: " + response))
                            .doOnError(error -> System.err.println("Error sending device " + deviceId + ": " + error.getMessage()));
                }, 100) // concurrency level, 100 requests at a time
                .blockLast(); // Wait until all requests are finished
    }
}
