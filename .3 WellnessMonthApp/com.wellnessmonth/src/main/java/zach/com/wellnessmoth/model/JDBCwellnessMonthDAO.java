package zach.com.wellnessmoth.model;

import java.time.LocalDate;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCwellnessMonthDAO implements WellnessMonthDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public JDBCwellnessMonthDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
// Contests
	@Override
	public WellnessContest createContest(WellnessContest newContest) {
		String sql = "INSERT INTO contest (org_id, start_date, end_date) " +
					 "VALUES (?, ?, ?);";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newContest.getOrgId(), newContest.getStartDate(), newContest.getEndDate());
		newContest.setId(id);
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
		}
		return contest;
	}
// Teams
	@Override
	public Team createTeam(Team newTeam) {
		String sql = "INSERT INTO team (team_name, contest_id) " +
				 "VALUES (?, ?);";
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
// Players
	@Override
	public Player createPlayer(Player newPlayer) {
		String sql = "INSERT INTO player (player_name, team_id) "+
					 "VALUES (?, ?);";
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
//Challenges
	@Override
	public Challenge createChallenge(Challenge newChallenge) {
		String sql = "INSERT INTO challenge (challenge_name, points, timeframe, description) "+
					 "VALUES (?, ?, ?);";
		int id = jdbcTemplate.queryForObject(sql, Integer.class, newChallenge.getName(), newChallenge.getPoints(), 
																 newChallenge.getTimeframe(), newChallenge.getDescription());
		newChallenge.setId(id);
		return newChallenge;
	}
	@Override
	public Challenge getChallengeById(int id) {
		String sql = "SELECT challenge_name, points, timeframe, description "+
				 	 "FROM challenge WHERE challenge_id = ?;";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
		Challenge challenge = new Challenge();
		if(result.next()) {
			challenge.setId(id);
			challenge.setName(result.getString("challenge_name"));
			challenge.setPoints(result.getInt("points"));
			challenge.setTimeframe(result.getString("timeframe"));
			challenge.setDescription(result.getString("description"));
		}
		return challenge;
	}

}
