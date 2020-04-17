package com.techelevator.model;

import java.util.List;

public interface MaintenanceRequestDao {

public List<MaintenanceRequest> getAllUncompletedRequests();
public void createRequest (MaintenanceRequest request);
public void updateRequest(MaintenanceRequest request);
public void updateRequestMaint(MaintenanceRequest request);

public List<MaintenanceRequest> getAllCompletedRequestsByEmployeeId(int employeeId);
public List<MaintenanceRequest> getAllUncompletedRequestsByEmployeeId(int employeeId);
}