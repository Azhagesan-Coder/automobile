package com.Customer.CustomerManagement.Controller;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.Customer.CustomerManagement.DTO.*;
import com.Customer.CustomerManagement.Service.CustomerService;
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service) { this.service = service; }
    @PostMapping("")
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(201).body(CustomerMapper.toResponse(service.createCustomer(CustomerMapper.toEntity(request))));
    }
    @GetMapping
    public List<CustomerResponse> list() {
        return service.getAllCustomers().stream().map(CustomerMapper::toResponse).toList();
    }
    @GetMapping("/{id}")
    public CustomerResponse get(@PathVariable Long id) { return CustomerMapper.toResponse(service.getCustomerById(id)); }
    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return CustomerMapper.toResponse(service.updateCustomer(id, CustomerMapper.toEntity(request)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/inventory/{carId}")
    public com.Customer.CustomerManagement.Entities.InventoryResponse inventory(@PathVariable Long carId) {
        return service.fetchCarDetails(carId);
    }
}