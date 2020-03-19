package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO{

	JdbcTemplate jdbcTemplate;
	
	public JDBCParkDAO(DataSource datasource) {
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}
	
	@Override
	public List<Park> getParkNamesAndIDs() {
		List<Park> parkList = new ArrayList<Park>();
		
		String sql = "SELECT park_id, name FROM park ORDER BY name;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		
		while (results.next()) {
			Park currentPark = new Park();
			currentPark.setParkId(results.getInt("park_id"));
			currentPark.setParkName(results.getString("name"));
			parkList.add(currentPark);
		}
		
		return parkList;
	}

	@Override
	public Park getParkById(int parkId) {
		Park selectedPark = new Park();
		String sql = "SELECT name, location, establish_date, area, visitors, description " +
					 "FROM park WHERE park_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
		if (results.next()) {
			selectedPark.setParkId(parkId);
			selectedPark.setParkName(results.getString("name"));
			selectedPark.setParkLocation(results.getString("location"));
			selectedPark.setEstDate(results.getDate("establish_date").toLocalDate());
			selectedPark.setAreaSqFt(results.getInt("area"));
			selectedPark.setVisitorsPerYear(results.getInt("visitors"));
			selectedPark.setDescription(results.getString("description"));
		}
		return selectedPark;
	}

}
