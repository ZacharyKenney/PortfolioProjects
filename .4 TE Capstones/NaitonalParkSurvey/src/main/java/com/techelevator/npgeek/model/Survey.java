package com.techelevator.npgeek.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


public class Survey {
	
	@NotBlank (message="Please enter a valid email.")
	@Email(message="Invalid Email")
	private String email;
	
	@NotBlank (message="Please select a park.")
	private String favPark;
	
	@NotBlank (message="Please select your state of residence.")
	private String stateRes;
	@AssertTrue (message="Please select a valid state.")
	public boolean isValidState() {
		States[] states = States.values();
		for (States s : states) {
			if (s.toString().equals(stateRes)) {
				return true;
			}
		}
		return false;
	}
	
	@NotBlank (message="Please select an activity level.")
	private String activityLvl;
	@AssertTrue (message="Please select a valid activity level.")
	public boolean isValidActivityLevel() {
		List<String> levels = Arrays.asList(ACTIVITY_LEVELS); //make a constant, for each to populate jsp
		
		
		return levels.contains(activityLvl);
	}

	public final static String[] ACTIVITY_LEVELS = new String[] {"Inactive", "Sedentary", "Active", "Extremely Active"};
// Getters & Setters
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFavPark() {
		return favPark;
	}
	public void setFavPark(String favPark) {
		this.favPark = favPark;
	}
	public String getStateRes() {
		return stateRes;
	}
	public void setStateRes(String stateRes) {
		this.stateRes = stateRes;
	}
	public String getActivityLvl() {
		return activityLvl;
	}
	public void setActivityLvl(String activityLvl) {
		this.activityLvl = activityLvl;
	}
}
