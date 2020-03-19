package zach.com.wellnessmonth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import zach.com.wellnessmoth.model.WellnessMonthDAO;

@Controller 
public class WellnessMonthController {
	
	@Autowired
	WellnessMonthDAO wellnessMonthDAO;
	
	@RequestMapping("/greeting")
	public String displayGreeting() {
		
		return "greeting";
	}
}
