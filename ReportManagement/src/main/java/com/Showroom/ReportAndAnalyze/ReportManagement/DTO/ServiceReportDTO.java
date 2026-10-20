package com.Showroom.ReportAndAnalyze.ReportManagement.DTO;

public class ServiceReportDTO {

	private String serviceType;
	private long count;
	private double totalEarnings;
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public double getTotalEarnings() {
		return totalEarnings;
	}
	public void setTotalEarnings(double totalEarnings) {
		this.totalEarnings = totalEarnings;
	}
}
