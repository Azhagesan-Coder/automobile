package com.Customer.CustomerManagement.Test;
import com.Customer.CustomerManagement.Entities.InventoryResponse;
import com.Customer.CustomerManagement.DTO.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class InventoryResponseTest {
    @Test void inventoryIdAndStockAreMappedToCustomerContract() throws Exception {
        InventoryResponse response = new ObjectMapper().readValue("{\"id\":7,\"model\":\"City\",\"stock\":4,\"price\":1200000}", InventoryResponse.class);
        assertEquals(7L, response.getCarId()); assertEquals(4, response.getQuantity());
        assertEquals("City", response.getModel());
    }
    @Test void requestDoesNotAssignDatabaseIdentity() {
        var entity = CustomerMapper.toEntity(new CustomerRequest("Alice", "alice@example.com", null, null, null, null));
        assertNull(entity.getId());
        entity.setId(9L);
        assertEquals(9L, CustomerMapper.toResponse(entity).id());
        assertEquals("alice@example.com", CustomerMapper.toResponse(entity).email());
    }
}
