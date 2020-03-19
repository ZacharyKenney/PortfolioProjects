package com.techelevator.npgeek.controller;



import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techelevator.npgeek.model.Park;
import com.techelevator.npgeek.model.ParkDao;
import com.techelevator.npgeek.model.Weather;
import com.techelevator.npgeek.model.WeatherDao;

@Controller
public class ParkWeatherController {
	
	@Autowired
	private ParkDao parkDao;
	@Autowired
	private WeatherDao weatherDao;
	
	@GetMapping("/")
	public String displayHomePage(ModelMap modelMap) {
		List<Park> allParks = parkDao.getAllParks();
		modelMap.put("parks", allParks);
		return "home";
	}
	
	@GetMapping("/detail")
	public String displayDetailPage(@RequestParam String code, @RequestParam(required = false) String celsius, ModelMap modelMap, HttpSession session) {
// Park to be displayed
		Park park = parkDao.getParkByCode(code);
		modelMap.put("park", park);
// Current Date to reference
		String month = LocalDate.now().getMonth().toString();
		int day = LocalDate.now().getDayOfMonth();
		modelMap.put("month", month);
		modelMap.put("day", day);
// MultiDay Forecast from Dark Sky API
		List<Weather> parkWeather = weatherDao.getForecastByLocaiton(park.getLocation());
// Celsius Check
		if (celsius !=null) {
			session.setAttribute("celsius", celsius);
			if (celsius.equals("t")) {
				for (Weather w: parkWeather) {
					w.isCelsius();
				}
			}
		} else {
			try { // pull out variable beforehand
				if (session.getAttribute("celsius").equals("t")) {
					for (Weather w: parkWeather) {
						w.isCelsius();
					}
				}
			} catch (NullPointerException e) {
				// Catch NullPointer
			}
		}
		modelMap.put("weathers", parkWeather);
		return "detail";
	}
	
}
