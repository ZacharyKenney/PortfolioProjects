package zach.com.wellnessmonth.model;

import java.util.List;

public interface WellnessMonthDAO {
	public Organization createNewOrg(Organization newOrg);
	public Organization getOrgByName(String name);
	public WellnessContest createContest(WellnessContest newContest);
	public WellnessContest getContestById(int id);
	public Team createTeam(Team newTeam);
	public Team getTeamById(int id);
	public Team getFreeAgentsByContest(WellnessContest contest);
	public List<Team> getAllTeamsByContest(WellnessContest contest);
	public Player createPlayer(Player newPlayer);
	public Player getPlayerByID(int id);
	public List<Player> getAllPlayersByTeam(Team team);
	public Challenge createChallenge(Challenge newChallenge);
	public Challenge getChallengeById(int id);
	public List<Challenge> getAllChallenges();
	public boolean playerHasCompletedChallenge(Player player, Challenge challenge);
}
