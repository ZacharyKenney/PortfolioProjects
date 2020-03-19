package com.techelevator.model;

import java.util.List;

public interface ReservationDAO {

	public List<Site> findAvailableSitesByCamp(Reservation userReservation);
	public Integer saveReservationToDB(Reservation userReservation);
	public List<Site> findAvailableSitesByPark(Reservation userReservation);

}
