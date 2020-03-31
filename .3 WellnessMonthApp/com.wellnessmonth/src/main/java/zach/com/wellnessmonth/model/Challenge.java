package zach.com.wellnessmonth.model;

public class Challenge {
	private int id;
	private String name;
	private int points;
	private String timeframe;
	private String description;
	private String category;
	
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
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public String getTimeframe() {
		return timeframe;
	}
	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
}
