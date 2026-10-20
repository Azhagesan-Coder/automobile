package com.Customer.CustomerManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Customer.CustomerManagement.Entities.Customer;
public final class CustomerMapper {
    private CustomerMapper() {}
    public static Customer toEntity(CustomerRequest request) {
        Customer entity = new Customer();
        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setMobile(request.mobile());
        entity.setAddress(request.address());
        entity.setPreferences(request.preferences());
        entity.setPurchaseHistory(request.purchaseHistory());
        return entity;
    }
    public static CustomerResponse toResponse(Customer entity) {
        return new CustomerResponse(entity.getId(), entity.getName(), entity.getEmail(), entity.getMobile(), entity.getAddress(), entity.getPreferences(), entity.getPurchaseHistory());
    }
}