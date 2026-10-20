package com.Customer.CustomerManagement.Test;
import com.Customer.CustomerManagement.Client.InventoryClient;
import com.Customer.CustomerManagement.Resilience.DownstreamCircuits;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.*;

class InventoryCircuitTest {
    @Test void dependencyFailureOpensCircuitAndDoesNotCallInventoryAgain() {
        AtomicInteger attempts = new AtomicInteger();
        InventoryClient client = new InventoryClient();
        ReflectionTestUtils.setField(client, "inventoryServiceName", "inventory-management");
        ReflectionTestUtils.setField(client, "circuits", new DownstreamCircuits(2, 2, 50, 30, 2));
        ReflectionTestUtils.setField(client, "webClientBuilder", WebClient.builder().exchangeFunction(request -> {
            attempts.incrementAndGet();
            return Mono.error(new IllegalStateException("inventory unavailable"));
        }));
        for (int i = 0; i < 2; i++) assertEquals(502,
            assertThrows(ResponseStatusException.class, () -> client.getInventoryDetails(7L)).getStatusCode().value());
        assertEquals(503, assertThrows(ResponseStatusException.class, () -> client.getInventoryDetails(7L)).getStatusCode().value());
        assertEquals(2, attempts.get());
    }
}
