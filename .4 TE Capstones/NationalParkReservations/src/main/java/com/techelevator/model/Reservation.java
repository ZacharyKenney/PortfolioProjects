package com.techelevator.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

	private int reservationId;
	private int siteId;
	private int campgroundId = -1;
	private String reservationName;
	private LocalDate startDate;
	private LocalDate endDate;
	private int durationDays;
	private LocalDate createDate;
	private BigDecimal costPerDay;
	private boolean usedAdvanceSearch;
	private int partySize;
	private int rVLength;
	private boolean accessible;
	private boolean utility;

	

	// Constructor
	public Reservation() {

	}

	public Reservation(LocalDate arriveTime, LocalDate departTime) {
		setStartDate(arriveTime);
		setEndDate(departTime);
		setDurationDays(departTime);
		setCampgroundId(0);
	}

	public Reservation(Campground c, LocalDate arriveTime, LocalDate departTime) {
		// setSiteId(c.getCampgroundId());
		setCampgroundId(c.getCampgroundId());
		setStartDate(arriveTime);
		setEndDate(departTime);
		setDurationDays(departTime);
		setCostPerDay(c.getDailyFee());

	}

	public Reservation(int reservationId, int siteId, String reservationName, LocalDate startDate, int durationDays,
			LocalDate createDate) {
		this.reservationId = reservationId;
		this.siteId = siteId;
		this.reservationName = reservationName;
		this.startDate = startDate;
		this.durationDays = durationDays;
		this.createDate = createDate;
		this.endDate = startDate.plusDays(durationDays);
	}
	
	public Reservation(Campground c, LocalDate arriveTime, LocalDate departTime, int partySize, 
						int rVLength, boolean handicapNeeded, boolean utilitiesNeeded) {
		this.usedAdvanceSearch = true;
		setCampgroundId(c.getCampgroundId());
		setStartDate(arriveTime);
		setEndDate(departTime);
		setDurationDays(departTime);
		setCostPerDay(c.getDailyFee());
		
		this.partySize = partySize;
		this.rVLength = rVLength;
		this.accessible = handicapNeeded;
		this.utility = utilitiesNeeded;
	}

// Getters & Setters
	public BigDecimal getCostPerDay() {
		return costPerDay;
	}

	public void setCostPerDay(BigDecimal costPerDay) {
		this.costPerDay = costPerDay;
	}

	public int getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public int getDurationDays() {
		return durationDays;
	}

	public void setDurationDays(LocalDate departTime) {
		this.durationDays = (int) ChronoUnit.DAYS.between(startDate, departTime);
	}

	public LocalDate getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDate createDate) {
		this.createDate = createDate;
	}
	
	public boolean getUsedAdvanceSearch() {
		return usedAdvanceSearch;
	}

	public void setUsedAdvanceSearch(boolean usedAdvanceSearch) {
		this.usedAdvanceSearch = usedAdvanceSearch;
	}

	public int getPartySize() {
		return partySize;
	}

	public void setPartySize(int maxOccupancy) {
		this.partySize = maxOccupancy;
	}

	public int getRVLength() {
		return rVLength;
	}

	public void setRVLength(int maxRVLength) {
		this.rVLength = maxRVLength;
	}

	public boolean isAccessible() {
		return accessible;
	}

	public void setAccessible(boolean accessible) {
		this.accessible = accessible;
	}

	public boolean isUtility() {
		return utility;
	}

	public void setUtility(boolean utility) {
		this.utility = utility;
	}
	@Override
	public String toString() {
		return String.format("%-20d %-20d %-20s %-20s %-20d %-20s", reservationId, siteId, reservationName, startDate, durationDays, createDate.toString());
	}
}
