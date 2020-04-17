package com.techelevator.model;

import java.time.LocalDate;

public class MaintenanceRequest {

    private int requestId;
    private int unitId;
    private int requestUserId;
    private String requesterFullName;
    private String requesterEmail;
    private String requestDesc;
    private int priority;
    private LocalDate dateRequested;
    private int assignedUserId;
    private boolean completed;
    private String UnitNumber;
    private String propertyName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;

    public int getRequestId() {
        return requestId;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequesterFullName() {
        return requesterFullName;
    }

    public void setRequesterFullName(String requesterFullName) {
        this.requesterFullName = requesterFullName;
    }

    public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(final String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(final String propertyName) {
        this.propertyName = propertyName;
    }

    public String getUnitNumber() {
        return UnitNumber;
    }

    public void setUnitNumber(final String unitNumber) {
        this.UnitNumber = unitNumber;
    }

    public LocalDate getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(final LocalDate dateRequested) {
        this.dateRequested = dateRequested;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(final boolean completed) {
        this.completed = completed;
    }

    public int getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(final int assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(final int priority) {
        this.priority = priority;
    }

    public String getRequestDesc() {
        return requestDesc;
    }

    public void setRequestDesc(final String requestDesc) {
        this.requestDesc = requestDesc;
    }

    public int getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(final int requestUserId) {
        this.requestUserId = requestUserId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(final int unitId) {
        this.unitId = unitId;
    }

    public void setRequestId(final int requestId) {
        this.requestId = requestId;
    }

}