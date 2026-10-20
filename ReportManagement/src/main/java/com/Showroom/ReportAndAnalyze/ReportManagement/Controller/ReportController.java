package com.Showroom.ReportAndAnalyze.ReportManagement.Controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Showroom.ReportAndAnalyze.ReportManagement.DTO.SalesReportDTO;
import com.Showroom.ReportAndAnalyze.ReportManagement.Service.ReportService;

@RestController
@RequestMapping("/api/report")
public class ReportController {

	@Autowired
	private ReportService reportService;
	
	@GetMapping("/sales/monthly")
	public ResponseEntity<List<SalesReportDTO>> getMonthlyReport(@RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to){
		List<SalesReportDTO> report = reportService.getMonthlyReport(from, to);
		return ResponseEntity.ok(report);
	}
}
