package com.techelevator.model;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Park {
	
	private int parkId;
	private String parkName;
	private String parkLocation;
	private LocalDate estDate;
	private int areaSqFt;
	private int visitorsPerYear;
	private String description;
	
//Getters & Setters
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getParkLocation() {
		return parkLocation;
	}
	public void setParkLocation(String parkLocation) {
		this.parkLocation = parkLocation;
	}
	public LocalDate getEstDate() {
		return estDate;
	}
	public void setEstDate(LocalDate estDate) {
		this.estDate = estDate;
	}
	public int getAreaSqFt() {
		return areaSqFt;
	}
	public void setAreaSqFt(int areaSqFt) {
		this.areaSqFt = areaSqFt;
	}
	public int getVisitorsPerYear() {
		return visitorsPerYear;
	}
	public void setVisitorsPerYear(int visitorsPerYear) {
		this.visitorsPerYear = visitorsPerYear;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		String result = "";
		int maxLineLength = 75;
		int lineLength = 0;
		for (int i = 0; i < description.length(); i++) {
			result+= description.charAt(i);
			lineLength++;
			if(lineLength >= maxLineLength) {
				if (description.charAt(i) == (' ')) {
					result += "\n";
					lineLength = 0;
				}
			}
		}
		
		this.description = result;
	}
	
	@Override
	public String toString() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		DecimalFormat numberFormat = new DecimalFormat("#,###");
		String result = "";
		result += parkName + "\n";
		result += String.format("%-20s %s\n", "Location:", parkLocation);
		result += String.format("%-20s %s\n", "Established:", dateFormatter.format(estDate));
		result += String.format("%-20s %s sq km\n", "Area:", numberFormat.format(areaSqFt));
		result += String.format("%-20s %s\n", "Annual Visitors:", numberFormat.format(visitorsPerYear));
		result += "\n";
		result += description;
		
		return result;
	}
}
