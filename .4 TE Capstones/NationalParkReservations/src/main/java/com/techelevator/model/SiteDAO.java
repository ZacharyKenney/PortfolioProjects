package com.techelevator.model;

import java.math.BigDecimal;

public interface SiteDAO {

	public boolean isValidSite(int siteNumber, int campgroundId);
	public int siteIdByNumber(int siteNumber, int campgroundId);
	public int siteNumberById(int siteId);
	public int campIdBySiteId(int siteId);
	public BigDecimal totalCostBySite(int numOfDays, int campgroundId);
}
