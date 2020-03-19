package zach.com.wellnessmoth.model;

import java.util.List;

public class Team {

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
	
}
