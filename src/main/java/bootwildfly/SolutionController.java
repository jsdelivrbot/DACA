package bootwildfly;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import models.InvalidSolutionException;
import models.Output;
import models.Problem;
import models.Solution;
import models.SolutionDTO;
import models.User;
import services.ProblemRepository;
import services.SolutionRepository;
import services.SolutionService;
import services.TokenService;

@RestController
@RequestMapping(value="problem/{problemId}/solution", produces="application/json")
public class SolutionController {

	@Autowired
	private ProblemRepository problemRepository;
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(method=RequestMethod.GET)
	public SolutionDTO getSolution(@PathVariable("problemId") Long problemId, @RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Solution solution = solutionService.getSubmitterSolution(requestor, problemId);
		if (solution == null) {
			throw new ResourceNotFoundException();
		}
		SolutionDTO solutionDTO = new SolutionDTO();
		solutionDTO.setBody(solution.getBody());
		solutionDTO.setOutputs(new ArrayList<String>());
		for (Output o : solution.getOutputs()) {
			solutionDTO.getOutputs().add(o.getOutput());
		}
		return solutionDTO;
	}
	
	@RequestMapping(method=RequestMethod.PUT)
	@Transactional
	public ResponseEntity<String> createSolution(@RequestBody SolutionDTO solutionDTO, 
			@PathVariable("problemId") Long problemId, @RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = problemRepository.findOne(problemId);
		if (problem == null) {
			throw new ResourceNotFoundException();
		} else if (!problem.isPublished()) {
			throw new InvalidSolutionException("Cannot solve an unpublished problem.");
		} else if (solutionDTO.getOutputs() == null || solutionDTO.getOutputs().size() != problem.getTests().size()) {
			throw new InvalidSolutionException("Outputs length differ from the number of tests.");
		}
		Solution solution = solutionService.getSubmitterSolution(requestor, problemId);
		if (solution == null) {
			solution = new Solution(requestor, problem, solutionDTO.getBody());
		} else {
			solution.setBody(solutionDTO.getBody());
		}
		List<Output> outputs = new ArrayList<>();
		for (int i = 0; i < problem.getTests().size(); i++) {
			Output output = new Output(problem.getTests().get(i), solutionDTO.getOutputs().get(i));
			outputs.add(output);
		}
		solution.setOutputs(outputs);
		solutionRepository.save(solution);
		return new HttpResponse()
				.put("solved", solution.isSolved())
				.build();
	}
	
}
