package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.dao.EmptyResultDataAccessException;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;

public class CampgroundCLI {

	private static Scanner input = new Scanner(System.in);
	private Menu menu;

	private static final String PARK_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH_RESERVATION = "Search for Reservation";
	private static final String PARK_MENU_OPTION_DISPLAY_RESERVATIONS = "Display Reservations";
	private static final String PARK_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] PARK_MENU_OPTIONS = new String[] { PARK_MENU_OPTION_VIEW_CAMPGROUNDS,
			PARK_MENU_OPTION_SEARCH_RESERVATION, PARK_MENU_OPTION_DISPLAY_RESERVATIONS, PARK_MENU_OPTION_RETURN };

	private static final String CAMP_MENU_OPTION_SEARCH_RESERVATIONS = "Search Available Reservation";
	private static final String CAMP_MENU_OPTION_RETURN = "Return to Previous Screen";
	private static final String[] CAMP_MENU_OPTIONS = new String[] { CAMP_MENU_OPTION_SEARCH_RESERVATIONS,
			CAMP_MENU_OPTION_RETURN };

	private JDBCParkDAO parkDao;
	private JDBCCampgroundDAO campDao;
	private JDBCReservationDAO reservationDao;
	private JDBCSiteDAO siteDao;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword(System.getenv("DB_PASSWORD"));
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		this.parkDao = new JDBCParkDAO(datasource);
		this.campDao = new JDBCCampgroundDAO(datasource);
		this.reservationDao = new JDBCReservationDAO(datasource);
		this.siteDao = new JDBCSiteDAO(datasource);
	}

	private void run() {
		System.out.println();
		Park selectedPark = showParkMenu();
//Park Details
		boolean done = false;
		while (done == false) {
			System.out.println();
			System.out.println(selectedPark);
			System.out.println();
			System.out.println();
			System.out.println("Select a Command Below:");
			String command = (String) menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
//Park Menu Options
			if (command.equals(PARK_MENU_OPTION_VIEW_CAMPGROUNDS)) {
				handleViewCampgrounds(selectedPark);
				// Camp Menu Options
				String campOption = (String) menu.getChoiceFromOptions(CAMP_MENU_OPTIONS);
				if (campOption.equals(CAMP_MENU_OPTION_SEARCH_RESERVATIONS)) {

					Reservation userReservation = handleSearchSitesByCamp(selectedPark);
					if (userReservation.getCampgroundId() != -1) {

						int reservationChoice = findAvailableReservationByCamp(userReservation);
						if (reservationChoice != 0) {
							setReservation(userReservation);
						}
						done = true;
					}
				} else if (campOption.equals(CAMP_MENU_OPTION_RETURN)) {
					// Back to Loop
				}
			} else if (command.equals(PARK_MENU_OPTION_SEARCH_RESERVATION)) {
				Reservation userReservation = handleSearchSitesByPark(selectedPark);
				if (userReservation.getCampgroundId() != -1) {

					int reservationChoice = findAvailableReservationByPark(userReservation);
					if (reservationChoice != 0) {
						setReservation(userReservation);
					}
					done = true;
				}
			} else if (command.equals(PARK_MENU_OPTION_DISPLAY_RESERVATIONS)) {
				System.out.format("%-20s %-20s %-20s %-20s %-20s %-20s\n", "Reservation ID", "Site ID",
						"Reservation Name", "Start Date", "Duration", "Submission Date");// header
				List<Reservation> resList = reservationDao.reservationsNext30DaysByPark(selectedPark);
				for (Reservation res : resList) {
					System.out.println(res);
				}
				done = true;
			} else if (command.equals(PARK_MENU_OPTION_RETURN)) {
				run();
			}
		}
		run();
	}

	private Park showParkMenu() {
		Park selectedPark = null;
		boolean done = false;
		while (done == false) {
			System.out.println("Park Menu");
			System.out.println("***********");
			List<Park> parkList = parkDao.getParkNamesAndIDs();
			int parkCounter = 0;
//Park Menu
			for (Park p : parkList) {
				parkCounter++;
				System.out.format("%d) %s\n", parkCounter, p.getParkName());
			}
			System.out.print("Select a park for futher details (Q to quit): ");
			String choice = input.nextLine().toUpperCase();
			try {
				if (choice.equals("Q")) {
					System.out.println("Thanks & Come Again!");
					System.exit(0);
				} else if (Integer.parseInt(choice) > parkList.size()) {
					throw new NumberFormatException();
				}
				selectedPark = parkList.get(Integer.parseInt(choice) - 1);
				selectedPark = parkDao.getParkById(selectedPark.getParkId());
				done = true;
			} catch (NumberFormatException e) {
				System.out.println("Please input valid option!\n");
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Please input valid option!\n");
			}
		}
		return selectedPark;
	}

	private void handleViewCampgrounds(Park selectedPark) {
		List<Campground> campsPerPark = campDao.getCampgroundsPerPark(selectedPark);
		int campCounter = 0;
		System.out.println("");
		System.out.println(selectedPark.getParkName() + " Campgrounds\n");
		System.out.format("%-6s %-35s %-15s %-15s %-15s\n", "", "Name", "Open", "Close", "Daily Fee");
		System.out.println("       -----------------------------------------------------------------------------");
		for (Campground c : campsPerPark) {
			campCounter++;
			System.out.format("#%-5d %s\n", campCounter, c);
		}
	}

	private Reservation handleSearchSitesByCamp(Park selectedPark) {
		Reservation result = null;
		List<Campground> campsPerPark = campDao.getCampgroundsPerPark(selectedPark);
		Campground selectedCampground = null;
		LocalDate arriveTime = null;
		LocalDate departTime = null;

		boolean done = false;
		while (done == false) {

			System.out.print("Which campground would you like to stay at?(enter 0 to cancel): ");
			String campChoice = input.nextLine();
			if (campChoice.equals("0")) {
				return new Reservation();
			}
			try {
				selectedCampground = campsPerPark.get(Integer.parseInt(campChoice) - 1);
				done = true;
			} catch (Exception e) {
				System.out.println("Invalid campground. PLEASE input a valid campground number.");
			}
		}
		done = false;
		while (done == false) {
			System.out.print("When will you be arriving?(mm/dd/yyyy): ");
			String[] arrivalDate = input.nextLine().split("/");
			try {
				int month = Integer.parseInt(arrivalDate[0]);
				int day = Integer.parseInt(arrivalDate[1]);
				int year = Integer.parseInt(arrivalDate[2]);

				if (month < Integer.parseInt(selectedCampground.getOpenMonth())
						|| month >= Integer.parseInt(selectedCampground.getCloseMonth())) {
					throw new Exception();
				}
				arriveTime = LocalDate.of(year, month, day);
				if (arriveTime.compareTo(LocalDate.now()) < 0) {
					throw new Exception();
				}
				done = true;
			} catch (Exception e) {
				System.out.println("This is an invalid arrival date.");
			}
		}
		done = false;
		while (done == false) {
			System.out.print("When will you be departing?(mm/dd/yyyy): ");
			String[] departDate = input.nextLine().split("/");
			try {
				int month = Integer.parseInt(departDate[0]);
				int day = Integer.parseInt(departDate[1]);
				int year = Integer.parseInt(departDate[2]);

				departTime = LocalDate.of(year, month, day);
				if (month < Integer.parseInt(selectedCampground.getOpenMonth())
						|| month >= Integer.parseInt(selectedCampground.getCloseMonth())) {
					throw new Exception();
				}
				if (departTime.compareTo(arriveTime) < 0) {
					throw new Exception();
				}
				done = true;
			} catch (Exception e) {
				System.out.println("This is an invalid departure date.");
			}
		}
		
		boolean useAdvanced = false;
		int partySize = 0;
		int rVLength = 0;
		boolean handicapNeeded = false;
		boolean utilitiesNeeded = false;
		
		System.out.print("Would you like to use advanced search options? (y/n): ");
		String advancedChoice = input.nextLine().toUpperCase();
		useAdvanced = advancedChoice.equals("Y");
		if (useAdvanced) {
			done = false;
			while (done == false) {
				try {
					System.out.print("\nPlease enter your party's size (Enter 0 to skip): ");
					partySize = Integer.parseInt(input.nextLine());
					System.out.print("\nPlease enter your RV length in feet (Enter 0 for no RV): ");
					rVLength = Integer.parseInt(input.nextLine());
					done = true;
				} catch (NumberFormatException e) {
					System.out.println("Invalid number!");
				}
			}
			System.out.print("\nDo you require a handicap accesible site? (y/n): ");
			handicapNeeded = input.nextLine().toUpperCase().equals("Y");
			System.out.print("\nDo you require site ultilities? (y/n): ");
			utilitiesNeeded = input.nextLine().toUpperCase().equals("Y");
		}
		if (useAdvanced) {
			result = new Reservation(selectedCampground, arriveTime, departTime, partySize, rVLength, handicapNeeded, utilitiesNeeded);
		} else {
			result = new Reservation(selectedCampground, arriveTime, departTime);
		}
		return result;
	}

	private Reservation handleSearchSitesByPark(Park selectedPark) {
		boolean done = false;
		LocalDate arriveTime = null;
		LocalDate departTime = null;
		while (done == false) {
			System.out.print("When will you be arriving?(mm/dd/yyyy): ");
			String[] arrivalDate = input.nextLine().split("/");
			try {
				int month = Integer.parseInt(arrivalDate[0]);
				int day = Integer.parseInt(arrivalDate[1]);
				int year = Integer.parseInt(arrivalDate[2]);

				arriveTime = LocalDate.of(year, month, day);
				if (arriveTime.compareTo(LocalDate.now()) < 0) {
					throw new Exception();
				}
				done = true;
			} catch (Exception e) {
				System.out.println("This is an invalid arrival date.");
			}
		}
		done = false;
		while (done == false) {
			System.out.print("When will you be departing?(mm/dd/yyyy): ");
			String[] departDate = input.nextLine().split("/");
			try {
				int month = Integer.parseInt(departDate[0]);
				int day = Integer.parseInt(departDate[1]);
				int year = Integer.parseInt(departDate[2]);

				departTime = LocalDate.of(year, month, day);
				if (departTime.compareTo(arriveTime) < 0) {
					throw new Exception();
				}
				done = true;
			} catch (Exception e) {
				System.out.println("This is an invalid departure date.");
			}
		}

		return new Reservation(arriveTime, departTime);
	}

	private int findAvailableReservationByCamp(Reservation userReservation) {
		List<Site> availableSites = reservationDao.findAvailableSitesByCamp(userReservation);
		int numberOfDays = userReservation.getDurationDays();
		BigDecimal costPerDay = userReservation.getCostPerDay();
		BigDecimal totalCost = costPerDay.multiply(new BigDecimal(numberOfDays));
		int reservationChoice = 0;
		if (availableSites.size() == 0) {
			System.out.println("There are no available sites for that criteria!\n");
		} else {
			System.out.println("\nResults Matching Your Search Criteria!\n");
			System.out.format("%-15s %-15s %-15s %-15s %-15s %-15s\n", "Site #", "Max Occupancy", "Accessible?",
					"Max RV Length", "Utility?", "Cost");
			for (Site s : availableSites) {
				System.out.format("%s $%.02f\n", s, totalCost);
			}
			boolean done = false;
			while (done == false) {
			System.out.print("Enter site ID to be reserved(0 for cancel): ");
			String reservationChoiceString = input.nextLine();
			if (reservationChoiceString.equals("0") == false) {
					try {
						reservationChoice = Integer.parseInt(reservationChoiceString);
						if (reservationChoice != 0) {
							userReservation.setSiteId(
									siteDao.siteIdByNumber(reservationChoice, userReservation.getCampgroundId()));
						}
						if (siteDao.isValidSite(reservationChoice, userReservation.getCampgroundId())) {
							done = true;
						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.println("Please enter a valid site number!");
					} catch (EmptyResultDataAccessException e) {
						System.out.println("Please enter a valid site number!");
					}
			} else {
				done = true;
			}
			}
		}
		return reservationChoice;
	}

	private int findAvailableReservationByPark(Reservation userReservation) {
		int reservationChoice = 0;

		List<Site> availableSites = reservationDao.findAvailableSitesByPark(userReservation);
		int numberOfDays = userReservation.getDurationDays();
		if (availableSites.size() == 0) {
			System.out.println("No available sites for that criteria");
		} else {
			System.out.println("\nResults Matching Your Search Criteria!\n");
			System.out.format("%-15s %-25s %-15s %-15s %-15s %-15s %-15s %-15s\n", "Site ID", "Campground", "Site #",
					"Max Occupancy", "Accessible?", "Max RV Length", "Utility?", "Cost");
			System.out.println(
					"----------------------------------------------------------------------------------------------------------------------------------------");
			for (Site s : availableSites) {
				System.out.format("%-15d %-25s %s $%.02f\n", s.getSiteId(),
						campDao.getCampNameById(s.getCampgroundId()), s,
						siteDao.totalCostBySite(numberOfDays, s.getCampgroundId()));
			}
			boolean done = false;
			while (done == false) {
			System.out.print("Enter site ID to be reserved(0 for cancel): ");
			String reservationChoiceString = input.nextLine();
			if (reservationChoiceString.equals("0") == false) {
					try {
						reservationChoice = Integer.parseInt(reservationChoiceString);
						userReservation.setSiteId(reservationChoice);
						int siteNumber = siteDao.siteNumberById(reservationChoice);
						int campId = siteDao.campIdBySiteId(reservationChoice);
						if (siteDao.isValidSite(siteNumber, campId)) {
							done = true;
						} else {
							throw new NumberFormatException();
						}
					} catch (NumberFormatException e) {
						System.out.println("Please enter a valid site number!");
					} catch (EmptyResultDataAccessException e) {
						System.out.println("Please enter a valid site number!");
					}
			} else {
				done = true;
			}
			}
		}
		return reservationChoice;
	}

	private void setReservation(Reservation userReservation) {
		System.out.print("What name should the reservation be under?: ");
		userReservation.setReservationName(input.nextLine());
		Integer reservationId = reservationDao.saveReservationToDB(userReservation);
		if (reservationId != null) {
			System.out.println("The reservation has been made and the confirmation ID is " + reservationId);
		} else {
			System.out.println("Sorry, your reservation could not be saved!");
		}
	}
}