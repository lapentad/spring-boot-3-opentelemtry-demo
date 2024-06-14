package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.spring.webflux.v5_3.SpringWebfluxTelemetry;

@Configuration
public class WebClientConfig {
    private final SpringWebfluxTelemetry webfluxTelemetry;

    public WebClientConfig(OpenTelemetry openTelemetry) {
        this.webfluxTelemetry = SpringWebfluxTelemetry.builder(openTelemetry).build();
    }

    @Bean(name = "telemetryWebClientBuilder")
    public WebClient.Builder webClientBuilder() {
        WebClient webClient = WebClient.create();
        return webClient.mutate().filters(webfluxTelemetry::addClientTracingFilter);
    }
}
