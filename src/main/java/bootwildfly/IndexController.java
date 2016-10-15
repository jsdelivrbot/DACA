package bootwildfly;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@RestController
public class IndexController {

	@RequestMapping(value="/", method=RequestMethod.GET)
	public void index(HttpServletResponse response) {
		response.setStatus(HttpStatus.PERMANENT_REDIRECT.value());
		response.setHeader("Location", "/index.html");
	}
	
}
