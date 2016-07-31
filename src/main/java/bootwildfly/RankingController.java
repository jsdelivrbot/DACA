package bootwildfly;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="ranking", produces="application/json")
public class RankingController {

	@RequestMapping(method=RequestMethod.GET)
	public String getRanking() {
		return "";
	}
	
}
