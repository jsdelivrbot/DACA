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
	
	public boolean isSolved(Problem problem, User user) {
		Iterable<Solution> solutions = repository.findAll();
		for (Solution s : solutions) {
			if (s.getProblem().equals(problem) && s.getSubmitter().equals(user)) {
				return s.isSolved();
			}
		}
		return false;
	}

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
		Iterable<Solution> solutions = repository.findAll();
		for (Solution s : solutions) {
			if (s.getSubmitter().equals(user) && s.isSolved()) {
				solved++;
			}
		}
		return solved;
	}
	
}
