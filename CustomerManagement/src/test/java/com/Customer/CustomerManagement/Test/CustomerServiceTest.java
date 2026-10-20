package com.Customer.CustomerManagement.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.Customer.CustomerManagement.Entities.Customer;
import com.Customer.CustomerManagement.Repository.CustomerRepository;
import com.Customer.CustomerManagement.Service.CustomerService;


@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	
	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerService customerService;
	
	private Customer customer;
	
	@BeforeEach
	void setUp() {
		customer = new Customer();
		customer.setId(1L);
		customer.setName("John");
		customer.setEmail("john@gmail.com");
		customer.setAddress("chennai, tamil nadu, india");
		customer.setMobile("8754274675");
	}
	
	@Test
	public void testCreateCustomer() {
		when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
		
		Customer saved = customerService.createCustomer(customer);
		assertNotNull(saved);
		assertEquals("John", saved.getName());
	}
	
	public void testUpdateCustomer() {
		when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.empty());
	}
}
