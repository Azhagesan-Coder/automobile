package com.Showroom.Sales.SalesManagement.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.Showroom.Sales.SalesManagement.Entities.Sales;
import com.Showroom.Sales.SalesManagement.Repository.SalesRepository;
import com.Showroom.Sales.SalesManagement.Service.SalesService;

//@SpringBootTest
//@ExtendWith()
public class ServiceTest {

	@Mock
	private SalesRepository salesRepository;
	@Mock
	private com.Showroom.Sales.SalesManagement.Service.SalesReferenceClient referenceClient;
	
	@InjectMocks
	private SalesService salesService;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	
	@Test
	public void testCreateSales() {
		Sales sale = new Sales(null, "cust1", "car1", 180900, LocalDate.now());
		Sales save = new Sales(1l, "cust1", "car1", 180900, LocalDate.now());
		
		when(salesRepository.save(sale)).thenReturn(save);
		
		Sales result =salesService.createSale(sale);
		assertNotNull(result.getId());
		assertEquals("cust1", result.getCustomerId());
	}
}
