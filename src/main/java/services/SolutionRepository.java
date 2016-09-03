package services;

import org.springframework.data.repository.CrudRepository;

import models.Solution;
import models.User;

public interface SolutionRepository extends CrudRepository<Solution, Long> {

	public Iterable<Solution> findBySubmitter(User submitter);
	
}
