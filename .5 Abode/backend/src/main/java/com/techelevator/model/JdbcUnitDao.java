package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcUnitDao implements UnitDao {

    
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUnitDao(DataSource datasource){
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }
//     @Override
//     public List<Unit> getAllUnits() {
//         String sql = "SELECT unit_id, unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available FROM unit;";
//         SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
//         return;
//     }

    // @Override
    // public List<Unit> getUnitsByProperty(property) {

    //     return null;
    // }

    @Override
    public List<Unit> getUnitByRenter(int userId) {
        List<Unit> unitByRenter = new ArrayList<Unit>();
        String sql = "SELECT unit.unit_id, unit_number, property_id, bed_count, bath_count, price, sq_ft FROM unit LEFT JOIN lease ON lease.unit_id = unit.unit_id WHERE user_id = ?;";
        SqlRowSet unitResults = jdbcTemplate.queryForRowSet(sql, userId);
        while (unitResults.next()) {
            Unit u = new Unit();
            u.setUnitId(unitResults.getInt("unit_id"));
            u.setUnitNumber(unitResults.getString("unit_number"));
            u.setPropertyId(unitResults.getInt("property_id"));
            u.setBedCount(unitResults.getInt("bed_count"));
            u.setBedCount(unitResults.getInt("bath_count"));
            u.setPrice(unitResults.getBigDecimal("price"));
            u.setSqFt(unitResults.getInt("sq_ft"));
            unitByRenter.add(u);
        }
        return unitByRenter;
    }
    @Override
    public List<User> getRenterByUnit(int unitId) {
        List<User> rentersInUnit = new ArrayList<User>();
        String sql = "SELECT user_id, email, first_name, last_name, phone_number FROM user LEFT JOIN lease ON lease.user_id = user.user_id WHERE unit_id = ?;";
        SqlRowSet renterResults = jdbcTemplate.queryForRowSet(sql, unitId);
        while (renterResults.next()) {
            User r = new User();
            r.setId(renterResults.getLong("user_id"));
            r.setEmail(renterResults.getString("email"));
            r.setFirstName(renterResults.getString("first_name"));
            r.setLastName(renterResults.getString("last_name"));
            r.setPhoneNumber(renterResults.getString("phone_number"));
            rentersInUnit.add(r);
        }
        return rentersInUnit;
    }
}
//