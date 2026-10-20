package com.Showroom.Sales.SalesManagement.Service;
import com.Showroom.Sales.SalesManagement.DTO.*;
import com.Showroom.Sales.SalesManagement.Entities.Sales;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
@Component
public class SalesReferenceClient {
    @org.springframework.beans.factory.annotation.Autowired
    private com.Showroom.Sales.SalesManagement.Resilience.DownstreamCircuits circuits = new com.Showroom.Sales.SalesManagement.Resilience.DownstreamCircuits();
    private final RestTemplate client;
    public SalesReferenceClient(RestTemplate client) { this.client = client; }
    public void validateSale(Sales sale) {
        try {
            CustomerReference customer = circuits.call("customer", () -> client.getForObject("http://customer-management/api/customers/{id}", CustomerReference.class, sale.getCustomerId()));
            VehicleReference vehicle = circuits.call("inventory", () -> client.getForObject("http://inventory-management/api/invent/{id}", VehicleReference.class, sale.getCarId()));
            if (customer == null || customer.id() == null || vehicle == null || vehicle.id() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer or vehicle does not exist");
            }
            if (vehicle.stock() == null || vehicle.stock() <= 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is out of stock");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer or vehicle does not exist");
        } catch (RestClientException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to validate sale references", e);
        }
    }
}
