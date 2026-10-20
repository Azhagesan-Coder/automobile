package com.Showroom.Inventory.InventoryManagement.Controller;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Showroom.Inventory.InventoryManagement.DTO.*;
import com.Showroom.Inventory.InventoryManagement.Service.ServiceImpl;
@RestController
@RequestMapping("/api/invent")
public class InventController {
    private final ServiceImpl service;
    public InventController(ServiceImpl service) { this.service = service; }
    @PostMapping("/add")
    public ResponseEntity<VehiclesResponse> create(@Valid @RequestBody VehiclesRequest request) {
        return ResponseEntity.status(201).body(VehiclesMapper.toResponse(service.addVehicle(VehiclesMapper.toEntity(request))));
    }
    @GetMapping
    public List<VehiclesResponse> list() {
        return service.getAllVehicles().stream().map(VehiclesMapper::toResponse).toList();
    }
    @GetMapping("/{id}")
    public VehiclesResponse get(@PathVariable Long id) { return VehiclesMapper.toResponse(service.getVehicleById(id)); }
    @PutMapping("/{id}")
    public VehiclesResponse update(@PathVariable Long id, @Valid @RequestBody VehiclesRequest request) {
        return VehiclesMapper.toResponse(service.updateVehicle(id, VehiclesMapper.toEntity(request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/category/{category}")
    public List<VehiclesResponse> category(@PathVariable com.Showroom.Inventory.InventoryManagement.Entity.Category category) {
        return service.getVehiclesByCategory(category).stream().map(VehiclesMapper::toResponse).toList();
    }
}