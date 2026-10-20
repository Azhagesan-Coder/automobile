package com.Showroom.Sales.SalesManagement.Test;
import com.Showroom.Sales.SalesManagement.DTO.*;
import com.Showroom.Sales.SalesManagement.Entities.Sales;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SalesDtoTest {
    @Test void acceptsJsonAndMapsDecimalAmountWithoutClientIdentity() throws Exception {
        SalesRequest request = new ObjectMapper().findAndRegisterModules().readValue(
            "{\"customerId\":\"1\",\"carId\":\"7\",\"amount\":123.45,\"saleDate\":\"2026-10-08\"}", SalesRequest.class);
        Sales entity = SalesMapper.toEntity(request);
        assertNull(entity.getId()); assertEquals(123.45, entity.getAmount());
        assertEquals(request.saleDate(), entity.getSaleDate());
        entity.setId(5L);
        assertEquals(5L, SalesMapper.toResponse(entity).id());
    }
}
