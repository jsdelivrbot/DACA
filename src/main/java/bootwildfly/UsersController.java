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
import services.AuthException;
import services.TokenService;
import services.UserRepository;

@RestController
public class UsersController {

	@Autowired
	private UserRepository repository;
	@Autowired
	private TokenService tokenService;
	
	@RequestMapping(value="users", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public ResponseEntity<String> createUser(@RequestBody User request) {
		User user = new User(request.getEmail(), request.getPassword());
		if (repository.findById(user.getEmail()) != null) {
			throw new InvalidFieldException("email", "E-mail is already being used.");
		}
		repository.save(user);
		return new HttpResponse()
				.status(HttpStatus.CREATED)
				.put("token", tokenService.generateToken(user))
				.build();
	}
	
	@RequestMapping(value="auth", method=RequestMethod.POST)
	public ResponseEntity<String> createAuthentication(@RequestBody User user) {
		User found;
		if ((found = repository.findById(user.getEmail())) == null || 
			!found.getPassword().equals(user.getPassword())) {
			throw new AuthException("User or password don't match.");
		}
		return new HttpResponse()
				.put("token", tokenService.generateToken(user))
				.build();
	}

}
