package services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import models.Problem;

@Component
public class ProblemRepository extends services.Repository<Long, Problem> {

	@Override
	public Problem findById(Long id) {
		Problem p = super.findById(id);
		if (p != null) {
			return new Problem(p);
		}
		return p;
	}
	
	@Override
	public List<Problem> findAll() {
		List<Problem> problems = super.findAll();
		List<Problem> copies = new ArrayList<Problem>();
		for (Problem p : problems) {
			copies.add(new Problem(p));
		}
		return copies;
	}

	public int getNumPublishedProblems() {
		int count = 0;
		List<Problem> problems = super.findAll();
		for (Problem p : problems) {
			if (p.isPublished()) count++;
		}
		return count;
	}

}
