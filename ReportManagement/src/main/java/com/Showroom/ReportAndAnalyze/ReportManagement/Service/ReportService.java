package com.Showroom.ReportAndAnalyze.ReportManagement.Service;
import java.time.LocalDate;
import java.util.*;
import com.Showroom.ReportAndAnalyze.ReportManagement.DTO.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
@Service
public class ReportService {
    @org.springframework.beans.factory.annotation.Autowired
    private com.Showroom.ReportAndAnalyze.ReportManagement.Resilience.DownstreamCircuits circuits = new com.Showroom.ReportAndAnalyze.ReportManagement.Resilience.DownstreamCircuits();
    private final RestTemplate client;
    public ReportService(RestTemplate client) { this.client = client; }
    public List<SalesReportDTO> getMonthlyReport(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "from must not be after to");
        try {
            SaleSource[] sales = circuits.call("sales", () -> client.getForObject("http://sales-management/api/sales", SaleSource[].class));
            Map<String, String> models = new HashMap<>();
            Map<String, DoubleSummaryStatistics> totals = new TreeMap<>();
            if (sales == null) return List.of();
            for (SaleSource sale : sales) {
                if (sale.saleDate() == null || sale.amount() == null || sale.saleDate().isBefore(from) || sale.saleDate().isAfter(to)) continue;
                String model = models.computeIfAbsent(sale.carId(), id -> {
                    try {
                        VehicleSource vehicle = circuits.call("inventory", () -> client.getForObject("http://inventory-management/api/invent/{id}", VehicleSource.class, id));
                        return vehicle != null && vehicle.model() != null ? vehicle.model() : "Unknown vehicle " + id;
                    } catch (HttpClientErrorException.NotFound e) { return "Unknown vehicle " + id; }
                });
                totals.computeIfAbsent(model, ignored -> new DoubleSummaryStatistics()).accept(sale.amount());
            }
            return totals.entrySet().stream().map(entry -> new SalesReportDTO(entry.getKey(),
                entry.getValue().getCount(), entry.getValue().getSum(), entry.getValue().getAverage())).toList();
        } catch (RestClientException | IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Unable to retrieve report source data", e);
        }
    }
}
