package com.Showroom.Inventory.InventoryManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Inventory.InventoryManagement.Entity.Vehicles;
import com.Showroom.Inventory.InventoryManagement.Entity.Category;
public record VehiclesRequest(@NotBlank String vin, @NotBlank String make, @NotBlank String model, String color, Integer year, @PositiveOrZero Double mileage, @NotNull @PositiveOrZero Double price, @NotNull Category category, @NotNull @PositiveOrZero Integer stock, LocalDate dateAdded) {}
