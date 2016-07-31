package bootwildfly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import models.InvalidSolutionException;
import models.Problem;
import models.Solution;
import models.User;
import services.ProblemRepository;
import services.ResourceNotFoundException;
import services.SolutionRepository;
import services.TokenService;

@RestController
@RequestMapping(value="problems/{problemId}/solution", produces="application/json")
public class SolutionController {

	@Autowired
	private ProblemRepository problemRepository;
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(method=RequestMethod.GET)
	public Solution getSolution(@PathVariable("problemId") Long problemId, @RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Solution solution = solutionRepository.findById(requestor.getEmail() + "_" + problemId);
		if (solution == null) {
			throw new ResourceNotFoundException();
		}
		return solution;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	public ResponseEntity<String> createSolution(@RequestBody Solution solution, 
			@PathVariable("problemId") Long problemId, @RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = problemRepository.findById(problemId);
		if (problem == null) {
			throw new ResourceNotFoundException();
		} else if (!problem.isPublished()) {
			throw new InvalidSolutionException("Cannot solve a unpublished problem.");
		}
		if (solution.getOutputs().size() != problem.getTests().size()) {
			throw new InvalidSolutionException("Outputs length differ from the number of tests.");
		}
		solution.setId(requestor.getEmail() + "_" + problem.getId());
		solution.setSubmitter(requestor.getEmail());
		solution.setProblemId(problemId);
		solution.setTests(problem.getTests());
		solutionRepository.save(solution);
		return new HttpResponse()
				.put("solved", solution.isSolved())
				.build();
	}
	
}
