package com.Showroom.Service.ServiceMangement.Service;

import java.util.List;


import com.Showroom.Service.ServiceMangement.Enitity.ServiceRecord;

public interface ServiceInter {

	public ServiceRecord scheduleAppointment(ServiceRecord record);
	public ServiceRecord updateService(Long id, String status);
	void deleteService(Long id);
	List<ServiceRecord> getAllService(Long vehicleId);
}
