package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;

public interface LeaseDao {

    public List<Lease> getAllLeases();
    
    public List<Lease> getLeasesForLandlord(int landlordUserId);
    
    public List<Lease> getLeasesForRenter(int renterUserId);

    public Lease getCurrentLeaseForRenter(int renterUserId);

    public Lease getLeaseById(int leaseId);

    public void createLease(Lease newLease);
    
	public void updateLease(Lease lease);
    
    public void updateRentCycles(int leaseId, BigDecimal overdueBalance, BigDecimal lateFee);
}