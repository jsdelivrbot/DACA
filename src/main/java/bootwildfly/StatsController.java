package bootwildfly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import models.User;
import services.ProblemService;
import services.SolutionService;
import services.UserRepository;

@RestController
public class StatsController {
	
	@Autowired
	private ProblemService problemService;
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(value="stats", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<String> getGlobalStats() {
		return new HttpResponse()
				.put("problems", problemService.getNumPublishedProblems())
				.put("submitters", solutionService.getNumSubmitters())
				.build();
	}
	
	@RequestMapping(value="/user/{email}/stats", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<String> getUserStats(@PathVariable("email") String email) {
		Iterable<User> users = userRepository.findByEmail(email);
		for (User user : users) {
			return new HttpResponse()
				.put("solved", solutionService.getNumSolved(user))
				.build();
		}
		throw new ResourceNotFoundException();
	}
	
}
