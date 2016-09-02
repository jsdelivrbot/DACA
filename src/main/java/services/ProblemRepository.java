package services;

import org.springframework.data.repository.CrudRepository;

import models.Problem;

public interface ProblemRepository extends CrudRepository<Problem, Long> {

}
