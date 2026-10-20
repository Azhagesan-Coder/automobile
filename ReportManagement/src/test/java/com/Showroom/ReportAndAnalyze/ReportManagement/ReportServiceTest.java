package com.Showroom.ReportAndAnalyze.ReportManagement;
import java.time.LocalDate;
import com.Showroom.ReportAndAnalyze.ReportManagement.DTO.*;
import com.Showroom.ReportAndAnalyze.ReportManagement.Service.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ReportServiceTest {
    @Test void aggregatesLiveSalesAndIncludesBothDateBoundaries() {
        RestTemplate client = mock(RestTemplate.class);
        LocalDate from = LocalDate.of(2026, 10, 1), to = LocalDate.of(2026, 10, 31);
        when(client.getForObject("http://sales-management/api/sales", SaleSource[].class)).thenReturn(new SaleSource[] {
            new SaleSource(1L, "1", "7", 100.0, from), new SaleSource(2L, "1", "7", 200.0, to),
            new SaleSource(3L, "1", "7", 900.0, from.minusDays(1))
        });
        when(client.getForObject("http://inventory-management/api/invent/{id}", VehicleSource.class, "7")).thenReturn(new VehicleSource(7L, "City"));
        var report = new ReportService(client).getMonthlyReport(from, to);
        assertEquals(1, report.size()); assertEquals("City", report.get(0).getCarModel());
        assertEquals(2, report.get(0).getUnitsSold()); assertEquals(300.0, report.get(0).getTotalRevenue());
        assertEquals(150.0, report.get(0).getAvgPrice());
        verify(client, times(1)).getForObject("http://inventory-management/api/invent/{id}", VehicleSource.class, "7");
    }
    @Test void invertedRangeIsRejectedWithoutCallingServices() {
        RestTemplate client = mock(RestTemplate.class);
        assertEquals(400, assertThrows(ResponseStatusException.class, () -> new ReportService(client)
            .getMonthlyReport(LocalDate.of(2026,10,31), LocalDate.of(2026,10,1))).getStatusCode().value());
        verifyNoInteractions(client);
    }
    @Test void failedSalesSourceOpensCircuitWithoutReturningEmptyReport() {
        RestTemplate client = mock(RestTemplate.class);
        ReportService service = new ReportService(client);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "circuits",
            new com.Showroom.ReportAndAnalyze.ReportManagement.Resilience.DownstreamCircuits(2, 2, 50, 30, 2));
        when(client.getForObject("http://sales-management/api/sales", SaleSource[].class))
            .thenThrow(new org.springframework.web.client.ResourceAccessException("timeout"));
        LocalDate day = LocalDate.of(2026, 10, 1);
        for (int attempt = 0; attempt < 2; attempt++)
            assertEquals(502, assertThrows(ResponseStatusException.class, () -> service.getMonthlyReport(day, day)).getStatusCode().value());
        assertEquals(503, assertThrows(ResponseStatusException.class, () -> service.getMonthlyReport(day, day)).getStatusCode().value());
        verify(client, times(2)).getForObject("http://sales-management/api/sales", SaleSource[].class);
        verifyNoMoreInteractions(client);
    }
}
