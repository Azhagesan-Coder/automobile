package com.Showroom.Sales.SalesManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SalesManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalesManagementApplication.class, args);
	}

}
