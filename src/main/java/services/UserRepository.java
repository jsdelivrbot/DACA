package services;

import org.springframework.stereotype.Component;

import models.User;

@Component
public class UserRepository extends services.Repository<String, User> {

	public UserRepository() {
		User admin1 = new User("abner@gmail.com", "123456");
		admin1.setAdmin(true);
		save(admin1);
		User admin2 = new User("vitor@gmail.com", "123456");
		admin2.setAdmin(true);
		save(admin2);
		User default1 = new User("default@gmail.com", "123456");
		save(default1);
	}
	
	public User save(User model) {
		return this.data.put(model.getId(), model);
	}

}
