package com.Showroom.Sales.SalesManagement.Entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Sales {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; 
	
	private String customerId;
	private String carId;
	private Double amount;
	private LocalDate saleDate;
	
	public Sales() {}

    public Sales(Long object, String string, String string2, double i, LocalDate now) {
		// TODO Auto-generated constructor stub
		this.id = (Long) object;
		this.customerId=string;
		this.carId=string2;
		this.amount=(double) i;
		this.saleDate=now;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public LocalDate getSaleDate() {
		return saleDate;
	}
	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}
}
