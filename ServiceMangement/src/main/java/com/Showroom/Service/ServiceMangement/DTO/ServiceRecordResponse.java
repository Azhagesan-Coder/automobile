package com.Showroom.Service.ServiceMangement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Service.ServiceMangement.Enitity.ServiceRecord;
public record ServiceRecordResponse(Long id, Long vehicleId, String serviceType, LocalDate appointmentDate, Double cost, String notes, String status) {}
