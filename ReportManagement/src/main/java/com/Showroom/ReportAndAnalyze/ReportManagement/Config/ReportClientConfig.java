package com.Showroom.ReportAndAnalyze.ReportManagement.Config;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;
@Configuration
public class ReportClientConfig {
    @Bean @LoadBalanced
    public RestTemplate reportRestTemplate(RestTemplateBuilder builder) {
        return builder.setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(10)).build();
    }
}