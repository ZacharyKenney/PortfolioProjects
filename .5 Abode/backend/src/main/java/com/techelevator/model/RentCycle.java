package com.techelevator.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RentCycle {
    private int id;
    private int leaseId;
    private LocalDate startDate;
    private LocalDate dueDate;
    private BigDecimal balance;
    private String rentStatus;

    public int getId() {
        return id;
    }
    public void setId(final int id) {
        this.id = id;
    }
    public int getLeaseId() {
        return leaseId;
    }
    public void setLeaseId(int leaseId) {
        this.leaseId = leaseId;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getRentStatus() {
        return rentStatus;
    }
    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }
}