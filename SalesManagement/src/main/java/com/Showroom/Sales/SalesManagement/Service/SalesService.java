package com.Showroom.Sales.SalesManagement.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Showroom.Sales.SalesManagement.Entities.Sales;
import com.Showroom.Sales.SalesManagement.Repository.SalesRepository;

@Service
public class SalesService implements SalesServiceInter {

	
	private final SalesRepository salesRepository;
	private final SalesReferenceClient referenceClient;
	
	public SalesService(SalesRepository salesRepository, SalesReferenceClient referenceClient) {
		this.salesRepository=salesRepository;
		this.referenceClient=referenceClient;
	}
	
	@Override
	public Sales createSale(Sales sale) {
		// TODO Auto-generated method stub
		referenceClient.validateSale(sale);
		return salesRepository.save(sale);
	}

	@Override
	public List<Sales> getAllSales() {
		// TODO Auto-generated method stub
		return salesRepository.findAll();
	}
}
