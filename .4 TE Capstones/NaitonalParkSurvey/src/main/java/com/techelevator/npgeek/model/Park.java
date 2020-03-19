package com.techelevator.npgeek.model;

public class Park {
	
	private String parkCode;
	private String parkName;
	private String imageName;
	private String state;
	private long acreage;
	private long elevationInFeet;
	private float milesOfTrail;
	private long numberOfCampsites;
	private String climate;
	private int yearFounded;
	private long annualVisitors;
	private String inspirationalQuote;
	private String inspQuoteSource;
	private String description;
	private int entryFee;
	private int numberOfAnimalSpecies;
	private boolean isLeader = false;
	private String location;
	
	// Getters And Setters
	public String getParkCode() {
		return parkCode;
	}
	public void setParkCode(String parkCode) {
		this.parkCode = parkCode;
		this.imageName = parkCode.toLowerCase() + ".jpg";
	}
	public String getParkName() {
		return parkName;
	}
	public void setParkName(String parkName) {
		this.parkName = parkName;
	}
	public String getImageName() {
		return imageName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getAcreage() {
		return acreage;
	}
	public void setAcreage(long acreage) {
		this.acreage = acreage;
	}
	public long getElevationInFeet() {
		return elevationInFeet;
	}
	public void setElevationInFeet(long elevationInFeet) {
		this.elevationInFeet = elevationInFeet;
	}
	public double getMilesOfTrail() {
		return milesOfTrail;
	}
	public void setMilesOfTrail(float milesOfTrail) {
		this.milesOfTrail = milesOfTrail;
	}
	public long getNumberOfCampsites() {
		return numberOfCampsites;
	}
	public void setNumberOfCampsites(long numberOfCampsites) {
		this.numberOfCampsites = numberOfCampsites;
	}
	public String getClimate() {
		return climate;
	}
	public void setClimate(String climate) {
		this.climate = climate;
	}
	public int getYearFounded() {
		return yearFounded;
	}
	public void setYearFounded(int yearFounded) {
		this.yearFounded = yearFounded;
	}
	public long getAnnualVisitors() {
		return annualVisitors;
	}
	public void setAnnualVisitors(long annualVisitors) {
		this.annualVisitors = annualVisitors;
	}
	public String getInspirationalQuote() {
		return inspirationalQuote;
	}
	public void setInspirationalQuote(String inspirationalQuote) {
		this.inspirationalQuote = inspirationalQuote;
	}
	public String getInspQuoteSource() {
		return inspQuoteSource;
	}
	public void setInspQuoteSource(String inspQuoteSource) {
		this.inspQuoteSource = inspQuoteSource;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getEntryFee() {
		return entryFee;
	}
	public void setEntryFee(int entryFee) {
		this.entryFee = entryFee;
	}
	public int getNumberOfAnimalSpecies() {
		return numberOfAnimalSpecies;
	}
	public void setNumberOfAnimalSpecies(int numberOfAnimalSpecies) {
		this.numberOfAnimalSpecies = numberOfAnimalSpecies;
	}
	public boolean isLeader() {
		return isLeader;
	}
	public void setLeader(boolean isLeader) {
		this.isLeader = isLeader;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
}
