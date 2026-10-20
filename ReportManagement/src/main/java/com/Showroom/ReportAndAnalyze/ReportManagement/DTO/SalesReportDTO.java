package com.Showroom.ReportAndAnalyze.ReportManagement.DTO;

public class SalesReportDTO {

	public SalesReportDTO(String carModel, Long unitsSold, Double totalRevenue, Double avgPrice) {
		super();
		this.carModel = carModel;
		this.unitsSold = unitsSold;
		this.totalRevenue = totalRevenue;
		this.avgPrice = avgPrice;
	}
	
	private String carModel;
	private Long unitsSold;
	private Double totalRevenue;
	private Double avgPrice;
	
	public String getCarModel() {
		return carModel;
	}
	public void setCarModel(String carModel) {
		this.carModel = carModel;
	}
	public long getUnitsSold() {
		return unitsSold;
	}
	public void setUnitsSold(long unitsSold) {
		this.unitsSold = unitsSold;
	}
	public double getTotalRevenue() {
		return totalRevenue;
	}
	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
	public Double getAvgPrice() {
		return avgPrice;
	}
	public void setAvgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
	}
	
}
