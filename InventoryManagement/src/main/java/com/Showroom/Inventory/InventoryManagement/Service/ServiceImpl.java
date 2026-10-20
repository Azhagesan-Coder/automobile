package com.Showroom.Inventory.InventoryManagement.Service;

import java.util.List;

import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Showroom.Inventory.InventoryManagement.Entity.Category;
import com.Showroom.Inventory.InventoryManagement.Entity.Vehicles;
import com.Showroom.Inventory.InventoryManagement.Exception.NotFoundException;
import com.Showroom.Inventory.InventoryManagement.Repository.InventRepo;


@Service
public class ServiceImpl implements ServiceInter{

	@Autowired
	private InventRepo repo;
	
		
	@Override
	public Vehicles addVehicle(Vehicles vehicle) {
		// TODO Auto-generated method stub
		try{
			return repo.save(vehicle);
		}catch (StaleObjectStateException e) {
			// TODO: handle exception
			throw new RuntimeException("the vehicle class modified by another user. please reload and check");
		}
	}

	@Override
	public Vehicles updateVehicle(Long id, Vehicles updatedVehicle) {
		// TODO Auto-generated method stub
		Vehicles existing = repo.findById(id).orElseThrow(() -> new NotFoundException("vehicle not found"+ id +"with this id"));
		updatedVehicle.setId(existing.getId());
		return repo.save(updatedVehicle);
	}

	@Override
	public List<Vehicles> getAllVehicles() {
		// TODO Auto-generated method stub
		return repo.findAll();
	}

	@Override
	public Vehicles getVehicleById(Long id) {
		// TODO Auto-generated method stub
		return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "vehicle not found"+id+"for this id"));
	}

	@Override
	public void deleteVehicle(Long id) {
		// TODO Auto-generated method stub
		repo.deleteById(id);
	}

	@Override
	public List<Vehicles> getVehiclesByCategory(Category category) {
		// TODO Auto-generated method stub
		return repo.findByCategory(category);
	}
}
