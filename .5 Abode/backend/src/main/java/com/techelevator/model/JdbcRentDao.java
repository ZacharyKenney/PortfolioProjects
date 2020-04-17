package com.techelevator.model;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcRentDao implements RentDao{

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcRentDao(DataSource datasource){
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override //passed test sql is good
    public void createRentCycle(RentCycle rentCycle){
        String sqlCreateRentCycle = "INSERT INTO rent_cycle(lease_id, start_date, balance, due_date, rent_status) "
        + "VALUES(?,?,?,?,?);";
        jdbcTemplate.update(sqlCreateRentCycle, rentCycle.getId(), rentCycle.getLeaseId(), rentCycle.getStartDate(), rentCycle.getBalance(), rentCycle.getDueDate(), rentCycle.getRentStatus());

    }

    @Override
    public void updateRentCycle(RentCycle rentCycle){
        String sqlCreateRentCycle = "UPDATE rent_cycle SET rent_status = ? "
        + "WHERE  rent_cycle_id = ?;";
        jdbcTemplate.update(sqlCreateRentCycle, rentCycle.getRentStatus(), rentCycle.getId());

    }

    @Override
    public void createPayment(Payment payment){
        String sqlFindRentCycle = "SELECT rent_cycle_id FROM rent_cycle " +
                                  "WHERE lease_id = ? AND start_date < ? AND due_date >= ?;";
        int rentCycleId = jdbcTemplate.queryForObject(sqlFindRentCycle, Integer.class, payment.getLeaseId(), LocalDate.now(), LocalDate.now());
        RentCycle rc = getRentCycleById(rentCycleId);

        payment.setRentCycleId(rentCycleId);                          
        String sqlCreatePayment = "INSERT INTO payment (rent_cycle_id, amount_paid, date_paid) "
                                + "VALUES(?, ?, ?);";                      
        jdbcTemplate.update(sqlCreatePayment, payment.getRentCycleId(), payment.getAmountPaid(), LocalDate.now());

        //Overdue Balance Check
        BigDecimal overdueBalance = getOverdueBalanceForLease(payment.getLeaseId());
        BigDecimal rcBalance = rc.getBalance();
        BigDecimal zero = new BigDecimal("0");

        if (overdueBalance.compareTo(zero) == 1) {
            //balance is positive, they owe money
            overdueBalance = overdueBalance.subtract(payment.getAmountPaid());
            if(overdueBalance.compareTo(zero) == -1) {
                //they overpaid and have money to put towards rent
                payment.setAmountPaid(overdueBalance.abs());
                overdueBalance = zero;
            } else {
                //they underpaid or paid overdue balance exactly
                payment.setAmountPaid(zero);
            }
        } else if(overdueBalance.compareTo(zero) == -1) {
            //balance is negative, they have a credit
            payment.setAmountPaid(payment.getAmountPaid().add(overdueBalance.abs()));
            overdueBalance = zero;
        }
        //Rent Cycle Balance Check
        if (rc.getBalance().compareTo(zero) == 1) {
             //balance is positive, they owe money  
            rcBalance = rc.getBalance().subtract(payment.getAmountPaid());
            if(rcBalance.compareTo(zero) == -1) {
                //they overpaid and have money to put towards credit
                rc.setRentStatus("Paid");
                updateRentCycle(rc);
                payment.setAmountPaid(rcBalance.abs());
                rcBalance = zero;
            } else {
                //they underpaid or paid rent balance exactly
                if(rcBalance.compareTo(zero) == 0) {
                    rc.setRentStatus("Paid");
                    updateRentCycle(rc);
                }
                payment.setAmountPaid(zero);
            }
        } 
        if (payment.getAmountPaid().compareTo(zero) == 1) {
            overdueBalance = overdueBalance.subtract(payment.getAmountPaid());
        }
        
        String sqlUpdateOverdueBalance = "UPDATE lease SET overdue_balance = ? WHERE lease_id = ?";
        jdbcTemplate.update(sqlUpdateOverdueBalance, overdueBalance, payment.getLeaseId());

        String sqlUpdateRentCycleBalance = "UPDATE rent_cycle SET balance = ? WHERE rent_cycle_id =?";
        jdbcTemplate.update(sqlUpdateRentCycleBalance, rcBalance, rentCycleId);
    }

    @Override //passed test sql is good
    public List<RentCycle> getAllRent() {
        List<RentCycle> allRent = new ArrayList<>();
        String allRentSql = "SELECT rent_cycle_id, lease_id, start_date, balance, due_date, rent_status "
        + "FROM Rent_cycle " 
        + "WHERE rent_status = 'Unpaid' OR rent_status = 'Overdue';";
        SqlRowSet results = jdbcTemplate.queryForRowSet(allRentSql);
        while (results.next()){
           allRent.add(mapRowToRentCycle(results));
        }
        return allRent;
    }

    @Override //passed test sql is good
    public List<Payment> getAllPayments(){
        List<Payment> allPayments = new ArrayList<>();
        String allPaymentsSql = "SELECT payment_id, lease_id, payment.rent_cycle_id, amount_paid, date_paid "
                                + "FROM payment "
                                + "JOIN rent_cycle ON payment.rent_cycle_id = rent_cycle.rent_cycle_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(allPaymentsSql);
        while (results.next()){
            allPayments.add(mapRowToPayment(results));
        }
        return allPayments;
    }

    @Override //passed test sql is good
    public List<Payment> getAllPaymentsByLeaseId(int leaseId){
        List<Payment> allPayments = new ArrayList<>();
        String allPaymentsSql = "SELECT payment_id, rent_cycle.lease_id, payment.rent_cycle_id, amount_paid, date_paid "
                                + "FROM payment "
                                + "JOIN rent_cycle ON payment.rent_cycle_id = rent_cycle.rent_cycle_id "
                                + "WHERE rent_cycle.lease_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(allPaymentsSql, leaseId);
        while (results.next()){
            allPayments.add(mapRowToPayment(results));
        }
        return allPayments;
    }

    @Override //passed test sql is good
    public List<RentCycle> getAllRentCyclesByLeaseId(int leaseId){
        String sql = "SELECT rent_cycle_id, lease_id, start_date, balance, due_date, rent_status "
        + "FROM rent_cycle "
        + "WHERE lease_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, leaseId);

        List<RentCycle> rentCycles = new ArrayList<RentCycle>();
        while(results.next()){
            rentCycles.add(mapRowToRentCycle(results));
        }
        return rentCycles;
    }

    @Override //passed test sql is good
    public RentCycle getCurrentRentByLeaseId(int leaseId){
        String sql = "SELECT rent_cycle_id, rent_cycle.lease_id, start_date, balance, due_date, rent_status "
        + "FROM rent_cycle "
        + "WHERE lease_id = ? AND start_date < ? AND due_date >= ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, leaseId, LocalDate.now(), LocalDate.now());

        RentCycle rentCycle = new RentCycle();
        while(results.next()){
            rentCycle = mapRowToRentCycle(results);
        }
        return rentCycle;
    }

    @Override //passed test sql is good
    public List<Payment> getPaymentsByRentCycleId(int rentCycleId){
        String sql = "SELECT payment_id, rent_cycle_id, amount_paid, date_paid "
        + "FROM payment WHERE rent_cycle_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, rentCycleId);

        List<Payment> payment = new ArrayList<>();
        while(results.next()){
            payment.add(mapRowToPayment(results));
        }
        return payment;
    }

    @Override
    public RentCycle getRentCycleById( int rentCycleId){
        String sql = "SELECT rent_cycle_id, lease_id, start_date, balance, due_date, rent_status "
        + "FROM rent_cycle WHERE rent_cycle_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, rentCycleId);
        RentCycle rentCycle = new RentCycle();
        while(results.next()){
            rentCycle = mapRowToRentCycle(results);
        }
        return rentCycle;
    }
    @Override
    public BigDecimal getOverdueBalanceForLease(int leaseId) {
        String sql = "SELECT overdue_balance "
                    +"FROM lease "
                    +"WHERE lease_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, leaseId);
        
        BigDecimal overdueBalance = null;

        if (result.next()) {
            overdueBalance = result.getBigDecimal("overdue_balance");
        }
        return overdueBalance;
    }

   private Payment mapRowToPayment(SqlRowSet row){
    Payment payment = new Payment();
    payment.setId(row.getInt("payment_id"));
    payment.setLeaseId(row.getInt("lease_id"));
    payment.setRentCycleId(row.getInt("rent_cycle_id"));
    payment.setAmountPaid(row.getBigDecimal("amount_paid"));
    payment.setDatePaid(row.getDate("date_paid").toLocalDate());
        return payment;
   }


    private RentCycle mapRowToRentCycle(SqlRowSet row){
    RentCycle renCy = new RentCycle();
    renCy.setId(row.getInt("rent_cycle_id"));
    renCy.setLeaseId(row.getInt("lease_id"));
    renCy.setStartDate(row.getDate("start_date").toLocalDate());
    renCy.setBalance(row.getBigDecimal("balance"));
    renCy.setDueDate(row.getDate("due_date").toLocalDate());
    renCy.setRentStatus(row.getString("rent_status"));
    return renCy;
}
}