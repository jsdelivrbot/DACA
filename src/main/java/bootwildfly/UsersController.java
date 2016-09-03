package bootwildfly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import models.InvalidFieldException;
import models.User;
import models.UserDTO;
import services.TokenService;
import services.UserRepository;

@RestController
public class UsersController {

	@Autowired
	private UserRepository repository;
	@Autowired
	private TokenService tokenService;
	
	private User getUser(String email) {
		Iterable<User> users = repository.findByEmail(email);
		for (User user : users) {
			return user;
		}
		return null;
	}
	
	@RequestMapping(value="users", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
		User user = new User(userDTO.getEmail(), userDTO.getPassword());
		if (getUser(userDTO.getEmail()) != null) {
			throw new InvalidFieldException("email", "E-mail is already being used.");
		}   
		repository.save(user);
		return new HttpResponse()
				.status(HttpStatus.CREATED)
				.put("token", tokenService.generateToken(user))
				.build();
	}
	
	@RequestMapping(value="auth", method=RequestMethod.POST)
	public ResponseEntity<String> createAuthentication(@RequestBody UserDTO userDTO) {
		User found = getUser(userDTO.getEmail());
		if (found == null || !found.getPassword().equals(userDTO.getPassword())) {
			throw new AuthException("User or password don't match.");
		}
		return new HttpResponse()
				.put("token", tokenService.generateToken(found))
				.build();
	}

}
