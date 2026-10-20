package com.Customer.CustomerManagement.Config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	
	
//	@Bean
//	public WebClient webClient() {
//		return WebClient
//				.builder()
//				.baseUrl("http://fileupload-and-download")
//				.filter(filterFunction)
//				.build();
//	}
//	
//	@Bean
//	public InventoryClient fileClient() {
//		HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
//				.builder(WebClientAdapter.forClient(webClient()))
//				.build();
//		return httpServiceProxyFactory.createClient(InventoryClient.class);
//	}
	
	@Bean
	@LoadBalanced
	public WebClient.Builder webClientBuilder(){
		return WebClient.builder();
	}
}
