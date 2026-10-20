package com.Showroom.ReportAndAnalyze.ReportManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ReportManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReportManagementApplication.class, args);
	}

}
