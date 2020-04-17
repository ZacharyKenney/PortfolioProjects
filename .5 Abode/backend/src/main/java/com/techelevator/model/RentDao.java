package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;

public interface RentDao {

    public List<RentCycle> getAllRent();
    public List<Payment> getAllPayments();
    public void createRentCycle(RentCycle rentCycle);
    public void updateRentCycle(RentCycle rentCycle);
    public void createPayment(Payment payment);
    public List<Payment> getAllPaymentsByLeaseId(int leaseId);
    public List<RentCycle> getAllRentCyclesByLeaseId(int leaseId);
    public RentCycle getCurrentRentByLeaseId(int leaseId);
    public BigDecimal getOverdueBalanceForLease(int leaseId);
    public List<Payment> getPaymentsByRentCycleId(int rentCycleId);
    public RentCycle getRentCycleById(int rentCycleId);
}