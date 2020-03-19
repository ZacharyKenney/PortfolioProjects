package com.techelevator.model;

import java.util.List;

public interface CampGroundDAO {

	public List<Campground> getCampgroundsPerPark(Park park);
	
	public String getCampNameById(int campId); 
}
