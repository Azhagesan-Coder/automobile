package com.Showroom.Sales.SalesManagement.Controller;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Showroom.Sales.SalesManagement.DTO.*;
import com.Showroom.Sales.SalesManagement.Service.SalesService;
@RestController
@RequestMapping("/api/sales")
public class SalesController {
    private final SalesService service;
    public SalesController(SalesService service) { this.service = service; }
    @PostMapping("")
    public ResponseEntity<SalesResponse> create(@Valid @RequestBody SalesRequest request) {
        return ResponseEntity.status(201).body(SalesMapper.toResponse(service.createSale(SalesMapper.toEntity(request))));
    }
    @GetMapping
    public List<SalesResponse> list() {
        return service.getAllSales().stream().map(SalesMapper::toResponse).toList();
    }
}