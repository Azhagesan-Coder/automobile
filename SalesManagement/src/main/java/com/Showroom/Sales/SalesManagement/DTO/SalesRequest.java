package com.Showroom.Sales.SalesManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Sales.SalesManagement.Entities.Sales;
public record SalesRequest(@NotBlank @Pattern(regexp = "[1-9][0-9]*") String customerId, @NotBlank @Pattern(regexp = "[1-9][0-9]*") String carId, @NotNull @Positive Double amount, @NotNull LocalDate saleDate) {}
