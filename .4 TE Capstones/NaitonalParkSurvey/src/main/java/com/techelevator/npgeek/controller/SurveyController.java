package com.techelevator.npgeek.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techelevator.npgeek.model.Park;
import com.techelevator.npgeek.model.ParkDao;
import com.techelevator.npgeek.model.States;
import com.techelevator.npgeek.model.Survey;

@Controller
public class SurveyController {

	@Autowired
	private ParkDao parkDao;
	
	@GetMapping("/survey")
	public String displaySurvey(ModelMap modelMap) {
		
		if(modelMap.containsAttribute("surveyData") == false) {
			Survey empty = new Survey();
			modelMap.put("surveyData", empty);
		}
		List<Park> allParks = parkDao.getAllParks();
		modelMap.put("parks", allParks);
		States[] states = States.values();
		modelMap.put("states", states);
		modelMap.put("levels", Survey.ACTIVITY_LEVELS);
		return "survey";
	}
	
	@PostMapping("/survey")
	public String submitSurvey(@Valid @ModelAttribute Survey survey, BindingResult result, RedirectAttributes ra) {
		if(result.hasErrors()) {
			ra.addFlashAttribute("surveyData", survey);
			ra.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX +"surveyData", result);
			return "redirect:/survey";
		}
		List<String> parkCodes = parkDao.getAllParkCodes();
		if (parkCodes.contains(survey.getFavPark())) {
			parkDao.submitSurvey(survey);
		}
		return "redirect:/surveyresults";
	}
	
	@GetMapping("/surveyresults")
	public String displaySurveyResults(ModelMap modelMap) {
		Map<Park, Integer> parkResults = parkDao.getSurveyResults();
		
		modelMap.put("parkResults", parkResults);
		return "surveyResults";
	}
}
