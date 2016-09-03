package bootwildfly;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import models.InvalidFieldException;
import models.InvalidSolutionException;

@EnableWebMvc
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(InvalidFieldException.class)
	@ResponseBody
	public ResponseEntity<String> handleError(InvalidFieldException exception) {
		return new HttpResponse()
				.status(HttpStatus.BAD_REQUEST)
				.put("field", exception.getField())
				.put("reason", exception.getReason())
				.build();
	}

	@ExceptionHandler(InvalidSolutionException.class)
	@ResponseBody
	public ResponseEntity<String> handleErrorSolution(InvalidSolutionException exception) {
		return new HttpResponse()
				.status(HttpStatus.BAD_REQUEST)
				.put("reason", exception.getMessage())
				.build();
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseBody
	public String handleNotFound(ResourceNotFoundException exception) {
		return "";
	}
	
	@ExceptionHandler(AuthException.class)
	@ResponseBody
	public ResponseEntity<String> handleAuthError(AuthException exception) {
		return new HttpResponse()
				.status(HttpStatus.UNAUTHORIZED)
				.put("reason", exception.getMessage())
				.build();
	}
	
}
