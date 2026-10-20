package com.Showroom.Sales.SalesManagement.Resilience;

import java.time.Duration;
import java.util.function.Supplier;
import io.github.resilience4j.circuitbreaker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

/** One independent circuit per downstream dependency; never retries writes. */
@Component
public class DownstreamCircuits {
    private final CircuitBreakerRegistry registry;

    public DownstreamCircuits() { this(20, 10, 50, 30, 3); }

    @Autowired
    public DownstreamCircuits(
            @Value("${downstream.circuit.window-size:20}") int window,
            @Value("${downstream.circuit.minimum-calls:10}") int minimum,
            @Value("${downstream.circuit.failure-rate:50}") float failureRate,
            @Value("${downstream.circuit.open-seconds:30}") long openSeconds,
            @Value("${downstream.circuit.half-open-calls:3}") int probes) {
        registry = CircuitBreakerRegistry.of(CircuitBreakerConfig.custom()
            .slidingWindowSize(window).minimumNumberOfCalls(minimum)
            .failureRateThreshold(failureRate)
            .waitDurationInOpenState(Duration.ofSeconds(openSeconds))
            .permittedNumberOfCallsInHalfOpenState(probes)
            .ignoreException(DownstreamCircuits::isClientError).build());
    }

    private static boolean isClientError(Throwable error) {
        if (error instanceof org.springframework.web.client.HttpClientErrorException http)
            return http.getStatusCode().value() != 429;
        
        if (error instanceof com.twilio.exception.ApiException api && api.getStatusCode() != null)
            return api.getStatusCode() >= 400 && api.getStatusCode() < 500 && api.getStatusCode() != 429;
        return false;
    }

    public <T> T call(String dependency, Supplier<T> request) {
        try { return registry.circuitBreaker(dependency).executeSupplier(request); }
        catch (CallNotPermittedException error) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                dependency + " is temporarily unavailable", error);
        }
    }
}
