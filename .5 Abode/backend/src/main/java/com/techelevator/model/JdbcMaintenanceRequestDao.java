package com.techelevator.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcMaintenanceRequestDao implements MaintenanceRequestDao {
    
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMaintenanceRequestDao(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public List<MaintenanceRequest> getAllUncompletedRequests() {
        List<MaintenanceRequest> allRequests = new ArrayList<>();
        String requestSearchSql = "SELECT request_id, maintenance_request.unit_id, request_user_id, CONCAT(users.first_name, ' ', users.last_name) AS full_name, users.email, request_desc, priority, date_requested, assigned_user_id, is_completed, unit_number, property_name, street_address, city, state, property.zip_code "
        + "FROM maintenance_request "
        + "JOIN unit ON maintenance_request.unit_id = unit.unit_id "
        + "JOIN property ON unit.property_id = property.property_id "
        + "JOIN users ON maintenance_request.request_user_id = users.user_id "
        + "WHERE is_completed = false ORDER BY request_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(requestSearchSql);
        while (results.next()) {
            allRequests.add(mapRowToRequest(results));
        }
        return allRequests;
    }

    @Override
    public void createRequest(MaintenanceRequest request) {
        String sqlInsert = "INSERT INTO maintenance_request (unit_id, request_user_id, request_desc, priority, date_requested) "
                + "VALUES (?,?,?,?,?);";
        jdbcTemplate.update(sqlInsert, request.getUnitId(), request.getRequestUserId(), request.getRequestDesc(), 
        request.getPriority(), LocalDateTime.now());
    }

    @Override
    public void updateRequest(MaintenanceRequest request){
        String sqlUpdate = "UPDATE maintenance_request SET assigned_user_id = ?, priority = ?, is_completed = ? " 
        + "JOIN unit ON maintenance_request.unit_id = unit.unit_id "
        + "JOIN property ON unit.property_id = property.property_id "
        + "WHERE request_id = ?;";
        jdbcTemplate.update(sqlUpdate, request.getAssignedUserId(), request.getPriority(), request.isCompleted(), request.getRequestId());
    }



    @Override
    public List<MaintenanceRequest> getAllCompletedRequestsByEmployeeId(int employeeId){
        String sql = "SELECT request_id, unit.unit_id, request_user_id, CONCAT(users.first_name, ' ', users.last_name) AS full_name, users.email, request_desc, priority, date_requested, assigned_user_id, is_completed, unit_number, property_name, street_address, city, state, property.zip_code "
        + "FROM maintenance_request "
        + "JOIN unit ON maintenance_request.unit_id = unit.unit_id "
        + "JOIN property ON unit.property_id = property.property_id "
        + "JOIN users ON maintenance_request.request_user_id = users.user_id "
        + "WHERE assigned_user_id = ? AND is_completed = true "
        + "ORDER BY request_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, employeeId);
        List<MaintenanceRequest> requests = new ArrayList<>();
        while (results.next()) {
            requests.add(mapRowToRequest(results));
        }
        return requests;
    }

    @Override
    public List<MaintenanceRequest> getAllUncompletedRequestsByEmployeeId(int employeeId){
        String sql = "SELECT request_id,  maintenance_request.unit_id, request_user_id, CONCAT(users.first_name, ' ', users.last_name) AS full_name, users.email, request_desc, priority, date_requested, assigned_user_id, is_completed, unit_number, property_name, street_address, city, state, property.zip_code "
        + "FROM maintenance_request "
        + "JOIN unit ON maintenance_request.unit_id = unit.unit_id "
        + "JOIN property ON unit.property_id = property.property_id "
        + "JOIN users ON maintenance_request.request_user_id = users.user_id "
        + "WHERE assigned_user_id = ? AND is_completed = false "
        + "ORDER BY request_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, employeeId);
        List<MaintenanceRequest> requests = new ArrayList<>();
        while (results.next()) {
            requests.add(mapRowToRequest(results));
        }
        return requests;
    }

    @Override
    public void updateRequestMaint(MaintenanceRequest request) {
        String sqlUpdate = "UPDATE maintenance_request SET priority = ?, is_completed = ?, assigned_user_id = ? " +
        "WHERE request_id = ?;";
        jdbcTemplate.update(sqlUpdate, request.getPriority(), request.isCompleted(), request.getAssignedUserId(), request.getRequestId());
    }

    private MaintenanceRequest mapRowToRequest(SqlRowSet row){
        MaintenanceRequest req = new MaintenanceRequest();
        req.setRequestId(row.getInt("request_id"));
            req.setUnitId(row.getInt("unit_id"));
            req.setRequestUserId(row.getInt("request_user_id"));
            req.setRequesterFullName(row.getString("full_name"));
            req.setRequesterEmail(row.getString("email"));
            req.setRequestDesc(row.getString("request_desc"));
            req.setPriority(row.getInt("priority"));
            req.setDateRequested(row.getDate("date_requested").toLocalDate());
            req.setAssignedUserId(row.getInt("assigned_user_id"));
            req.setCompleted(row.getBoolean("is_completed"));
            req.setUnitNumber(row.getString("unit_number"));
            req.setPropertyName(row.getString("property_name"));
            req.setStreetAddress(row.getString("street_address"));
            req.setCity(row.getString("city"));
            req.setState(row.getString("state"));
            req.setZipCode(row.getString("zip_code"));
            return req;
    }
}