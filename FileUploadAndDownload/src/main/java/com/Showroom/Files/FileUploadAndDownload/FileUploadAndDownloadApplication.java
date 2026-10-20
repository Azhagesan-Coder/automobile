package com.Showroom.Files.FileUploadAndDownload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FileUploadAndDownloadApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadAndDownloadApplication.class, args);
	}

}
