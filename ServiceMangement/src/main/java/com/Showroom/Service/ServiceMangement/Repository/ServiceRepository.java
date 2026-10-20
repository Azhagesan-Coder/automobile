package com.Showroom.Service.ServiceMangement.Repository;


import java.time.LocalDate;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Showroom.Service.ServiceMangement.Enitity.ServiceRecord;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceRecord, Long> {

	List<ServiceRecord> findByVehicleId(Long vehicleId);
	List<ServiceRecord> findByAppointmentDate(LocalDate date);
}
