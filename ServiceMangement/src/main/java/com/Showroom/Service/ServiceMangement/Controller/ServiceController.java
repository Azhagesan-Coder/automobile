package com.Showroom.Service.ServiceMangement.Controller;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Showroom.Service.ServiceMangement.DTO.*;
import com.Showroom.Service.ServiceMangement.Service.ServiceImpl;
@RestController
@RequestMapping("/api/service")
public class ServiceController {
    private final ServiceImpl service;
    public ServiceController(ServiceImpl service) { this.service = service; }
    @PostMapping("")
    public ResponseEntity<ServiceRecordResponse> create(@Valid @RequestBody ServiceRecordRequest request) {
        return ResponseEntity.status(201).body(ServiceRecordMapper.toResponse(service.scheduleAppointment(ServiceRecordMapper.toEntity(request))));
    }
    @GetMapping
    public List<ServiceRecordResponse> list() {
        return service.getAllServices().stream().map(ServiceRecordMapper::toResponse).toList();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteService(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/vehicle/{vehicleId}")
    public List<ServiceRecordResponse> history(@PathVariable Long vehicleId) {
        return service.getAllService(vehicleId).stream().map(ServiceRecordMapper::toResponse).toList();
    }
    @PatchMapping("/{id}/status")
    public ServiceRecordResponse status(@PathVariable Long id, @RequestParam String status) {
        if (!java.util.Set.of("Scheduled", "In Progress", "Completed", "Cancelled").contains(status)) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid service status");
        }
        return ServiceRecordMapper.toResponse(service.updateService(id, status));
    }
}