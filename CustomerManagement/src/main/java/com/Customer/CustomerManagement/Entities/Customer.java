package com.Customer.CustomerManagement.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(nullable = false, unique = true)
    private String email;
	private String mobile;
	private String address;
	private String preferences;
	
	@Column(name ="purchase_history")
	private String purchaseHistory;

	@Override
	public String toString() {
		return "CustomModel [id=" + id + ", name=" + name + ", email=" + email + ", mobile=" + mobile + ", address="
				+ address + ", preferences=" + preferences + ", purchaseHistory=" + purchaseHistory + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPreferences() {
		return preferences;
	}

	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}

	public String getPurchaseHistory() {
		return purchaseHistory;
	}

	public void setPurchaseHistory(String purchaseHistory) {
		this.purchaseHistory = purchaseHistory;
	}
}
