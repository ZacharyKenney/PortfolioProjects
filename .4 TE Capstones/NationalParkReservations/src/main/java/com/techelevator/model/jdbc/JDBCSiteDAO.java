package com.techelevator.model.jdbc;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {

	private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public boolean isValidSite(int siteNumber, int campgroundId) {
		
		String sql = "SELECT site_id FROM site " +
					 "WHERE campground_id = ? AND site_number = ?;";
		Integer siteId = jdbcTemplate.queryForObject(sql, Integer.class, campgroundId, siteNumber);
		
		return siteId != null;
	}
	
	@Override
	public int siteIdByNumber(int siteNumber, int campgroundId) {
		String sql = "SELECT site_id FROM site " +
					 "WHERE site_number = ? AND campground_id = ?;";
		Integer siteId = jdbcTemplate.queryForObject(sql, Integer.class, siteNumber, campgroundId);
		
		return siteId;
	}
	
	@Override
	public int siteNumberById(int siteId) {
		String sql = "SELECT site_number FROM site " +
					 "WHERE site_id = ?;";
		Integer siteNumber = jdbcTemplate.queryForObject(sql, Integer.class, siteId);
		
		return siteNumber;
	}
	
	@Override
	public int campIdBySiteId(int siteId) {
		String sql = "SELECT campground_id FROM site " +
					 "WHERE site_id = ?;";
		Integer campId = jdbcTemplate.queryForObject(sql, Integer.class, siteId);
		
		return campId;
	}
	
	@Override
	public BigDecimal totalCostBySite(int numOfDays, int campgroundId) {
		String sql = "SELECT daily_fee FROM campground " +
					 "WHERE campground_id = ?;";
		BigDecimal costQuery = jdbcTemplate.queryForObject(sql, BigDecimal.class, campgroundId);
		return costQuery;
	}
}
