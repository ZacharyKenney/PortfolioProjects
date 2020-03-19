package com.techelevator.model;

import java.math.BigDecimal;
import java.text.DateFormatSymbols;


public class Campground {

	private int campgroundId;
	private int parkId;
	private String campName;
	private String openMonth;
	private String closeMonth;
	private BigDecimal dailyFee;
	
	public int getCampgroundId() {
		return campgroundId;
	}
	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getCampName() {
		return campName;
	}
	public void setCampName(String campName) {
		this.campName = campName;
	}
	public String getOpenMonth() {
		return openMonth;
	}
	public void setOpenMonth(String openMonth) {
		this.openMonth = openMonth;
	}
	public String getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(String closeMonth) {
		this.closeMonth = closeMonth;
	}
	public BigDecimal getDailyFee() {
		return dailyFee;
	}
	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}
	@Override
	public String toString() {
		String openMonthName = new DateFormatSymbols().getMonths()[Integer.parseInt(openMonth) - 1];
		String closeMonthName = new DateFormatSymbols().getMonths()[Integer.parseInt(closeMonth) - 1];
		String result = String.format("%-35s %-15s %-15s $%-15.02f", campName, openMonthName, closeMonthName, dailyFee);
		return result;
	}
	
	
	
}
