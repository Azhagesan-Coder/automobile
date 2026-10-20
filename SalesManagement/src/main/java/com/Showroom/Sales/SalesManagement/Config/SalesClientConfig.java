package com.Showroom.Sales.SalesManagement.Config;
import java.time.Duration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestTemplate;
@Configuration
public class SalesClientConfig {
    @Bean @LoadBalanced
    public RestTemplate salesRestTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(10)).build();
    }
}
