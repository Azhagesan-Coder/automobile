package com.Showroom.Service.ServiceMangement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Service.ServiceMangement.Enitity.ServiceRecord;
public final class ServiceRecordMapper {
    private ServiceRecordMapper() {}
    public static ServiceRecord toEntity(ServiceRecordRequest request) {
        ServiceRecord entity = new ServiceRecord();
        entity.setVehicleId(request.vehicleId());
        entity.setServiceType(request.serviceType());
        entity.setAppointmentDate(request.appointmentDate());
        entity.setCost(request.cost());
        entity.setNotes(request.notes());
        return entity;
    }
    public static ServiceRecordResponse toResponse(ServiceRecord entity) {
        return new ServiceRecordResponse(entity.getId(), entity.getVehicleId(), entity.getServiceType(), entity.getAppointmentDate(), entity.getCost(), entity.getNotes(), entity.getStatus());
    }
}