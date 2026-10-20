package com.Showroom.ReportAndAnalyze.ReportManagement.DTO;
import java.time.LocalDate;
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public record SaleSource(Long id, String customerId, String carId, Double amount, LocalDate saleDate) {}
