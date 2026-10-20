package com.Showroom.Inventory.InventoryManagement.DTO;
import java.time.LocalDate;
import jakarta.validation.constraints.*;
import com.Showroom.Inventory.InventoryManagement.Entity.Vehicles;
import com.Showroom.Inventory.InventoryManagement.Entity.Category;
public final class VehiclesMapper {
    private VehiclesMapper() {}
    public static Vehicles toEntity(VehiclesRequest request) {
        Vehicles entity = new Vehicles();
        entity.setVin(request.vin());
        entity.setMake(request.make());
        entity.setModel(request.model());
        entity.setColor(request.color());
        entity.setYear(request.year());
        entity.setMileage(request.mileage());
        entity.setPrice(request.price());
        entity.setCategory(request.category());
        entity.setStock(request.stock());
        entity.setDateAdded(request.dateAdded());
        return entity;
    }
    public static VehiclesResponse toResponse(Vehicles entity) {
        return new VehiclesResponse(entity.getId(), entity.getVin(), entity.getMake(), entity.getModel(), entity.getColor(), entity.getYear(), entity.getMileage(), entity.getPrice(), entity.getCategory(), entity.getStock(), entity.getDateAdded());
    }
}