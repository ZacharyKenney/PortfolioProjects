package zach.com.wellnessmonth.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCwellnessMonthDAO implements WellnessMonthDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JDBCwellnessMonthDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
//Organizations
	@Override
	public Organization createNewOrg(Organization newOrg) {
		String sql = "INSERT INTO organization (org_name) "+
					 "VALUES (?) RETURNING org_id;";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newOrg.getName());
		newOrg.setId(id);
		return newOrg;
	}
	@Override
	public Organization getOrgByName(String name) {
		String sql = "SELECT org_id FROM organization WHERE org_name = ?;";
		Organization org = new Organization();
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, name);
		if (result.next()) {
			org.setId(result.getInt("org_id"));
		}
		org.setName(name);
		return org;
	}
// Contests
	@Override
	public WellnessContest createContest(WellnessContest newContest) {
		String sql = "INSERT INTO contest (org_id, start_date, end_date) " +
					 "VALUES (?, ?, ?) RETURNING contest_id;";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newContest.getOrgId(), newContest.getStartDate(), newContest.getEndDate());
		newContest.setId(id);
		Team freeAgents = new Team();
		freeAgents.setName("Free Agents");
		freeAgents.setContestId(id);
		createTeam(freeAgents);
		return newContest;
	}
	@Override
	public WellnessContest getContestById(int id) {
		String sql = "SELECT org_id, start_date, end_date " +
					 "FROM contest WHERE contest_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
		WellnessContest contest = new WellnessContest();
		if(result.next()) {
			contest.setId(id);
			contest.setOrgId(result.getInt("org_id"));
			contest.setStartDate(result.getDate("start_date").toLocalDate());
			contest.setEndDate(result.getDate("end_date").toLocalDate());
			contest.setTeams(getAllTeamsByContest(contest));
		}
		return contest;
	}
// Teams
	@Override
	public Team createTeam(Team newTeam) {
		String sql = "INSERT INTO team (team_name, contest_id) " +
				 	 "VALUES (?, ?) RETURNING team_id;";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newTeam.getName(), newTeam.getContestId());
		newTeam.setId(id);
		return newTeam;
	}
	@Override
	public Team getTeamById(int id) {
		String sql = "SELECT team_name, contest_id " +
					 "FROM team WHERE team_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
		Team team = new Team();
		if(result.next()) {
			team.setId(id);
			team.setName(result.getString("team_name"));
			team.setContestId(result.getInt("contest_id"));
		}
		return team;
	}
	@Override 
	public Team getFreeAgentsByContest(WellnessContest contest) {
		String sql = "SELECT team_id " +
				 	 "FROM team WHERE contest_id = ? AND team_name = 'Free Agents';";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, contest.getId());
		Team freeAgents = new Team();
		if (result.next()) {
			freeAgents.setId(result.getInt("team_id"));
			freeAgents.setName("Free Agents");
			freeAgents.setContestId(contest.getId());
		}
		return freeAgents;
	}
	@Override 
	public List<Team> getAllTeamsByContest(WellnessContest contest) {
		String sql = "SELECT team_id, team_name " +
					 "FROM team WHERE contest_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, contest.getId());
		List<Team> teams= new ArrayList<>();
		while(results.next()) {
			Team currentTeam = new Team();
			currentTeam.setId(results.getInt("team_id"));
			currentTeam.setName(results.getString("team_name"));
			teams.add(currentTeam);
		}
		return teams;
	}
// Players
	@Override
	public Player createPlayer(Player newPlayer) {
		String sql = "INSERT INTO player (player_name, team_id) "+
					 "VALUES (?, ?) RETURNING player_id;";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newPlayer.getName(), newPlayer.getTeamId());
		newPlayer.setId(id);
		return newPlayer;
	}
	@Override
	public Player getPlayerByID(int id) {
		String sql = "SELECT player_name, points, team_id "+
					 "FROM player WHERE player_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
		Player player = new Player();
		if(result.next()) {
			player.setId(id);
			player.setName(result.getString("player_name"));
			player.setPoints(result.getInt("points"));
			player.setTeamId(result.getInt("team_id"));
		}
		return player;
	}
	@Override 
	public List<Player> getAllPlayersByTeam(Team team) {
		String sql = "SELECT player_id, player_name, points " +
					 "FROM player WHERE team_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, team.getId());
		List<Player> players = new ArrayList<>();
		while(results.next()) {
			Player currentPlayer = new Player();
			currentPlayer.setId(results.getInt("player_id"));
			currentPlayer.setName(results.getString("player_name"));
			currentPlayer.setPoints(results.getInt("points"));
			players.add(currentPlayer);
		}
		return players;
	}
//Challenges
	@Override
	public Challenge createChallenge(Challenge newChallenge) {
		String sql = "INSERT INTO challenge (challenge_name, points, time_frame, description) "+
					 "VALUES (?, ?, ?) RETURNING challenge_id;";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newChallenge.getName(), newChallenge.getPoints(), 
																 newChallenge.getTimeframe(), newChallenge.getDescription());
		newChallenge.setId(id);
		return newChallenge;
	}
	@Override
	public Challenge getChallengeById(int id) {
		String sql = "SELECT challenge_name, points, time_frame, description "+
				 	 "FROM challenge WHERE challenge_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
		Challenge challenge = new Challenge();
		if(result.next()) {
			challenge.setId(id);
			challenge.setName(result.getString("challenge_name"));
			challenge.setPoints(result.getInt("points"));
			challenge.setTimeframe(result.getString("time_frame"));
			challenge.setDescription(result.getString("description"));
		}
		return challenge;
	}
	@Override
	public List<Challenge> getAllChallenges() {
		String sql = "SELECT challenge_id, challenge_name, points, time_frame, description "+
					 "FROM challenge";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
		List<Challenge> challenges = new ArrayList<>();
		while(results.next()) {
			Challenge currentChallenge = new Challenge();
			currentChallenge.setId(results.getInt("challenge_id"));
			currentChallenge.setName(results.getString("challenge_name"));
			currentChallenge.setPoints(results.getInt("points"));
			currentChallenge.setTimeframe(results.getString("time_frame"));
			currentChallenge.setDescription(results.getString("description"));
			challenges.add(currentChallenge);
		}
		return challenges;
	}
	@Override
	public boolean playerHasCompletedChallenge(Player player, Challenge challenge) {
		String sql = "SELECT challenge_id, player_id " +
					 "FROM player_challenge WHERE player_id = ? AND challenge_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, player.getId(),challenge.getId());
		boolean completed = false;
		if(result.next()) {
			completed = true;
		}
		return completed;
	}

}
