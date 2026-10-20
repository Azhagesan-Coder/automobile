package com.Showroom.ReportAndAnalyze.ReportManagement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.Showroom.ReportAndAnalyze.ReportManagement.Service.ReportService;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest(properties = {"spring.cloud.config.enabled=false", "spring.cloud.discovery.enabled=false", "eureka.client.enabled=false"})
class ReportContextTest {
    @Autowired ReportService service;
    @Test void reportServiceStartsWithoutDatabase() { assertNotNull(service); }
}
