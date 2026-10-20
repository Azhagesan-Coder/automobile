package com.Showroom.Inventory.InventoryManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Inventory.InventoryManagement.Entity.Vehicles;
import com.Showroom.Inventory.InventoryManagement.Entity.Category;
public record VehiclesResponse(Long id, String vin, String make, String model, String color, Integer year, Double mileage, Double price, Category category, Integer stock, LocalDate dateAdded) {}
