package services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import models.Problem;
import models.Solution;
import models.User;

@Component
public class SolutionService {
	
	@Autowired
	private SolutionRepository repository;
	
	public int getNumSubmitters() {
		Set<User> submitters = new HashSet<>();
		Iterable<Solution> solutions = repository.findAll();
		for (Solution s : solutions) {
			submitters.add(s.getSubmitter());
		}
		return submitters.size();
	}

	public Object getNumSolved(User user) {
		int solved = 0;
		Iterable<Solution> solutions = repository.findBySubmitter(user);
		for (Solution s : solutions) {
			if (s.isSolved()) {
				solved++;
			}
		}
		return solved;
	}

	public Solution getSubmitterSolution(User submitter, Long problemId) {
		Iterable<Solution> solutions = repository.findBySubmitter(submitter);
		for (Solution s : solutions) {
			if (s.getProblem().getId().equals(problemId)) {
				return s;
			}
		}
		return null;
	}

	public boolean isSolved(Problem problem, User user) {
		if (user == null) return false;
		Solution s = getSubmitterSolution(user, problem.getId());
		if (s == null) {
			return false;
		}
		return s.isSolved();
	}

}
