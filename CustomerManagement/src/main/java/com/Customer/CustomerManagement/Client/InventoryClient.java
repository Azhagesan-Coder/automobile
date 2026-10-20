package com.Customer.CustomerManagement.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.Customer.CustomerManagement.Entities.InventoryResponse;

import reactor.core.publisher.Mono;

@Component
public class InventoryClient {
    @org.springframework.beans.factory.annotation.Autowired
    private com.Customer.CustomerManagement.Resilience.DownstreamCircuits circuits = new com.Customer.CustomerManagement.Resilience.DownstreamCircuits();

	@Autowired
	private WebClient.Builder webClientBuilder;
	
	@Value("${inventory.service.name:inventory-management}")
	private String inventoryServiceName;
	
	public InventoryResponse getInventoryDetails(Long carId) {
		try { return circuits.call("inventory", () -> webClientBuilder.build()
				.get()
				.uri("http://" + inventoryServiceName + "/api/invent/{carId}", carId)
				.retrieve()
				.bodyToMono(InventoryResponse.class)
				.block(java.time.Duration.ofSeconds(10)));
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException.NotFound e) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found");
        } catch (org.springframework.web.server.ResponseStatusException e) { throw e;
        } catch (RuntimeException e) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to retrieve inventory details", e);
        }
	}
}
