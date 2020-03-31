package zach.com.wellnessmonth.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

@Component
public class JDBCwellnessMonthDAO implements WellnessMonthDAO {
	
	private JdbcTemplate jdbcTemplate;
	private PasswordHasher passwordHasher;
	
	@Autowired
	public JDBCwellnessMonthDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.passwordHasher = new PasswordHasher();
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
			team.setRoster(getAllPlayersByTeam(team));
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
			currentTeam.setRoster(getAllPlayersByTeam(currentTeam));
			teams.add(currentTeam);
		}
		return teams;
	}
	
	@Override 
	public Player joinTeam(Player user, Team exisitingTeam) {
		String sql = "UPDATE player SET team_id = ? "+
					 "WHERE player_name = ?;";
		jdbcTemplate.update(sql, exisitingTeam.getId(), user.getName());
		exisitingTeam.setRoster(getAllPlayersByTeam(exisitingTeam));
		user = getPlayerByName(user.getName());
		return user;
	}
// Players
	@Override
	public Player createPlayer(Player newPlayer, String playerPW) {
		byte[] salt = passwordHasher.generateRandomSalt();
        String hashedPassword = passwordHasher.computeHash(playerPW, salt);
        String saltString = new String(Base64.getEncoder().encode(salt));
        
		String sql = "INSERT INTO player (player_name, player_pw, salt, team_id) "+
					 "VALUES (?, ?, ?, ?);";
		jdbcTemplate.update(sql, newPlayer.getName(), hashedPassword, saltString, newPlayer.getTeamId());
		return newPlayer;
	}
	@Override
    public boolean isUsernameAndPasswordValid(String playerName, String playerPW) {
        String sqlSearchForUser = "SELECT player_name, player_pw, salt FROM player WHERE player_name = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchForUser, playerName);
        if (results.next()) {
            String storedSalt = results.getString("salt");
            String storedPassword = results.getString("player_pw");
            String hashedPassword = passwordHasher.computeHash(playerPW, Base64.getDecoder().decode(storedSalt));
            return storedPassword.equals(hashedPassword);
        } else {
            return false;
        }
    }
	@Override
	public Player getPlayerByName(String playerName) {
		String sql = "SELECT points, team_id "+
					 "FROM player WHERE player_name = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, playerName);
		Player player = new Player();
		if(result.next()) {
			player.setName(playerName);
			player.setPoints(result.getInt("points"));
			player.setTeamId(result.getInt("team_id"));
		}
		return player;
	}
	@Override 
	public List<Player> getAllPlayersByTeam(Team team) {
		String sql = "SELECT player_name, points " +
					 "FROM player WHERE team_id = ?;";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, team.getId());
		List<Player> players = new ArrayList<>();
		while(results.next()) {
			Player currentPlayer = new Player();
			currentPlayer.setName(results.getString("player_name"));
			currentPlayer.setPoints(results.getInt("points"));
			players.add(currentPlayer);
		}
		return players;
	}
//Challenges
	@Override
	public Challenge createChallenge(Challenge newChallenge) {
		String sql = "INSERT INTO challenge (challenge_name, points, time_frame, description, category) "+
					 "VALUES (?, ?, ?, ?, ?) RETURNING challenge_id;";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newChallenge.getName(), newChallenge.getPoints(), 
																 newChallenge.getTimeframe(), newChallenge.getDescription(), 
																 newChallenge.getCategory());
		newChallenge.setId(id);
		return newChallenge;
	}
	@Override
	public Challenge getChallengeById(int id) {
		String sql = "SELECT challenge_name, points, time_frame, description, category "+
				 	 "FROM challenge WHERE challenge_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
		Challenge challenge = new Challenge();
		if(result.next()) {
			challenge.setId(id);
			challenge.setName(result.getString("challenge_name"));
			challenge.setPoints(result.getInt("points"));
			challenge.setTimeframe(result.getString("time_frame"));
			challenge.setDescription(result.getString("description"));
			challenge.setCategory(result.getString("category"));
		}
		return challenge;
	}
	@Override
	public List<Challenge> getAllChallenges() {
		String sql = "SELECT challenge_id, challenge_name, points, time_frame, description, category "+
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
			currentChallenge.setCategory(results.getString("category"));
			challenges.add(currentChallenge);
		}
		return challenges;
	}
	@Override
	public boolean playerHasCompletedChallenge(Player player, Challenge challenge) {
		String sql = "SELECT challenge_id, player_name " +
					 "FROM player_challenge WHERE player_name = ? AND challenge_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, player.getName(),challenge.getId());
		boolean completed = false;
		if(result.next()) {
			completed = true;
		}
		return completed;
	}
	@Override
	public void completeChallenge(String playerName, int challengeId) {
		String sql = "INSERT INTO player_challenge (player_name, challenge_id) "+
					 "VALUES (?, ?);";
		jdbcTemplate.update(sql, playerName, challengeId);
		
		Player user = getPlayerByName(playerName);
		sql = "UPDATE player SET points = ? "+
			  "WHERE player_name = ?";
		jdbcTemplate.update(sql, user.getPoints()+getChallengeById(challengeId).getPoints(), playerName);
	}

}
