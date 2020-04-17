package com.techelevator.controller;

import java.util.List;

import com.techelevator.authentication.AuthProvider;
import com.techelevator.authentication.UnauthorizedException;
import com.techelevator.model.Feature;
import com.techelevator.model.Lease;
import com.techelevator.model.LeaseDao;
import com.techelevator.model.MaintenanceRequest;
import com.techelevator.model.MaintenanceRequestDao;
import com.techelevator.model.Payment;
import com.techelevator.model.Property;
import com.techelevator.model.PropertyDao;
import com.techelevator.model.RentCycle;
import com.techelevator.model.RentDao;
import com.techelevator.model.Unit;
import com.techelevator.model.UnitDao;
import com.techelevator.model.User;
import com.techelevator.model.UserDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

/**
 * ApiController
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private AuthProvider authProvider;

    @Autowired
    private LeaseDao leaseDao;

    @Autowired
    private PropertyDao propertyDao;

    @Autowired
    private UnitDao unitDao;

    @Autowired
    private RentDao rentDao;

    @Autowired
    private MaintenanceRequestDao maintReqDao;

    @Autowired
	private UserDao userDao;
    
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String authorizedOnly() throws UnauthorizedException {
        /*
        You can lock down which roles are allowed by checking
        if the current user has a role.
        
        In this example, if the user does not have the admin or user role
        we send back an unauthorized error.
        */
        if (!authProvider.userHasRole(new String[] { "admin", "renter", "maintenance", "landlord" })) {
            throw new UnauthorizedException();
        }
        return "Success";
    }
    /**************** USER CONTROLLER METHODS ****************/
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    @GetMapping("/users/renters")
    public List<User> getAllRenters() {
        return userDao.getAllRenters();
    }
    @GetMapping("/users/maintenance")
    public List<User> getAllMaintenanceUsers(){
        return userDao.getAllMaintenancUsers();
    }
    /**************** USER CONTROLLER METHODS ****************/


    /**************** PROPERTY CONTROLLER METHODS ****************/
    @GetMapping("/properties")
    public List<Property> getAllProperties() {
        return propertyDao.getAllProperties();
    }
    @GetMapping("/properties/landlord/{landlordId}")
    public List<Property> getLandlordsProperties(@PathVariable int landlordId) {
        return propertyDao.getPropertiesByLandlord(landlordId);
    }
    @PostMapping("/properties/new")
    public boolean addNewProperty(@RequestBody Property newProperty) {
        return propertyDao.addNewProperty(newProperty);
    }
    @PutMapping("/properties/update")
    public boolean displayUpdateExistingProperty(@RequestBody Property updatedProperty) {
        return propertyDao.updateExistingProperty(updatedProperty);
    }
    @GetMapping("/property/{propertyId}")
    public Property getPropertyById(@PathVariable int propertyId) {
        return propertyDao.getPropertyById(propertyId);
    }
    @GetMapping("public/features/{propertyId}")
    public List<Feature> getFeaturesByPropertyId(@PathVariable int propertyId){
        return propertyDao.getFeaturesByPropertyId(propertyId);
    }
    @GetMapping("/features")
    public List<Feature> getAllfeatures() {
        return propertyDao.getAllFeatures();
    }

    /**************** PROPERTY CONTROLLER METHODS ****************/


    /**************** UNIT CONTROLLER METHODS ****************/
    @GetMapping("/unit/renter/{renterId}")
    public List<Unit> getUnitByRenter(@PathVariable int renterId) {
        return unitDao.getUnitByRenter(renterId);
    }
    @GetMapping("/renter/unit/{unitId}")
    public List<User> getRenterByUnit(@PathVariable int unitId) {
        return unitDao.getRenterByUnit(unitId);
    }
    /**************** UNIT CONTROLLER METHODS ****************/

    

    /**************** LEASE CONTROLLER METHODS ****************/

    @GetMapping("/leases")
    public List<Lease> getAllLeases() {
        return leaseDao.getAllLeases();
    }
    @GetMapping("/leases/landlord/{landlordUserId}")
    public List<Lease> getLeasesForLandlord(@PathVariable int landlordUserId) {
        return leaseDao.getLeasesForLandlord(landlordUserId);
    }
    @GetMapping("/leases/renter/{renterUserId}")
    public List<Lease> getLeasesForRenter(@PathVariable int renterUserId) {
        return leaseDao.getLeasesForRenter(renterUserId);
    }
    @GetMapping("/leases/active/renter/{renterUserId}")
    public Lease getCurrentLeaseForRenter(@PathVariable int renterUserId) {
        return leaseDao.getCurrentLeaseForRenter(renterUserId);
    }
    @GetMapping("/leases/{leaseId}")
    public Lease getLeaseById(@PathVariable int leaseId) {
        return leaseDao.getLeaseById(leaseId);
    }

    @PostMapping("/leases")
    @ResponseStatus(HttpStatus.CREATED)
    public void createNewLease(@RequestBody Lease newLease) {
        leaseDao.createLease(newLease);
    }
    @PutMapping("/leases/{leaseId}")
    public void updateLease(@PathVariable int leaseId, @RequestBody Lease updatedLease) {
        if (leaseDao.getLeaseById(leaseId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        updatedLease.setLeaseId(leaseId);
        leaseDao.updateLease(updatedLease);
    }

    /**************** LEASE CONTROLLER METHODS ****************/    


    /*******  RENT CONTROLLER METHODS ************/

   
    @GetMapping("/rent")
    public List<RentCycle> getAllRent(){
        return rentDao.getAllRent();
    }

   
    @GetMapping("/payments")
    public List<Payment> getAllPayments(){
        return rentDao.getAllPayments();
    }

    
    @PostMapping("/rent")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRentCycle(@RequestBody RentCycle rentCycle){
        rentDao.createRentCycle(rentCycle);
    }

    
    @PostMapping("/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPayment(@RequestBody Payment payment){
        rentDao.createPayment(payment);
    }

    
    @PutMapping("/rent/{rentCycleId}")
    public void updateRentCycle(@PathVariable int rentCycleId, @RequestBody RentCycle rentCycle){
        if(rentDao.getRentCycleById(rentCycleId) == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        rentCycle.setId(rentCycleId);
        rentDao.updateRentCycle(rentCycle);
    }

    @GetMapping("/payments/lease/{leaseId}")
    public List<Payment> getAllPaymentsByLeaseId(@PathVariable int leaseId){
        return rentDao.getAllPaymentsByLeaseId(leaseId);
    }
     
    @GetMapping("/rent/lease/{leaseId}")
    public List<RentCycle> getAllRentCyclesByLeaseId(@PathVariable int leaseId){
        return rentDao.getAllRentCyclesByLeaseId(leaseId);
    }
     
    @GetMapping("/rent/currentlease/{leaseId}")
    public RentCycle gerCurrentRentByLeaseId(@PathVariable int leaseId){
        return rentDao.getCurrentRentByLeaseId(leaseId);
    }

   
    @GetMapping("/payments/{rentCycleId}")
    public List<Payment> getPaymentsByRentCycleId(@PathVariable int rentCycleId){
        return rentDao.getPaymentsByRentCycleId(rentCycleId);
    }


    /******** RENT CONTROLLER METHODS **********/


    /******** MAINTENANCE CONTROLLER METHODS **********/

    //works in postman
    @PostMapping("/maintenance/request")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRequest (@RequestBody MaintenanceRequest request){
        maintReqDao.createRequest(request);
    }
    //works in postman
    @PutMapping("/Landlord/assignMaintenance")
    public void updateRequest(@RequestBody MaintenanceRequest request){
        maintReqDao.updateRequestMaint(request);
    }
    @PutMapping("/maintenance/requests")
    public void updateRequestMaint(@RequestBody MaintenanceRequest request){
        maintReqDao.updateRequestMaint(request);
    }
    //works in postman
    @GetMapping("/Landlord/assignMaintenance")
    public List<MaintenanceRequest> getAllRequests(){
        return maintReqDao.getAllUncompletedRequests();
    }

    @GetMapping("/maintenance/completed/{employeeId}")
    public List<MaintenanceRequest> getAllRequestsByEmployeeId(@PathVariable int employeeId){
        return maintReqDao.getAllCompletedRequestsByEmployeeId(employeeId);
    }

    @GetMapping("/maintenance/uncompleted/{employeeId}")
    public List<MaintenanceRequest> getAllUncompletedRequestsByEmployeeId(@PathVariable int employeeId){
        return maintReqDao.getAllUncompletedRequestsByEmployeeId(employeeId);
    }
   

}