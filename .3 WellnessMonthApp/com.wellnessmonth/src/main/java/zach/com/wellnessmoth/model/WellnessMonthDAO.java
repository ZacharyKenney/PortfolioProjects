package zach.com.wellnessmoth.model;

public interface WellnessMonthDAO {
	public WellnessContest createContest(WellnessContest newContest);
	public WellnessContest getContestById(int id);
	public Team createTeam(Team newTeam);
	public Team getTeamById(int id);
	public Player createPlayer(Player newPlayer);
	public Player getPlayerByID(int id);
	public Challenge createChallenge(Challenge newChallenge);
	public Challenge getChallengeById(int id);
}
