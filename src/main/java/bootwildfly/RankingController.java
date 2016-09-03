package bootwildfly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import models.Score;
import models.User;
import services.SolutionService;
import services.UserRepository;

@RestController
@RequestMapping(value="ranking", produces="application/json")
public class RankingController {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private SolutionService solutionService;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Score> getRanking(@RequestParam(name="top", defaultValue="10") int top) {
		Iterable<User> users = repository.findAll();
		List<Score> scores = new ArrayList<>();
		for (User user : users) {
			int numSolved = solutionService.getNumSolved(user);
			Score score = new Score(user, numSolved);
			scores.add(score);
		}
		Collections.sort(scores);
		return scores.subList(0, top);
	}
	
}
