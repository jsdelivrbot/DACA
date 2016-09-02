package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import models.Problem;

@Component
public class ProblemService {
	
	@Autowired
	private ProblemRepository repository;
	
	public int getNumPublishedProblems() {
		int count = 0;
		Iterable<Problem> problems = repository.findAll();
		for (Problem p : problems) {
			if (p.isPublished()) count++;
		}
		return count;
	}
	
}
