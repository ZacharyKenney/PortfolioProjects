package com.techelevator.npgeek.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestWeatherDao implements WeatherDao {

// High Level API Data Holder >>> get "daily" key
	private static class DarkSkyForecast {
		public DarkSkyDataBlock daily;
	}
// Mid Level API Data Holder >>> get "data" key
	private static class DarkSkyDataBlock {
		public List<DarkSkyDataPoint> data;
	}
// Final Level API Data Holder >>> get specified data points
	private static class DarkSkyDataPoint {
		public double temperatureHigh;
		public double temperatureLow;
		public String icon;	
	}
// API Path + User specific key
	private static final String BASE_URL = "https://api.darksky.net/forecast/";
	private static final String USER_KEY = "939a9e0747fd4639234bf8e2ce1f3dc1"; //remove before github post
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public List<Weather> getForecastByLocaiton(String location) {
		
		String url = BASE_URL + USER_KEY + "/" + location;
		DarkSkyForecast response = restTemplate.getForObject(url, DarkSkyForecast.class);
		List<Weather> forecast = new ArrayList<>();
		int dayCounter = 0;
		for (DarkSkyDataPoint d : response.daily.data) {
			Weather weather = new Weather();
			weather.setHighTemp(d.temperatureHigh);
			weather.setLowTemp(d.temperatureLow);
			weather.setIcon(d.icon);
			weather.setDate(LocalDate.now().plusDays(dayCounter));
			forecast.add(weather);
			dayCounter++;

		}
		return forecast;
	}
}
