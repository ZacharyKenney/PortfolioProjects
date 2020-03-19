package zach.com.wellnessmoth.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WellnessContest {
	
	private int id;
	private int orgId;
	private List<Team> teams;
	private LocalDate startDate;
	private LocalDate endDate;

// Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOrgId() {
		return orgId;
	}
	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}
	public List<Team> getTeams() {
		return teams;
	}
	public void setTeams(List<Team> teams) {
		this.teams = teams;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

//Class Methods
	public List<Team> getStandings() {
		List<Team> teamsOrderedByPoints = new ArrayList<>();
		int i = 0;
		while (i < teams.size()) {
			Team topTeam = teams.get(0);
			for (Team t : teams) {
				if (t.getTeamPoints() > topTeam.getTeamPoints()) {
					topTeam = t;
					i++;
				}
			}
			teamsOrderedByPoints.add(topTeam);
			teams.remove(topTeam);
		}
		teams = teamsOrderedByPoints;
		return teamsOrderedByPoints;
	}
	
}
