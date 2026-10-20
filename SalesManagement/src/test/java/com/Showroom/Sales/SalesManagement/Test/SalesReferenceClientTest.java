package com.Showroom.Sales.SalesManagement.Test;
import com.Showroom.Sales.SalesManagement.Service.SalesReferenceClient;
import com.Showroom.Sales.SalesManagement.Entities.Sales;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
class SalesReferenceClientTest {
    @Test void existingCustomerAndAvailableVehiclePassValidation() {
        RestTemplate client = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(client);
        server.expect(requestTo("http://customer-management/api/customers/1")).andRespond(withSuccess("{\"id\":1,\"name\":\"Alice\"}", MediaType.APPLICATION_JSON));
        server.expect(requestTo("http://inventory-management/api/invent/7")).andRespond(withSuccess("{\"id\":7,\"stock\":3,\"model\":\"City\"}", MediaType.APPLICATION_JSON));
        new SalesReferenceClient(client).validateSale(new Sales(null, "1", "7", 123.45, LocalDate.now()));
        server.verify();
    }
    @Test void outOfStockVehicleIsRejected() {
        RestTemplate client = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(client);
        server.expect(requestTo("http://customer-management/api/customers/1")).andRespond(withSuccess("{\"id\":1}", MediaType.APPLICATION_JSON));
        server.expect(requestTo("http://inventory-management/api/invent/7")).andRespond(withSuccess("{\"id\":7,\"stock\":0}", MediaType.APPLICATION_JSON));
        assertEquals(409, assertThrows(ResponseStatusException.class, () -> new SalesReferenceClient(client)
            .validateSale(new Sales(null, "1", "7", 123.45, LocalDate.now()))).getStatusCode().value());
        server.verify();
    }
    @Test void missingCustomerIsRejected() {
        RestTemplate client = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(client);
        server.expect(requestTo("http://customer-management/api/customers/1")).andRespond(withResourceNotFound());
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> new SalesReferenceClient(client)
            .validateSale(new Sales(null, "1", "7", 123.45, LocalDate.now()))).getStatusCode().value());
        server.verify();
    }
    @Test void failingCustomerCircuitStopsRemoteCallsAndStillRejectsSale() {
        RestTemplate client = org.mockito.Mockito.mock(RestTemplate.class);
        var references = new SalesReferenceClient(client);
        org.springframework.test.util.ReflectionTestUtils.setField(references, "circuits",
            new com.Showroom.Sales.SalesManagement.Resilience.DownstreamCircuits(2, 2, 50, 30, 2));
        org.mockito.Mockito.when(client.getForObject("http://customer-management/api/customers/{id}",
            com.Showroom.Sales.SalesManagement.DTO.CustomerReference.class, "1"))
            .thenThrow(new org.springframework.web.client.ResourceAccessException("timeout"));
        var sale = new Sales(null, "1", "7", 123.45, LocalDate.now());
        for (int attempt = 0; attempt < 2; attempt++)
            assertEquals(502, assertThrows(ResponseStatusException.class, () -> references.validateSale(sale)).getStatusCode().value());
        assertEquals(503, assertThrows(ResponseStatusException.class, () -> references.validateSale(sale)).getStatusCode().value());
        org.mockito.Mockito.verify(client, org.mockito.Mockito.times(2)).getForObject(
            "http://customer-management/api/customers/{id}", com.Showroom.Sales.SalesManagement.DTO.CustomerReference.class, "1");
        org.mockito.Mockito.verifyNoMoreInteractions(client);
    }
}
