package bootwildfly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import services.ProblemRepository;
import services.SolutionRepository;

@RestController
public class StatsController {
	
	@Autowired
	private ProblemRepository problemRepository;
	@Autowired
	private SolutionRepository solutionRepository;
	
	@RequestMapping(value="stats", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<String> getGlobalStats() {
		return new HttpResponse()
				.put("problems", problemRepository.getNumPublishedProblems())
				.put("submitters", solutionRepository.getNumSubmitters())
				.build();
	}
	
	@RequestMapping(value="/users/{email}/stats", method=RequestMethod.GET, produces="application/json")
	public ResponseEntity<String> getUserStats(@PathVariable("email") String email) {
		return new HttpResponse()
				.put("solved", solutionRepository.getNumSolved(email))
				.build();
	}
	
}
