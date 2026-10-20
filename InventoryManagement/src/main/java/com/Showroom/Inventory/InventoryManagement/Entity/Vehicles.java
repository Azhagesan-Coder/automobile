package com.Showroom.Inventory.InventoryManagement.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Vehicles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique = true)
	private String vin;
	private String make;
	private String model;
	private String color;
	private Integer year;
	private Double mileage;
	private Double price;

	
	@Enumerated(EnumType.STRING)
	private Category category;
	private Integer stock;
	private LocalDate dateAdded;
	
	@Override
	public String toString() {
		return "Vehicles [id=" + id + ", vin=" + vin + ", make=" + make + ", model=" + model + ", color=" + color
				+ ", year=" + year + ", mileage=" + mileage + ", price=" + price + ", category=" + category + ", stock="
				+ stock + ", dateAdded=" + dateAdded + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Double getMileage() {
		return mileage;
	}
	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public LocalDate getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded = dateAdded;
	}
}
