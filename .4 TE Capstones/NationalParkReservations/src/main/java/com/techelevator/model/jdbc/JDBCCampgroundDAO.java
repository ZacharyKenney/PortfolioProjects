package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.model.CampGroundDAO;
import com.techelevator.model.Campground;
import com.techelevator.model.Park;

public class JDBCCampgroundDAO implements CampGroundDAO{

	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Campground> getCampgroundsPerPark(Park park) {
		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee " +
					 "FROM campground WHERE park_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, park.getParkId());
		
		List<Campground> campList = new ArrayList<>();
		while (result.next()) {
			Campground currentCamp = new Campground();
			currentCamp.setCampgroundId(result.getInt("campground_id"));
			currentCamp.setParkId(result.getInt("park_id"));
			currentCamp.setCampName(result.getString("name"));
			currentCamp.setOpenMonth(result.getString("open_from_mm"));
			currentCamp.setCloseMonth(result.getString("open_to_mm"));
			currentCamp.setDailyFee(result.getBigDecimal("daily_fee"));
			campList.add(currentCamp);
		}
		return campList;
	}

	@Override
	public String getCampNameById(int campId) {
		String sql = "SELECT name FROM campground WHERE campground_id = ?;";
		
		String result = jdbcTemplate.queryForObject(sql, String.class, campId);
		
		return result;
	}

}
