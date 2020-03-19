package com.techelevator.npgeek.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCParkDao implements ParkDao{
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JDBCParkDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Park> getAllParks() {
		
		List<Park> allParks = new ArrayList<Park>();
		String sql = "SELECT parkcode, parkname, parkdescription FROM park ORDER BY parkname;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			Park currentPark = new Park();
			currentPark.setParkCode(results.getString("parkcode"));
			currentPark.setParkName(results.getString("parkname"));
			currentPark.setDescription(results.getString("parkdescription"));
			
			allParks.add(currentPark);
		}
		
		return allParks;
	}
	@Override
	public List<String> getAllParkCodes() {
		List<String> codes = new ArrayList<>();
		String sql = "SELECT parkcode FROM park ORDER BY parkname;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		while(results.next()) {
			String currentCode = results.getString("parkcode");
			codes.add(currentCode);
		}
		
		return codes;
	}
	@Override
	public Park getParkByCode(String code) {
		Park park = new Park();
		String sql = "SELECT parkname, state, acreage, elevationinfeet, milesoftrail, numberofcampsites, climate, yearfounded, " +
					 "annualvisitorcount, inspirationalquote, inspirationalquotesource, parkdescription, entryfee, numberofanimalspecies, " +
					 "location " +
					 "FROM park WHERE parkcode = ?;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, code);
		if(results.next()) {
			park.setParkCode(code);
			park.setParkName(results.getString("parkname"));
			park.setState(results.getString("state"));
			park.setAcreage(results.getInt("acreage"));
			park.setElevationInFeet(results.getInt("elevationinfeet"));
			park.setMilesOfTrail(results.getFloat("milesoftrail"));
			park.setNumberOfCampsites(results.getInt("numberofcampsites"));
			park.setClimate(results.getString("climate"));
			park.setYearFounded(results.getInt("yearfounded"));
			park.setAnnualVisitors(results.getInt("annualvisitorcount"));
			park.setInspirationalQuote(results.getString("inspirationalquote"));
			park.setInspQuoteSource(results.getString("inspirationalquotesource"));
			park.setDescription(results.getString("parkdescription"));
			park.setEntryFee(results.getInt("entryfee"));
			park.setNumberOfAnimalSpecies(results.getInt("numberofanimalspecies"));
			park.setLocation(results.getString("location"));
		}
		
		return park;
	}
	
	@Override
	public void submitSurvey(Survey survey) {
		String sql = "INSERT INTO survey_result (parkcode, emailaddress, state, activitylevel) "+
					 "VALUES (?, ?, ?, ?);";
		jdbcTemplate.update(sql, survey.getFavPark(), survey.getEmail(), survey.getStateRes(), survey.getActivityLvl());
	}
	
	@Override
	public Map<Park, Integer> getSurveyResults(){
		Map <Park, Integer> resultsMap = new LinkedHashMap<>();
		String sql = "SELECT COUNT(parkcode) AS parkCount, parkcode  FROM survey_result GROUP BY parkcode ORDER BY parkCount DESC, parkcode;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		boolean isFirst = true;
		while (results.next()) {
			Park currentPark = getParkByCode(results.getString("parkcode"));
			if (isFirst) {
				currentPark.setLeader(true);
				isFirst = false;
			}
			resultsMap.put(currentPark, results.getInt("parkCount"));
		} return resultsMap;
	}

}