package bootwildfly;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import models.InvalidFieldException;
import models.Problem;
import models.Test;
import models.User;
import services.AuthException;
import services.ProblemRepository;
import services.ResourceNotFoundException;
import services.SolutionRepository;
import services.TokenService;

@RestController
@RequestMapping(value="problems", produces="application/json")
public class ProblemsController {

	@Autowired
	private ProblemRepository problemRepository;
	@Autowired
	private SolutionRepository solutionRepository;
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Problem> listProblems(@RequestHeader(value="Authorization", required=false) String token) {
		User requestor = tokenService.getUser(token);
		List<Problem> problems = problemRepository.findAll();
		for (Problem problem : problems) {
			problem.setTests(null);
			problem.setTip(null);
			if (requestor != null) {
				problem.setSolved(solutionRepository.isSolved(problem.getId(), requestor));
			} else {
				problem.setSolved(false);
			}
		}
		return problems;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method=RequestMethod.POST)
	public void createProblem(@RequestBody Problem request, @RequestHeader(value="Authorization") String token,
			HttpServletResponse response) {
		User requestor = tokenService.validateToken(token);
		Problem problem = new Problem(requestor, request.getName(), request.getDescription(), request.getTip(), false);
		problem.setCreationDate(Calendar.getInstance().getTime());
		problemRepository.save(problem);
		response.setHeader("Location", "/problems/" + problem.getId());
	}
	 
	@RequestMapping(value="/{problemId}", method=RequestMethod.GET)
	public Problem getProblem(@PathVariable("problemId") Long problemId, 
			@RequestHeader(value="Authorization", required=false) String token) {
		User requestor = tokenService.getUser(token);
		Problem found = problemRepository.findById(problemId);
		if (found == null) {
			throw new ResourceNotFoundException();
		} else if (requestor != null) {
			found.setSolved(solutionRepository.isSolved(problemId, requestor));
		} else {
			found.setSolved(false);
		}
		for (Test t : found.getTests()) {
			if (!found.isTestOutputVisible(t, requestor)) {
				t.setExpectedOutput(null);
			}
		}
		return found;
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value="/{problemId}", method=RequestMethod.PUT)
	public void updateProblem(@PathVariable("problemId") Long problemId, @RequestBody Problem request,
			@RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = problemRepository.findById(problemId);
		if (problem == null) {
			throw new ResourceNotFoundException();
		} else if (!requestor.isAdmin() && !problem.getOwner().equals(requestor.getEmail())) {
			throw new AuthException("You don't have permission to perform this action.");
		}
		Problem newProblem = new Problem(requestor, request.getName(), request.getDescription(), 
				request.getTip(), request.isPublished());
		newProblem.setId(problem.getId());
		newProblem.setCreationDate(problem.getCreationDate());
		newProblem.setTests(problem.getTests());
		if (newProblem.isPublished() && newProblem.getTests().isEmpty()) {
			throw new InvalidFieldException("published", "You must create at least one test before publishing.");
		}
		problemRepository.save(newProblem);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value="/{problemId}", method=RequestMethod.DELETE)
	public void deleteProblem(@PathVariable("problemId") Long problemId, 
			@RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = problemRepository.findById(problemId); 
		if (problem == null) {
			throw new ResourceNotFoundException();
		} else if (!requestor.isAdmin() && !problem.getOwner().equals(requestor.getEmail())) {
			throw new AuthException("You don't have permission to perform this action.");
		}
		problemRepository.delete(problemId);
	}
	
}
