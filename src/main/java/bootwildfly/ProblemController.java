package bootwildfly;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import models.InvalidFieldException;
import models.Problem;
import models.ProblemDTO;
import models.Test;
import models.User;
import services.ProblemRepository;
import services.SolutionService;
import services.TokenService;

@RestController
@RequestMapping(value="problem", produces="application/json")
public class ProblemController {

	private static final int PAGE_SIZE = 20;
	
	@Autowired
	private SolutionService solutionService;
	@Autowired
	private TokenService tokenService;
	@Autowired
	private ProblemRepository problemRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<ProblemDTO> listProblems(@RequestHeader(value="Authorization", required=false) String token,
			@RequestParam(name="page", defaultValue="0") int page) {
		//User requestor = tokenService.getUser(token);
		Iterable<Problem> problems = problemRepository.findByPublished(true, new PageRequest(page, PAGE_SIZE));
		List<ProblemDTO> problemsDTO = new ArrayList<>();
		for (Problem problem : problems) {
			ProblemDTO p = new ProblemDTO();
			p.setOwnerEmail(problem.getOwner().getEmail());
			p.setName(problem.getName());
			p.setDescription(problem.getDescription());
			//p.setSolved(solutionService.isSolved(problem, requestor));
			p.setPublished(problem.isPublished());
			problemsDTO.add(p);
		}
		return problemsDTO;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(method=RequestMethod.POST)
	public void createProblem(@RequestBody ProblemDTO problemDTO, @RequestHeader(value="Authorization") String token,
			HttpServletResponse response) {
		User requestor = tokenService.validateToken(token);
		Problem problem = new Problem(requestor, problemDTO.getName(), problemDTO.getDescription(), problemDTO.getTip());
		problemRepository.save(problem);
		response.setHeader("Location", "/problem/" + problem.getId());
	} 
	 
	@RequestMapping(value="/{problemId}", method=RequestMethod.GET)
	public Problem getProblem(@PathVariable("problemId") Long problemId, 
			@RequestHeader(value="Authorization", required=false) String token) {
		User requestor = tokenService.getUser(token);
		Problem found = problemRepository.findOne(problemId);
		if (found == null) {
			throw new ResourceNotFoundException();
		} else if (requestor != null) {
			found.setSolved(solutionService.isSolved(found, requestor));
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
	public void updateProblem(@PathVariable("problemId") Long problemId, @RequestBody ProblemDTO problemDTO,
			@RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = problemRepository.findOne(problemId);
		if (problem == null) {
			throw new ResourceNotFoundException();
		} else if (!requestor.isAdmin() && !problem.getOwner().equals(requestor)) {
			throw new AuthException("You don't have permission to perform this action.");
		} else if (problemDTO.isPublished() && problem.getTests().isEmpty()) {
			throw new InvalidFieldException("published", "You must create at least one test before publishing.");
		}
		problem.setName(problemDTO.getName());
		problem.setDescription(problemDTO.getDescription());
		problem.setTip(problemDTO.getTip());
		problem.setPublished(problemDTO.isPublished());
		problemRepository.save(problem);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(value="/{problemId}", method=RequestMethod.DELETE)
	public void deleteProblem(@PathVariable("problemId") Long problemId, 
			@RequestHeader(value="Authorization") String token) {
		User requestor = tokenService.validateToken(token);
		Problem problem = problemRepository.findOne(problemId); 
		if (problem == null) {
			throw new ResourceNotFoundException();
		} else if (!requestor.isAdmin() && !problem.getOwner().equals(requestor)) {
			throw new AuthException("You don't have permission to perform this action.");
		}
		problemRepository.delete(problem);
	}
	
}
