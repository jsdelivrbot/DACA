package services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import models.Solution;
import models.User;

@Component
public class SolutionRepository extends services.Repository<String, Solution> {

	public boolean isSolved(Long problemId, User user) {
		List<Solution> solutions = super.findAll();
		for (Solution s : solutions) {
			if (s.getProblemId().equals(problemId) && s.getSubmitter().equals(user.getEmail())) {
				return s.isSolved();
			}
		}
		return false;
	}

	public int getNumSubmitters() {
		Set<String> submitters = new HashSet<>();
		List<Solution> solutions = super.findAll();
		for (Solution s : solutions) {
			submitters.add(s.getSubmitter());
		}
		return submitters.size();
	}

	public Object getNumSolved(String user) {
		int solved = 0;
		List<Solution> solutions = super.findAll();
		for (Solution s : solutions) {
			if (s.getSubmitter().equals(user) && s.isSolved()) {
				solved++;
			}
		}
		return solved;
	}

}
