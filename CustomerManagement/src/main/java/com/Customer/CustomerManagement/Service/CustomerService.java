package com.Customer.CustomerManagement.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.Customer.CustomerManagement.Client.InventoryClient;
import com.Customer.CustomerManagement.Entities.Customer;
import com.Customer.CustomerManagement.Entities.InventoryResponse;
import com.Customer.CustomerManagement.Exception.DuplicateEntryException;
import com.Customer.CustomerManagement.Exception.NotFoundException;
import com.Customer.CustomerManagement.Repository.CustomerRepository;

@Service
public class CustomerService implements CustomerServiceInter {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private InventoryClient inventoryClient;

	@Override
	public Customer createCustomer(Customer customer) {
		if(customerRepository.findByEmail(customer.getEmail()).isPresent()) {
			throw new DuplicateEntryException("Customer already present this email");
		}
		return customerRepository.save(customer);
	}

	@Override
	public Customer getCustomerById(Long id) {
		// TODO Auto-generated method stub
		return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found by this id"));
	}

	@Override
	public List<Customer> getAllCustomers() {
		// TODO Auto-generated method stub
		return customerRepository.findAll();
	}

	@Override
	public Customer updateCustomer(Long id, Customer updateCustomer) {
		// TODO Auto-generated method stub
		Customer exist = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("customer not found create new customer"));
//		if(exist.getId() == null) {
//			
//		}
		customerRepository.findByEmail(updateCustomer.getEmail()).filter(other -> !other.getId().equals(id))
            .ifPresent(other -> { throw new DuplicateEntryException("Email already belongs to another customer"); });
        updateCustomer.setId(id);
		return customerRepository.save(updateCustomer);
	}

	@Override
	public void deleteCustomer(Long id) {
		// TODO Auto-generated method stub
		customerRepository.deleteById(id);		
	}

	public InventoryResponse fetchCarDetails(Long carId) {
		return inventoryClient.getInventoryDetails(carId);
	}
}
