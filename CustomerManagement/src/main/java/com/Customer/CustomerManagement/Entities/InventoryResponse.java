package com.Customer.CustomerManagement.Entities;


public class InventoryResponse {

	@com.fasterxml.jackson.annotation.JsonAlias("id")
    private Long carId;
	private String model;
	private String variant;
	@com.fasterxml.jackson.annotation.JsonAlias("stock")
    private int quantity;
	private double price;
	
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
