package bootwildfly;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

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
import models.TestDTO;
import models.User;
import services.ProblemRepository;
import services.TokenService;

@RestController
@RequestMapping(value="problems/{problemId}/tests", produces="application/json")
public class TestsController {

	@Autowired
	private ProblemRepository repository;
	@Autowired
	private TokenService tokenService;
	
	private Problem getProblem(Long problemId) {
		Problem problem = repository.findOne(problemId);
		if (problem == null) {
			throw new ResourceNotFoundException();
		}
		return problem;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public List<TestDTO> listTests(@PathVariable("problemId") Long problemId,
			@RequestHeader(value="Authorization", required=false) String token) {
		User requestor = tokenService.getUser(token);
		Problem problem = getProblem(problemId);
		List<TestDTO> tests = new ArrayList<>();
		for (Test test : problem.getTests()) {
			TestDTO t = new TestDTO();
			t.setName(test.getName());
			t.setTip(test.getTip());
			t.setInput(test.getInput());
			if (problem.isTestOutputVisible(test, requestor)) {
				t.setExpectedOutput(test.getExpectedOutput());
			}
			tests.add(t);
		}
		return tests;
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(method=RequestMethod.PUT)
	@Transactional
	public void insertTests(@PathVariable("problemId") Long problemId,
			@RequestBody List<TestDTO> testsDTO, @RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = getProblem(problemId);
		if (!requestor.isAdmin() && !requestor.equals(problem.getOwner())) {
			throw new AuthException("You don't have permission to perform this action.");
		} else if (testsDTO.isEmpty()) {
			throw new InvalidFieldException("tests", "Tests cannot be empty.");
		}
		problem.getTests().clear();
		for (TestDTO t : testsDTO) {
			Test test = new Test(t.getName(), t.getTip(), t.getInput(), t.getExpectedOutput(), t.isPublic());
			problem.getTests().add(test);
		}
		repository.save(problem);
	}
	
}
