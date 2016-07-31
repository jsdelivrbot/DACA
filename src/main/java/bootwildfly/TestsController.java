package bootwildfly;

import java.util.ArrayList;
import java.util.List;

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
import services.TokenService;

@RestController
@RequestMapping(value="problems/{problemId}/tests", produces="application/json")
public class TestsController {

	@Autowired
	private ProblemRepository repository;
	@Autowired
	private TokenService tokenService;
	
	private Problem getProblem(Long problemId) {
		Problem problem = repository.findById(problemId);
		if (problem == null) {
			throw new ResourceNotFoundException();
		}
		return problem;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Test> listTests(@PathVariable("problemId") Long problemId,
			@RequestHeader(value="Authorization", required=false) String token) {
		User requestor = tokenService.getUser(token);
		Problem problem = getProblem(problemId);
		List<Test> tests = problem.getTests();
		for (Test test : tests) {
			if (!problem.isTestOutputVisible(test, requestor)) {
				test.setExpectedOutput(null);
			}
		}
		return tests;
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(method=RequestMethod.PUT)
	public void insertTests(@PathVariable("problemId") Long problemId,
			@RequestBody List<Test> tests, @RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = getProblem(problemId);
		if (!requestor.isAdmin() && !requestor.getEmail().equals(problem.getOwner())) {
			throw new AuthException("You don't have permission to perform this action.");
		} else if (tests.isEmpty()) {
			throw new InvalidFieldException("tests", "Tests cannot be empty.");
		}
		List<Test> verifiedTests = new ArrayList<Test>();
		for (Test t : tests) {
			verifiedTests.add(new Test(t.getName(), t.getTip(), t.getInput(), t.getExpectedOutput(), t.isPublic()));
		}
		problem.setTests(verifiedTests);
		repository.save(problem);
	}
	
}
