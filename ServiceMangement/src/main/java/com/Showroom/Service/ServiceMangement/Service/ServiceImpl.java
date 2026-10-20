package com.Showroom.Service.ServiceMangement.Service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Showroom.Service.ServiceMangement.Enitity.ServiceRecord;
import com.Showroom.Service.ServiceMangement.Exception.NotFoundException;
import com.Showroom.Service.ServiceMangement.Repository.ServiceRepository;

@Service
public class ServiceImpl implements ServiceInter {

	@Autowired
	private ServiceRepository repositoryImpl;

	@Override
	public ServiceRecord scheduleAppointment(ServiceRecord recordService) {
		// TODO Auto-generated method stub
		recordService.setStatus("Scheduled");
		return repositoryImpl.save(recordService);
	}

	@Override
	public List<ServiceRecord> getAllService(Long vehicleId) {
		// TODO Auto-generated method stub
		return repositoryImpl.findByVehicleId(vehicleId);
	}

	@Override
	public ServiceRecord updateService(Long id, String status) {
		// TODO Auto-generated method stub
		ServiceRecord record = repositoryImpl.findById(id).orElseThrow(() -> new NotFoundException("record not found"));
		record.setStatus(status);
		return repositoryImpl.save(record);
	}

	@Override
	public void deleteService(Long id) {
		// TODO Auto-generated method stub
		repositoryImpl.deleteById(id);
	}

	public List<ServiceRecord> getAllServices() {
		// TODO Auto-generated method stub
		return repositoryImpl.findAll();
	}
	
}
