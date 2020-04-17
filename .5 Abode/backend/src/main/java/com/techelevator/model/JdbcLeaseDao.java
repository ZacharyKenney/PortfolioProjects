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
class JdbcLeaseDao implements LeaseDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLeaseDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private Lease mapRowToLease (SqlRowSet row) {
        Lease newLease = new Lease();

        newLease.setLeaseId(row.getInt("lease_id"));
        newLease.setUserId(row.getInt("user_id"));
        newLease.setUnitId(row.getInt("unit_id"));
        newLease.setSignedDate(row.getDate("signed_date").toLocalDate());
        newLease.setStartDate(row.getDate("start_date").toLocalDate());
        newLease.setRentLength(row.getInt("rent_length"));
        newLease.setRentAmount(row.getBigDecimal("rent_amount"));
        newLease.setLateFee(row.getBigDecimal("late_fee"));
        newLease.setStatus(row.getString("status"));
        newLease.setPropertyName(row.getString("property_name"));
        newLease.setPropertyAddress(row.getString("street_address"));;
        newLease.setUnitNumber(row.getString("unit_number"));;
        newLease.setRenterName(row.getString("first_name") + " " + row.getString("last_name"));;
        newLease.setRenterEmail(row.getString("email"));;
        newLease.setOverdueBalance(row.getBigDecimal("overdue_balance"));

        String sql = "SELECT rent_status FROM rent_cycle WHERE lease_id = ? AND start_date < ? AND due_date >= ?;";

        SqlRowSet row1 = jdbcTemplate.queryForRowSet(sql, row.getInt("lease_id"), LocalDate.now(), LocalDate.now());
        if(row1.next()) {
            newLease.setCurrentRentStatus(row1.getString("rent_status"));
        }
        return newLease;
    }

    @Override
    public List<Lease> getAllLeases() {
        String sql = "SELECT lease_id, signed_date, start_date, rent_length, rent_amount, late_fee, overdue_balance, status, "
                    + "lease.user_id, users.email, users.first_name, users.last_name, "
                    + "lease.unit_id, unit.unit_number, property.property_name, property.street_address "
                    + "FROM lease "
                    + "JOIN users ON lease.user_id = users.user_id "
                    + "JOIN unit ON lease.unit_id = unit.unit_id "
                    + "JOIN property ON unit.property_id = property.property_id;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);
        
        List<Lease> allLeases = new ArrayList<Lease>();
        while (rows.next()) {
            allLeases.add(mapRowToLease(rows));
        }
        return allLeases;
    }

    @Override
    public List<Lease> getLeasesForLandlord(int landlordUserId) {
        String sql = "SELECT lease_id, signed_date, start_date, rent_length, rent_amount, late_fee, overdue_balance, status, "
                    + "lease.user_id, users.email, users.first_name, users.last_name, "
                    + "lease.unit_id, unit.unit_number, property.property_name, property.street_address "
                    + "FROM lease "
                    + "JOIN users ON lease.user_id = users.user_id "
                    + "JOIN unit ON lease.unit_id = unit.unit_id "
                    + "JOIN property ON unit.property_id = property.property_id "
                    + "WHERE property.landlord_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, landlordUserId);
        
        List<Lease> landlordLeases = new ArrayList<Lease>();
        while (rows.next()) {
            landlordLeases.add(mapRowToLease(rows));
            updateRentCycles(rows.getInt("lease_id"), rows.getBigDecimal("overdue_balance"), rows.getBigDecimal("late_fee"));
        }
        return landlordLeases;
    }
    
    @Override
    public List<Lease> getLeasesForRenter(int renterUserId) {
        String sql = "SELECT lease_id, signed_date, start_date, rent_length, rent_amount, late_fee, overdue_balance, status, "
                    + "lease.user_id, users.email, users.first_name, users.last_name, "
                    + "lease.unit_id, unit.unit_number, property.property_name, property.street_address "
                    + "FROM lease "
                    + "JOIN users ON lease.user_id = users.user_id "
                    + "JOIN unit ON lease.unit_id = unit.unit_id "
                    + "JOIN property ON unit.property_id = property.property_id "
                    + "WHERE users.user_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, renterUserId);
        
        List<Lease> renterLeases = new ArrayList<Lease>();
        while (rows.next()) {
            renterLeases.add(mapRowToLease(rows));
        }
        return renterLeases;
    }

    @Override
    public Lease getCurrentLeaseForRenter(int renterUserId) {
        String sql = "SELECT lease_id, signed_date, start_date, rent_length, rent_amount, late_fee, overdue_balance, status, "
                    + "lease.user_id, users.email, users.first_name, users.last_name, "
                    + "lease.unit_id, unit.unit_number, property.property_name, property.street_address "
                    + "FROM lease "
                    + "JOIN users ON lease.user_id = users.user_id "
                    + "JOIN unit ON lease.unit_id = unit.unit_id "
                    + "JOIN property ON unit.property_id = property.property_id "
                    + "WHERE users.user_id = ? AND lease.status = 'Active';";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, renterUserId);
        
        Lease currentLease = new Lease();
        while (rows.next()) {
            currentLease = mapRowToLease(rows);
        }
        return currentLease;
    }

    @Override
    public Lease getLeaseById(int leaseId) {
        String sql = "SELECT lease_id, signed_date, start_date, rent_length, rent_amount, late_fee, overdue_balance, status, "
                    + "lease.user_id, users.email, users.first_name, users.last_name, "
                    + "lease.unit_id, unit.unit_number, property.property_name, property.street_address "
                    + "FROM lease "
                    + "JOIN users ON lease.user_id = users.user_id "
                    + "JOIN unit ON lease.unit_id = unit.unit_id "
                    + "JOIN property ON unit.property_id = property.property_id "
                    + "WHERE lease_id = ?;";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, leaseId);

        Lease newLease = new Lease();
        while (rows.next()) {
            newLease = mapRowToLease(rows);
        }
        return newLease;
    }
    
    @Override
    public void createLease(Lease newLease) {
        String sql = "INSERT INTO lease (user_id, unit_id, signed_date, start_date, rent_length, rent_amount, late_fee, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING lease_id;";
        int leaseId = jdbcTemplate.queryForObject(sql, Integer.class, newLease.getUserId(), 
                                    newLease.getUnitId(), 
                                    newLease.getSignedDate(),
                                    newLease.getStartDate(),
                                    newLease.getRentLength(),
                                    newLease.getRentAmount(),
                                    newLease.getLateFee(),
                                    newLease.getStatus());
        sql = "INSERT INTO rent_cycle (lease_id, start_date, balance, due_date, rent_status) "+
              "VALUES (?, ?, ?, ?, ?);";
        LocalDate startDate = newLease.getStartDate();
        for (int i = 0; i < newLease.getRentLength(); i++) {
            jdbcTemplate.update(sql, leaseId, startDate, newLease.getRentAmount(), startDate.plusMonths(1), "Unpaid");
            startDate = startDate.plusMonths(1);
        }
        // Update unit 'is_available' to false
        sql = "UPDATE unit SET is_available = false WHERE unit_id = ?;";
        jdbcTemplate.update(sql, newLease.getUnitId());
    }
    
    @Override
    public void updateLease(Lease lease) {
        String sql = "UPDATE lease SET user_id = ?, unit_id = ?, signed_date = ?, rent_length = ?, rent_amount = ?, late_fee = ?, status = ? "
                    + "WHERE lease_id = ?;";
        jdbcTemplate.update(sql, lease.getUserId(), 
                                lease.getUnitId(), 
                                lease.getSignedDate(),
                                lease.getRentLength(),
                                lease.getRentAmount(),
                                lease.getLateFee(),
                                lease.getStatus(),
                                lease.getLeaseId());
        // if Terminated, ensure unit 'is_available' is set to true
        if (lease.getStatus().equals("Terminated")) {
            sql = "UPDATE unit SET is_available = true WHERE unit_id = ?;";
            jdbcTemplate.update(sql, lease.getUnitId());
        }
    }

    @Override
    public void updateRentCycles(int leaseId, BigDecimal overdueBalance, BigDecimal lateFee) {
        String sqlRentCycles = "SELECT rent_cycle_id, rent_status, balance, due_date FROM rent_cycle " +
                               "WHERE lease_id = ?;";
        String sqlOverdueUpdate1 = "UPDATE rent_cycle SET rent_status = 'Overdue' " +
                                   "WHERE rent_cycle_id = ? AND due_date < ? RETURNING balance;";
        String sqlOverdueUpdate2 = "UPDATE lease SET overdue_balance = ? "+
                                "WHERE lease_id = ?;";

        SqlRowSet rows = jdbcTemplate.queryForRowSet(sqlRentCycles, leaseId);
        while (rows.next()) {
            if (rows.getDate("due_date").toLocalDate().compareTo(LocalDate.now()) == -1) {
                if (rows.getString("rent_status").equals("Overdue") == false && rows.getBigDecimal("balance").compareTo(new BigDecimal("0")) == 1) {
                    BigDecimal balance = jdbcTemplate.queryForObject(sqlOverdueUpdate1, BigDecimal.class, rows.getInt("rent_cycle_id"), LocalDate.now());
                    balance = balance.add(balance.multiply(lateFee.divide(new BigDecimal("100"))));
                    jdbcTemplate.update(sqlOverdueUpdate2, overdueBalance.add(balance), leaseId);
                    overdueBalance = overdueBalance.add(balance);
                }
            }
        }                           
    }

}