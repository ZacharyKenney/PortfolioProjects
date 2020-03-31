package zach.com.wellnessmonth.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import zach.com.wellnessmonth.model.Challenge;
import zach.com.wellnessmonth.model.Organization;
import zach.com.wellnessmonth.model.Player;
import zach.com.wellnessmonth.model.Team;
import zach.com.wellnessmonth.model.WellnessContest;
import zach.com.wellnessmonth.model.WellnessMonthDAO;

@Controller 
public class WellnessMonthController {
	
	@Autowired
	private WellnessMonthDAO wellnessMonthDAO;
	
	@GetMapping("/")
	public String displayHome(ModelMap modelMap) {
		modelMap.put("challenges", wellnessMonthDAO.getAllChallenges());
		return "home";
	}
	
	
	@GetMapping("/contest")
	public String displayContestRegistration() {
		return "contest";
	}
	@PostMapping("/contest")
	public String createNewContest(@RequestParam(required = false) String orgName, @RequestParam(required = false) String startDate, 
								   @RequestParam(required = false) String endDate, RedirectAttributes ra) {
		Organization org = wellnessMonthDAO.getOrgByName(orgName);
		if(org.getId() == null) {
			org = wellnessMonthDAO.createNewOrg(org);
		}
		WellnessContest newContest = new WellnessContest();
		newContest.setOrgId(org.getId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		newContest.setStartDate(LocalDate.parse(startDate, formatter));
		newContest.setEndDate(LocalDate.parse(endDate, formatter));
		newContest = wellnessMonthDAO.createContest(newContest);
		ra.addFlashAttribute("confirmation", "New Contest Created! ID = "+ newContest.getId());
		return "redirect:/";
	}
	@GetMapping("/login") 
	public String displayLogin() {
		return "login";
	}
	@PostMapping("/login")
	public String completeLogin(@RequestParam String playerName, @RequestParam String playerPW, 
								HttpSession session, RedirectAttributes ra) {
		if(wellnessMonthDAO.isUsernameAndPasswordValid(playerName, playerPW)) {
			Player user = wellnessMonthDAO.getPlayerByName(playerName);
			Team userTeam = wellnessMonthDAO.getTeamById(user.getTeamId());
			WellnessContest userContest = wellnessMonthDAO.getContestById(userTeam.getContestId());
			ra.addFlashAttribute("confirmation", "login sucess!");
			session.setAttribute("user", user);
			session.setAttribute("userTeam", userTeam);
			session.setAttribute("userContest", userContest);
		} else {
			ra.addFlashAttribute("confirmation", "login Failed");
		}
		return "redirect:/";
	}
	@GetMapping("/register")
	public String displayRegistration() {
		return "register";
	}
	@PostMapping("/register")
	public String createNewPlayer(@RequestParam String playerName, @RequestParam String playerPW, @RequestParam int contestId, RedirectAttributes ra) {
		Player newPlayer = new Player();
		WellnessContest contest = wellnessMonthDAO.getContestById(contestId);
		newPlayer.setName(playerName);
		newPlayer.setTeamId(wellnessMonthDAO.getFreeAgentsByContest(contest).getId());
		wellnessMonthDAO.createPlayer(newPlayer, playerPW);
		ra.addFlashAttribute("confirmation", "New Player Created! Username = "+ newPlayer.getName());
		return "redirect:/";
	}
	
	@GetMapping("/teams")
	public String displayTeamOptions(@RequestParam boolean newTeam, ModelMap modelMap) {
		modelMap.put("newTeam", newTeam);
		return "teams";
	}
	@PostMapping("/teams")
	public String updateTeamInfo(@RequestParam(required=false) String newTeamName, @RequestParam(required=false) Integer teamIdToJoin,
								 @RequestParam boolean newTeam, HttpSession session) {
		WellnessContest userContest = (WellnessContest) session.getAttribute("userContest");
		Player user = (Player)session.getAttribute("user");
		Team team = new Team();
		if(newTeam) {
			team.setName(newTeamName);
			team.setContestId(userContest.getId());
			team = wellnessMonthDAO.createTeam(team);
			user = wellnessMonthDAO.joinTeam(user, team);
			session.setAttribute("user", user);
			session.setAttribute("userTeam", team);
		} else {
			team = wellnessMonthDAO.getTeamById(teamIdToJoin);
			user = wellnessMonthDAO.joinTeam(user, team);
			session.setAttribute("user", user);
			session.setAttribute("userTeam", team);
		}
		return "redirect:/";
	}
	
	@GetMapping("/challenge")
	public String displayAddChallenge() {
		return "challenge";
	}
	@PostMapping("/challenge")
	public String createNewChallenge(@RequestParam String challengeName, @RequestParam String challengeCategory, @RequestParam int challengePoints, 
									 @RequestParam String challengeTimeFrame, @RequestParam String challengeDesc, RedirectAttributes ra) {
		Challenge newChallenge = new Challenge();
		newChallenge.setName(challengeName);
		newChallenge.setPoints(challengePoints);
		newChallenge.setTimeframe(challengeTimeFrame);
		newChallenge.setDescription(challengeDesc);
		newChallenge.setCategory(challengeCategory);
		newChallenge = wellnessMonthDAO.createChallenge(newChallenge);
		ra.addFlashAttribute("confirmation","New Challenge Created! ID = "+ newChallenge.getId());
		return "redirect:/";
	}
	
	@GetMapping("/detail")
	public String displayChallengeDetails(@RequestParam int id, ModelMap modelMap, HttpSession session) {
		Player user = (Player) session.getAttribute("user");
		Challenge challenge = wellnessMonthDAO.getChallengeById(id);
		modelMap.put("challenge", challenge);
		modelMap.put("completed", false);
		if (user != null) {
			modelMap.put("completed", wellnessMonthDAO.playerHasCompletedChallenge(user, challenge));
		}
		return "detail";
	}
	@PostMapping("/detail")
	public String completeChallenge(@RequestParam String playerName, @RequestParam int challengeId, 
									HttpSession session, RedirectAttributes ra) {
		wellnessMonthDAO.completeChallenge(playerName, challengeId);
		Player user = wellnessMonthDAO.getPlayerByName(playerName);
		Team userTeam = wellnessMonthDAO.getTeamById(user.getTeamId());
		WellnessContest userContest = wellnessMonthDAO.getContestById(userTeam.getContestId());
		session.setAttribute("user", user);
		session.setAttribute("userContest", userContest);
		session.setAttribute("userTeam", userTeam);
		return "redirect:/";
	}
}
