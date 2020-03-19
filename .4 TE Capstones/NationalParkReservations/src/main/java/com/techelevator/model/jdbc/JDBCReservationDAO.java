package com.techelevator.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.Site;

public class JDBCReservationDAO implements ReservationDAO {

private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> findAvailableSitesByCamp(Reservation userReservation) {
		String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
					 "FROM site " +
					 "WHERE campground_id = ? AND " +
					 "max_occupancy >= ? AND " +
					 "max_rv_length >= ? AND " +
					 "(accessible = true OR accessible = ?) AND " +
					 "(utilities = true OR utilities = ?) AND " +
					 "site_id NOT IN (SELECT site_id FROM reservation " + 
					 "WHERE (start_date, start_date + num_days) " + 
					 "OVERLAPS (?, ?)) " +
					 "ORDER BY site_number " +
					 "LIMIT 5;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userReservation.getCampgroundId(), 
							userReservation.getPartySize(), userReservation.getRVLength(),
							userReservation.isAccessible(), userReservation.isUtility(),
							userReservation.getStartDate(), userReservation.getEndDate());
		
		List<Site> availableSites = new ArrayList<Site>();
		while (results.next()) {
			Site currentSite = new Site();
			currentSite.setSiteId(results.getInt("site_id"));
			currentSite.setCampgroundId(results.getInt("campground_id"));
			currentSite.setSiteNumber(results.getInt("site_number"));
			currentSite.setMaxOccupancy(results.getInt("max_occupancy"));
			currentSite.setAccessible(results.getBoolean("accessible"));
			currentSite.setMaxRVLength(results.getInt("max_rv_length"));
			currentSite.setUtilities(results.getBoolean("utilities"));
			availableSites.add(currentSite);
		}
		return availableSites;
	}
	
	@Override
	public Integer saveReservationToDB(Reservation userReservation) {
		String sql = "INSERT INTO reservation (site_id, name, create_date, start_date, num_days) " +
					 "VALUES (?, ?, ?, ?, ?) RETURNING reservation_id;";
		Integer reservationId = jdbcTemplate.queryForObject(sql, Integer.class, userReservation.getSiteId(), userReservation.getReservationName(), LocalDate.now(),
							                 userReservation.getStartDate(), userReservation.getDurationDays());
		
		return reservationId;
	}
	
	@Override
	public List<Site> findAvailableSitesByPark(Reservation userReservation) {
		
		String sql = "SELECT site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
					 "FROM site " +
					 "JOIN campground ON site.campground_id = campground.campground_id " +
					 "WHERE CAST(open_from_mm AS INTEGER) <= ? AND CAST(open_to_mm AS INTEGER) > ? " +
					 "AND site_id NOT IN (SELECT site_id FROM reservation " + 
					 "WHERE (start_date, start_date + num_days) " + 
					 "OVERLAPS (?, ?)) " +
					 "ORDER BY max_occupancy DESC, site_id " +
					 "LIMIT 5;";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userReservation.getStartDate().getMonthValue(),
				userReservation.getEndDate().getMonthValue(),userReservation.getStartDate(), userReservation.getEndDate());
		
		List<Site> availableSites = new ArrayList<Site>();
		while (results.next()) {
			Site currentSite = new Site();
			currentSite.setSiteId(results.getInt("site_id"));
			currentSite.setCampgroundId(results.getInt("campground_id"));
			currentSite.setSiteNumber(results.getInt("site_number"));
			currentSite.setMaxOccupancy(results.getInt("max_occupancy"));
			currentSite.setAccessible(results.getBoolean("accessible"));
			currentSite.setMaxRVLength(results.getInt("max_rv_length"));
			currentSite.setUtilities(results.getBoolean("utilities"));
			availableSites.add(currentSite);
		}
		return availableSites;
	}
	
	public List<Reservation> reservationsNext30DaysByPark(Park selectedPark) {
		List<Reservation> results = new ArrayList<>();
		String sql = "SELECT reservation_id, reservation.site_id, reservation.name, start_date, num_days, create_date " +
					"FROM reservation " +
					"JOIN site ON reservation.site_id = site.site_id " +
					"JOIN campground ON site.campground_id = campground.campground_id " +
					"JOIN park ON campground.park_id = park.park_id " +
					"WHERE park.park_id = ?" +
					"AND start_date < current_date + 30;";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql, selectedPark.getParkId());
		while(row.next()) {
			Reservation currentRes = new Reservation(row.getInt("reservation_id"), row.getInt("site_id"), row.getString("name"),
					row.getDate("start_date").toLocalDate(),row.getInt("num_days"), row.getDate("create_date").toLocalDate());
			results.add(currentRes);
		}
		return results;
	}
}
