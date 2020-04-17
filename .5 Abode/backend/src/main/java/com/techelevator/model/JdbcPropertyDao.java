package com.techelevator.model;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JdbcPropertyDao implements PropertyDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcPropertyDao(DataSource datasource){
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public List<Property> getAllProperties() {
        List<Property> allProperties = new ArrayList<>();
        String propertySearchSql = "SELECT * FROM property;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(propertySearchSql);
        while (results.next()){
            Property prop = new Property();
            prop.setPropertyId(results.getInt("property_id"));
            prop.setLandlordId(results.getInt("landlord_id"));
            prop.setStreetAddress(results.getString("street_address"));
            prop.setCity(results.getString("city"));
            prop.setState(results.getString("state"));
            prop.setZipCode(results.getString("zip_code"));
            prop.setPropertyName(results.getString("property_name"));
            prop.setPhotoPath(results.getString("photo_path"));
            prop.setLocation(results.getString("location"));

            String unitSearchSql = "SELECT unit_id, unit_number, bed_count, bath_count, price, sq_ft, is_available " + 
                                "FROM unit WHERE property_id = ?;";
            SqlRowSet unitResults = jdbcTemplate.queryForRowSet(unitSearchSql, prop.getPropertyId());
            List<Unit> unitList = new ArrayList<>();
            while (unitResults.next()) {
                Unit currentUnit = new Unit();
                currentUnit.setUnitId(unitResults.getInt("unit_id"));
                currentUnit.setUnitNumber(unitResults.getString("unit_number"));
                currentUnit.setBedCount(unitResults.getInt("bed_count"));
                currentUnit.setBathCount(unitResults.getInt("bath_count"));
                currentUnit.setPrice(unitResults.getBigDecimal("price"));
                currentUnit.setSqFt(unitResults.getInt("sq_ft"));
                currentUnit.setAvailable(unitResults.getBoolean("is_available"));
                unitList.add(currentUnit);
            }
            prop.setUnits(unitList);
            prop.setFeatures(getFeaturesByPropertyId(prop.getPropertyId()));
            allProperties.add(prop);
        }
        return allProperties;
    }

    @Override
    public List<Property> getPropertiesByLandlord(int landlordId) {
        List<Property> portfolio = new ArrayList<>();
        String propertySearchSql = "SELECT * FROM property WHERE landlord_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(propertySearchSql, landlordId);
        while (results.next()){
            Property prop = new Property();
            prop.setPropertyId(results.getInt("property_id"));
            prop.setLandlordId(landlordId);
            prop.setStreetAddress(results.getString("street_address"));
            prop.setCity(results.getString("city"));
            prop.setState(results.getString("state"));
            prop.setZipCode(results.getString("zip_code"));
            prop.setPropertyName(results.getString("property_name"));
            prop.setPhotoPath(results.getString("photo_path"));
            prop.setLocation(results.getString("location"));

            String unitSearchSql = "SELECT unit_id, unit_number, bed_count, bath_count, price, sq_ft, is_available " + 
                                "FROM unit WHERE property_id = ?;";
            SqlRowSet unitResults = jdbcTemplate.queryForRowSet(unitSearchSql, prop.getPropertyId());
            List<Unit> unitList = new ArrayList<>();
            while (unitResults.next()) {
                Unit currentUnit = new Unit();
                currentUnit.setUnitId(unitResults.getInt("unit_id"));
                currentUnit.setUnitNumber(unitResults.getString("unit_number"));
                currentUnit.setBedCount(unitResults.getInt("bed_count"));
                currentUnit.setBathCount(unitResults.getInt("bath_count"));
                currentUnit.setPrice(unitResults.getBigDecimal("price"));
                currentUnit.setSqFt(unitResults.getInt("sq_ft"));
                currentUnit.setAvailable(unitResults.getBoolean("is_available"));
                unitList.add(currentUnit);
            }
            prop.setUnits(unitList);
            portfolio.add(prop);
        }
        return portfolio;
    }

    @Override
    public Property getPropertyById(int id) {
        String propertySearchSql = "SELECT * FROM property WHERE property_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(propertySearchSql, id);
        Property prop = new Property();
        if (results.next()){
            prop.setPropertyId(results.getInt("property_id"));
            prop.setLandlordId(results.getInt("landlord_id"));
            prop.setStreetAddress(results.getString("street_address"));
            prop.setCity(results.getString("city"));
            prop.setState(results.getString("state"));
            prop.setZipCode(results.getString("zip_code"));
            prop.setPropertyName(results.getString("property_name"));
            prop.setPhotoPath(results.getString("photo_path"));
            prop.setLocation(results.getString("location"));
        }
        return prop;
    }

    @Override
    public boolean addNewProperty(Property property) {
        String sql = "INSERT INTO property (landlord_id, street_address, city, state, zip_code, property_name, photo_path, location) "+
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING property_id;";
        Integer propertyId = jdbcTemplate.queryForObject(sql, Integer.class, property.getLandlordId(), property.getStreetAddress(), property.getCity(),
                                    property.getStreetAddress(), property.getZipCode(), property.getPropertyName(), property.getPhotoPath(), property.getLocation());
        addUnitsByProperty(propertyId, property.getUnits());
        addFeaturesPropertyLinks(propertyId, property.getFeatures());
        return propertyId != null;
    }
    @Override
    public void addUnitsByProperty(int propertyId, List<Unit> units) {
        String sql = "INSERT INTO unit (unit_number, property_id, bed_count, bath_count, price, sq_ft, is_available) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?);";
        for(Unit u : units) {             
            jdbcTemplate.update(sql, u.getUnitNumber(), propertyId, u.getBedCount(), u.getBathCount(), u.getPrice(), u.getSqFt(), u.isAvailable());
        }
    }
    @Override
    public void addFeaturesPropertyLinks(int propertyId, List<Feature> features) {
        String sql = "INSERT INTO property_feature (property_id, feature_id) " +
                     "VALUES (?,?);";
        for(Feature f : features) {
            jdbcTemplate.update(sql, propertyId, f.getFeatureId());
        }
    }

    @Override
    public boolean updateExistingProperty(Property property) {
        String sql = "UPDATE property " +
                     "SET street_address = ?, city = ?, state = ?, zip_code = ?, "+
                     "property_name = ?, photo_path = ?, location = ? " +
                     "WHERE property_id = ?;";
        int rows = jdbcTemplate.update(sql, property.getStreetAddress(), property.getCity(), property.getState(),
                            property.getZipCode(), property.getPropertyName(), property.getPhotoPath(), property.getLocation(),
                            property.getPropertyId());
        sql = "DELETE FROM unit WHERE property_id = ? AND is_available = true;";
        jdbcTemplate.update(sql, property.getPropertyId());
        addUnitsByProperty(property.getPropertyId(), property.getUnits());

        return rows > 0;
    }
    @Override
    public List<Feature> getFeaturesByPropertyId (int propertyId){
       List<Feature> features = new ArrayList<>();
       String featureSql = "SELECT * FROM feature "
       + "JOIN property_feature ON property_feature.feature_id = feature.feature_id "
       + "JOIN property ON property_feature.property_id = property.property_id WHERE property.property_id = ?;";
       SqlRowSet results = jdbcTemplate.queryForRowSet(featureSql, propertyId);
       while (results.next()){
        Feature feat = new Feature();
            feat.setFeatureId(results.getInt("feature_id"));
           feat.setFeatureName(results.getString("feature_name"));
           features.add(feat);
        } return features;
    }

    @Override
    public List<Feature> getAllFeatures() {
        String sql = "SELECT feature_id, feature_name FROM feature;";
        List<Feature> features = new ArrayList<>();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            Feature f = new Feature();
            f.setFeatureId(results.getInt("feature_id"));
            f.setFeatureName(results.getString("feature_name"));
            features.add(f);
        }
        return features;
    }
}
