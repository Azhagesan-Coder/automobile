package com.Customer.CustomerManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Customer.CustomerManagement.Entities.Customer;
public record CustomerResponse(Long id, String name, String email, String mobile, String address, String preferences, String purchaseHistory) {}
