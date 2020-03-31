package zach.com.wellnessmonth.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Team implements Comparable<Team>{

	private int id;
	private String name;
	private List<Player> roster;
	private int contestId;

	// Getters and Setters
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Player> getRoster() {
		return roster;
	}
	public void setRoster(List<Player> roster) {
		this.roster = roster;
	}
	public int getContestId() {
		return contestId;
	}
	public void setContestId(int contestId) {
		this.contestId = contestId;
	}
// Class Methods
	public int getTeamPoints() {
		int result = 0;
		for (Player p : roster) {
			result += p.getPoints();
		}
		return result;
	}
	
	public List<Player> getStandings() {
		Collections.sort(roster);
		return roster;
	}


	@Override
	public int compareTo(Team t) {
		int result = 0;
		
		if(this.getTeamPoints() < t.getTeamPoints()) {
			result = 1;
		} else if (this.getTeamPoints() > t.getTeamPoints()) {
			result = -1;
		}
		return result;
	}
	
}
