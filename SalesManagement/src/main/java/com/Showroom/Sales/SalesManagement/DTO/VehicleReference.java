package com.Showroom.Sales.SalesManagement.DTO;
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public record VehicleReference(Long id, Integer stock) {}
