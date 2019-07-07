package br.com.jproberto.desafioGrupoZap.core.model;

public class PricingInfos {
	private String period;
	private double yearlyIptu;
	private double price;
	private double rentalTotalPrice;
	private String businessType;
	private double monthlyCondoFee;

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public double getYearlyIptu() {
		return yearlyIptu;
	}

	public void setYearlyIptu(double yearlyIptu) {
		this.yearlyIptu = yearlyIptu;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getRentalTotalPrice() {
		return rentalTotalPrice;
	}

	public void setRentalTotalPrice(double rentalTotalPrice) {
		this.rentalTotalPrice = rentalTotalPrice;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public double getMonthlyCondoFee() {
		return monthlyCondoFee;
	}

	public void setMonthlyCondoFee(double monthlyCondoFee) {
		this.monthlyCondoFee = monthlyCondoFee;
	}
}
