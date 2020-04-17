package com.techelevator.model;

import java.util.List;

public interface PropertyDao {
    
public List<Property> getAllProperties();
public List<Property> getPropertiesByLandlord(int landlordId);
public Property getPropertyById(int id);
public boolean addNewProperty(Property property);
public boolean updateExistingProperty(Property property);
public void addUnitsByProperty(int propertyId, List<Unit> units);
public void addFeaturesPropertyLinks(int propertyId, List<Feature> features);
public List<Feature> getFeaturesByPropertyId (int id);
public List<Feature> getAllFeatures();

}