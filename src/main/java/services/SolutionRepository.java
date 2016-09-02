package services;

import org.springframework.data.repository.CrudRepository;

import models.Solution;
	
public interface SolutionRepository extends CrudRepository<Solution, Long> {

}
