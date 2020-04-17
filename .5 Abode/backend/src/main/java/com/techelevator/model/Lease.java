package com.techelevator.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Lease {

    private int leaseId;
    private int userId;
    private int unitId;
    private LocalDate signedDate;
    private LocalDate startDate;
    private int rentLength;
    private BigDecimal rentAmount;
    private BigDecimal lateFee;
    private BigDecimal overdueBalance;
    private String status;
    private String propertyName;
    private String propertyAddress;
    private String unitNumber;
    private String renterName;
    private String renterEmail;
    private String currentRentStatus;

    @Override
    public String toString() {
        return leaseId + " " + userId + " " + unitId + " " + status;
    }

    public String getRenterEmail() {
        return renterEmail;
    }

    public void setRenterEmail(String renterEmail) {
        this.renterEmail = renterEmail;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public int getLeaseId() {
        return leaseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }
    public BigDecimal getOverdueBalance() {
        return overdueBalance;
    }

    public void setOverdueBalance(BigDecimal overdueBalance) {
        this.overdueBalance = overdueBalance;
    }
    public BigDecimal getRentAmount() {
        return rentAmount;
    }

    public void setRentAmount(BigDecimal rentAmount) {
        this.rentAmount = rentAmount;
    }

    public int getRentLength() {
        return rentLength;
    }

    public void setRentLength(int rentLength) {
        this.rentLength = rentLength;
    }

    public LocalDate getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDate signedDate) {
        this.signedDate = signedDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }
    public String getCurrentRentStatus() {
        return currentRentStatus;
    }

    public void setCurrentRentStatus(String currentRentStatus) {
        this.currentRentStatus = currentRentStatus;
    }
}