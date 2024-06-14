package com.example.demo;

import java.time.Duration;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/web")
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    private final Random random = new Random();

    private final WebClient webClient;

    @Value("${spring.application.name}")
    private String applicationName;

    public WebController(@Qualifier("telemetryWebClientBuilder") WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping("/path1")
    public Mono<ResponseEntity<String>> path1() {
        logger.info("Incoming request at {} for request /path1", applicationName);
        return doNothingButSleepForSomeTime()
                .then(webClient.get()
                        .uri("http://localhost:8091/web/path2")
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(response -> doNothingButSleepForSomeTime().thenReturn(response))
                .map(response -> ResponseEntity.ok("response from /path1 + " + response));
    }

    @GetMapping("/path2")
    public Mono<ResponseEntity<String>> path2() {
        logger.info("Incoming request at {} at /path2", applicationName);
        return doNothingButSleepForSomeTime()
                .then(Mono.just(ResponseEntity.ok("response from /path2")));
    }

    @GetMapping("/notrace")
    public Mono<ResponseEntity<String>> path0() {
        logger.info("Incoming request at {} for request /path0", applicationName);
        return doNothingButSleepForSomeTime()
                .then(webClient.get()
                        .uri("http://demo-jaeger-notrace:8092/notrace/path2")
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(response -> doNothingButSleepForSomeTime().thenReturn(response))
                .map(response -> ResponseEntity.ok("response from /notrace + " + response));
    }

    @GetMapping("/mvc")
    public Mono<ResponseEntity<String>> path00() {
        logger.info("Incoming request at {} for request /path0", applicationName);
        return doNothingButSleepForSomeTime()
                .then(webClient.get()
                        .uri("http://demo-jaeger-mvc:8090/hello/path2")
                        .retrieve()
                        .bodyToMono(String.class))
                .flatMap(response -> doNothingButSleepForSomeTime().thenReturn(response))
                .map(response -> ResponseEntity.ok("response from /mvc + " + response));
    }

    public Mono<Void> doNothingButSleepForSomeTime() {
        int sleepTime = random.nextInt(2, 5);
        logger.info("sleeping for {} seconds", sleepTime);
        return Mono.delay(Duration.ofSeconds(sleepTime)).then();
    }
}
