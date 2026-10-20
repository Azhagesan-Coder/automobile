package com.Customer.CustomerManagement.Service;

import java.util.List;

import com.Customer.CustomerManagement.Entities.Customer;

public interface CustomerServiceInter {

	Customer createCustomer(Customer customer);
	Customer getCustomerById(Long id);
	List<Customer> getAllCustomers();
	Customer updateCustomer(Long id, Customer custtomer);
	void deleteCustomer(Long id);
}
