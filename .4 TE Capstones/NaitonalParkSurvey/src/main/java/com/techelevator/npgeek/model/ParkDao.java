package com.techelevator.npgeek.model;

import java.util.List;
import java.util.Map;

public interface ParkDao {
	
	public List<Park> getAllParks();
	public List<String> getAllParkCodes();
	public Park getParkByCode(String code);
//	public List<Weather> getAllWeatherByPark(String code);
	public void submitSurvey(Survey survey);
	public Map<Park, Integer> getSurveyResults();
}
