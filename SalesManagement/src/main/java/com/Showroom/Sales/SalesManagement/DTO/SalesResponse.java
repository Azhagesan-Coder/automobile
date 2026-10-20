package com.Showroom.Sales.SalesManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Sales.SalesManagement.Entities.Sales;
public record SalesResponse(Long id, String customerId, String carId, Double amount, LocalDate saleDate) {}
