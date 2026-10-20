package com.Showroom.Sales.SalesManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Sales.SalesManagement.Entities.Sales;
public final class SalesMapper {
    private SalesMapper() {}
    public static Sales toEntity(SalesRequest request) {
        Sales entity = new Sales();
        entity.setCustomerId(request.customerId());
        entity.setCarId(request.carId());
        entity.setAmount(request.amount());
        entity.setSaleDate(request.saleDate());
        return entity;
    }
    public static SalesResponse toResponse(Sales entity) {
        return new SalesResponse(entity.getId(), entity.getCustomerId(), entity.getCarId(), entity.getAmount(), entity.getSaleDate());
    }
}