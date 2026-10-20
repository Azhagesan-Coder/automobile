package com.Showroom.Sales.SalesManagement.Service;

import java.util.List;

import com.Showroom.Sales.SalesManagement.Entities.Sales;

public interface SalesServiceInter {

	Sales createSale(Sales sale);
	List<Sales> getAllSales();
}
