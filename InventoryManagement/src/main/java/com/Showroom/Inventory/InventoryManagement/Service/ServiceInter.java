package com.Showroom.Inventory.InventoryManagement.Service;

import java.util.List;

import com.Showroom.Inventory.InventoryManagement.Entity.Category;
import com.Showroom.Inventory.InventoryManagement.Entity.Vehicles;


public interface ServiceInter {

	Vehicles addVehicle(Vehicles vehicle);
	Vehicles updateVehicle(Long id, Vehicles updatedVehicle);
	List<Vehicles> getAllVehicles();
	Vehicles getVehicleById(Long id);
	void deleteVehicle(Long id);
	List<Vehicles> getVehiclesByCategory(Category category);
}
