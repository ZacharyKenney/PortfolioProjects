package com.techelevator.npgeek.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Weather {


	private double lowTemp;
	private double highTemp;
	private String icon;
	private boolean isCelsius = false;
	private LocalDate date;
	
	@SuppressWarnings("serial")
	public static final Map<String,String> RECOMMENDATIONS = new HashMap<String,String>(){{
		put("snow", "Bring snow shoes! ");
		put("rain", "Bring rain gear and wear waterproof shoes! ");
		put("thunderstorms", "Seek shelter and avoid hiking on exposed ridges! ");
		put("sunny", "Bring sunscreen! ");
		put("hot", "Bring an extra gallon of water! ");
		put("tempChange", "Wear breatheable layers! ");
		put("cold", "FROSTBITE WARNING: Beware of exposure to frigid temperatures! ");
	}};
	
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public double getLowTemp() {
		double result = lowTemp;
		if (isCelsius) {
			result = (result - 32) * 0.55; //Celsius multiplier
		}
		return result;
	}
	public void setLowTemp(double lowTemp) {
		this.lowTemp = lowTemp;
	}
	public double getHighTemp() {
		double result = highTemp;
		if (isCelsius) {
			result = (result - 32) * 0.55; //Celsius multiplier
		}
		return result;
	}
	public void setHighTemp(double highTemp) {
		this.highTemp = highTemp;
	}
	
	public String getImageName() {
		switch(icon) {
		case "clear-day":
			icon = "sunny";
			break;
		case "clear-night":
			icon = "sunny";
			break;
		case "sleet":
			icon = "snow";
			break;
		case "wind":
			icon = "partlyCloudy";
			break;
		case "fog":
			icon = "cloudy";
			break;
		case "partly-cloudy-day":
			icon = "partlyCloudy";
			break;
		case "partly-cloudy-night":
			icon = "partlyCloudy";
			break;
		}
		return icon + ".png";	
	}
	
	public String getIcon() {
		return icon;
	}
	// Icon data point >>> existing weather image names
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void isCelsius() {
		this.isCelsius = true;
	}
	//class methods
	public String getRecommendation() {
		String result="";
		if(icon.equals("snow") || icon.equals("sleet")) {
			result += RECOMMENDATIONS.get("snow");
		} else 	if(icon.equals("rain")) {
			result += RECOMMENDATIONS.get("rain");
		} else 	if(icon.equals("clear-day") || icon.equals("clear-night")) {
			result += RECOMMENDATIONS.get("sunny");
		} else 	if(icon.equals("thunderstorm")) {
			result += RECOMMENDATIONS.get("thunderstorms");
		}
		if (isCelsius == false) {
	// README specified breakpoint for HOT = 75 F
			if (highTemp > 75) {
				result += RECOMMENDATIONS.get("hot");
	// README specified breakpoint for COLD = 20 F
			} else if (lowTemp < 20){
				result += RECOMMENDATIONS.get("cold");
			}
	// README specified breakpoint for TempDiff = 20 F
			if (highTemp - lowTemp > 20) {
				result += RECOMMENDATIONS.get("tempChange");
			}
		} else {
			double multiplier = .55;
	// C conversion HOT
			if (highTemp > 23.889) {
				result += RECOMMENDATIONS.get("hot");
	// C conversion Cold
			} else if (lowTemp < -6.667){
				result += RECOMMENDATIONS.get("cold");
			}
	// C Conversion TempDiff
			if (highTemp - lowTemp > 11.11) {
				result += RECOMMENDATIONS.get("tempChange");
			}
		}
		return result;	
	}
}
