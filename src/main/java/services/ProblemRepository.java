package services;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import models.Problem;

public interface ProblemRepository extends PagingAndSortingRepository<Problem, Long> {

	@Cacheable
	public List<Problem> findByPublished(boolean published, Pageable pageable);
	
}
