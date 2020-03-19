package com.techelevator.model;

import java.util.List;

public interface ParkDAO {

	public List<Park> getParkNamesAndIDs();
	public Park getParkById(int parkId);
	
}
