package zach.com.wellnessmonth.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
import zach.com.wellnessmonth.model.WellnessContest;
import zach.com.wellnessmonth.model.WellnessMonthDAO;

@Controller 
public class WellnessMonthController {
	
	@Autowired
	private WellnessMonthDAO wellnessMonthDAO;
	
	@GetMapping("/")
	public String displayGreeting(ModelMap modelMap) {
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
	
	@GetMapping("/register")
	public String displayRegistration() {
		return "register";
	}
	@PostMapping("/register")
	public String createNewPlayer(@RequestParam String playerName, @RequestParam int contestId, RedirectAttributes ra) {
		Player newPlayer = new Player();
		WellnessContest contest = wellnessMonthDAO.getContestById(contestId);
		newPlayer.setName(playerName);
		newPlayer.setTeamId(wellnessMonthDAO.getFreeAgentsByContest(contest).getId());
		wellnessMonthDAO.createPlayer(newPlayer);
		ra.addFlashAttribute("confirmation", "New Player Created! ID = "+ newPlayer.getId());
		return "redirect:/";
	}
}
