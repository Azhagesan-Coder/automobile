package com.Showroom.Sales.SalesManagement.Resilience;
import io.github.resilience4j.circuitbreaker.*;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class DownstreamCircuitsTest {
    @Test void opensRejectsCallsAndRecoversAfterSuccessfulProbes() {
        DownstreamCircuits circuits = new DownstreamCircuits(2, 2, 50, 30, 2);
        AtomicInteger attempts = new AtomicInteger();
        for (int i = 0; i < 2; i++) assertThrows(ResourceAccessException.class,
            () -> circuits.call("inventory", () -> { attempts.incrementAndGet(); throw new ResourceAccessException("timeout"); }));
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
            () -> circuits.call("inventory", () -> { attempts.incrementAndGet(); return "unexpected"; }));
        assertEquals(503, error.getStatusCode().value());
        assertEquals(2, attempts.get());
        assertEquals("ok", circuits.call("customer", () -> "ok"));
        CircuitBreakerRegistry registry = (CircuitBreakerRegistry) ReflectionTestUtils.getField(circuits, "registry");
        CircuitBreaker breaker = registry.circuitBreaker("inventory");
        breaker.transitionToHalfOpenState();
        assertEquals("ok", circuits.call("inventory", () -> "ok"));
        assertEquals("ok", circuits.call("inventory", () -> "ok"));
        assertEquals(CircuitBreaker.State.CLOSED, breaker.getState());
    }
    @Test void missingRecordsDoNotOpenCircuitButRateLimitingDoes() {
        DownstreamCircuits circuits = new DownstreamCircuits(2, 2, 50, 30, 2);
        for (int i = 0; i < 5; i++) assertThrows(HttpClientErrorException.class,
            () -> circuits.call("inventory", () -> { throw new HttpClientErrorException(HttpStatus.NOT_FOUND); }));
        assertEquals("ok", circuits.call("inventory", () -> "ok"));
        for (int i = 0; i < 2; i++) assertThrows(HttpClientErrorException.class,
            () -> circuits.call("sms", () -> { throw new HttpClientErrorException(HttpStatus.TOO_MANY_REQUESTS); }));
        assertThrows(ResponseStatusException.class, () -> circuits.call("sms", () -> "unexpected"));
    }
}
