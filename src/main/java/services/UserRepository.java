package services;

import org.springframework.data.repository.CrudRepository;

import models.User;

public interface UserRepository extends CrudRepository<User, Long> {

	public Iterable<User> findByEmail(String email);
	
}
