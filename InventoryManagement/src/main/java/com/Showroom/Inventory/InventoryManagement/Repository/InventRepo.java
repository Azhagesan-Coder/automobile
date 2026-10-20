package com.Showroom.Inventory.InventoryManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Showroom.Inventory.InventoryManagement.Entity.Category;
import com.Showroom.Inventory.InventoryManagement.Entity.Vehicles;

@Repository
public interface InventRepo extends JpaRepository<Vehicles, Long> {

	List<Vehicles> findByCategory(@Param("category") Category category);
	Optional<Vehicles> findByVin(String vin);
}
