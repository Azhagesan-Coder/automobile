package com.Showroom.Service.ServiceMangement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Service.ServiceMangement.Enitity.ServiceRecord;
public record ServiceRecordRequest(@NotNull @Positive Long vehicleId, @NotBlank String serviceType, @NotNull LocalDate appointmentDate, @PositiveOrZero Double cost, String notes) {}
