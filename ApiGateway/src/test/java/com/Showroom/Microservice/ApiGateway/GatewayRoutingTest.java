package com.Showroom.Microservice.ApiGateway;

import com.Showroom.Microservice.ApiGateway.JwtUtil.JwtUtil;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "spring.cloud.config.enabled=false", "eureka.client.enabled=false",
    "management.tracing.enabled=false", "management.zipkin.tracing.export.enabled=false",
    "spring.datasource.url=jdbc:h2:mem:routing;MODE=MySQL;NON_KEYWORDS=USER",
    "spring.datasource.username=sa", "spring.datasource.password=",
    "spring.datasource.driver-class-name=org.h2.Driver", "spring.jpa.hibernate.ddl-auto=create-drop",
    "jwt.secret=test-only-secret-with-at-least-32-bytes"
})
@AutoConfigureMockMvc
class GatewayRoutingTest {
    private static final HttpServer backend;
    static {
        try {
            backend = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
            backend.createContext("/", exchange -> {
                byte[] body = exchange.getRequestURI().toString().getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(200, body.length);
                try (var output = exchange.getResponseBody()) { output.write(body); }
            });
            backend.start();
        } catch (java.io.IOException exception) { throw new ExceptionInInitializerError(exception); }
    }

    @DynamicPropertySource
    static void discovery(DynamicPropertyRegistry registry) {
        for (String service : new String[] {"customer-management", "inventory-management",
                "sales-management", "service-management", "fileupload-and-download", "report-management"}) {
            registry.add("spring.cloud.discovery.client.simple.instances." + service + "[0].uri",
                () -> "http://127.0.0.1:" + backend.getAddress().getPort());
        }
    }

    @Autowired io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry circuits;
    @org.springframework.boot.test.web.server.LocalServerPort int port;
    @Autowired org.springframework.boot.test.web.client.TestRestTemplate http;
    @Autowired MockMvc mvc;
    @Autowired JwtUtil jwt;

    @AfterAll static void stopBackend() { backend.stop(0); }

    @Test void proxiesAllConfiguredPathsWithoutRedirectingOrRewriting() throws Exception {
        String token = jwt.generateToken("routing-user");
        for (String path : new String[] {"/api/customers", "/api/invent/1", "/api/sales",
                "/api/service/vehicle/1", "/api/files/download/1", "/api/report/sales/monthly", "/api/sms/send"}) {
            mvc.perform(get(path + "?probe=true").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(header().doesNotExist("Location"))
                .andExpect(content().string(path + "?probe=true"));
        }
    }

    @Test void openCircuitReturnsUnavailableAndOtherRoutesStillWork() throws Exception {
        var circuit = circuits.circuitBreaker("customer-management");
        circuit.transitionToOpenState();
        try {
            String token = jwt.generateToken("routing-user");
            var headers = new org.springframework.http.HttpHeaders();
            headers.setBearerAuth(token);
            var response = http.exchange("http://localhost:" + port + "/api/customers",
                org.springframework.http.HttpMethod.GET, new org.springframework.http.HttpEntity<>(headers), String.class);
            org.junit.jupiter.api.Assertions.assertEquals(503, response.getStatusCode().value());
            org.junit.jupiter.api.Assertions.assertTrue(response.getBody().contains("Downstream service is temporarily unavailable"));
            mvc.perform(get("/api/invent/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        } finally { circuit.reset(); }
    }

    @Test void missingTokenReturnsUnauthorizedWithoutRedirect() throws Exception {
        mvc.perform(get("/api/customers")).andExpect(status().isUnauthorized())
            .andExpect(header().doesNotExist("Location"));
    }
}
