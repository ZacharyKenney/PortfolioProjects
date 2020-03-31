package zach.com.wellnessmonth.model;

public class Player implements Comparable<Player>{

	private String name;
	private int points;
	private int teamId;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
// Class Methods
	
	@Override
	public int compareTo(Player p) {
		int result = 0;
		
		if(this.getPoints() < p.getPoints()) {
			result = 1;
		} else if (this.getPoints() > p.getPoints()) {
			result = -1;
		}
		return result;
	}
}
