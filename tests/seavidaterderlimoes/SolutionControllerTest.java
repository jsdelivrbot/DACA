package seavidaterderlimoes;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import bootwildfly.Application;
import models.User;
import services.ProblemRepository;
import services.SolutionRepository;
import services.TokenService;

@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SolutionControllerTest {
 
	@Autowired
	private TokenService tokenService;
	@Autowired
	private ProblemRepository problemRepository;
	@Autowired
	private SolutionRepository solutionRepository;
	
	@Value("${local.server.port}")
	private int port;
	
	private String token1;
	private String location;
	
	@Before
	public void setUp() {
		this.token1 = this.tokenService.generateToken(new User("abner@gmail.com", "123456"));
		location = given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("name", "potencia")
					.put("description", "defina potencia")
					.put("tip", "eleve ao quadrado")
					.toString())
		.when()
			.port(this.port)
			.post("problem")
		.then()
			.assertThat().statusCode(is(201))
		.extract().header("Location");
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expectedOutput", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expectedOutput", "4"))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/test")
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("name", "potencia")
					.put("description", "defina potencia")
					.put("tip", "eleve ao quadrado")
					.put("published", true)
					.toString())
		.when()
			.port(this.port)
			.put(location)
		.then()
			.assertThat().statusCode(is(204));
	}

	@After
	public void tearDown() {
		this.solutionRepository.deleteAll();
		this.problemRepository.deleteAll();
	}
	
	@Test
	public void testSolveCorrect() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
		.when()
			.port(this.port)
			.get(location)
		.then()
			.assertThat().statusCode(is(200)).and().body("solved", is(false));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("outputs", new JSONArray().put("1").put("4"))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/solution")
		.then()
			.assertThat().statusCode(is(200)).and().body("solved", is(true));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
		.when()
			.port(this.port)
			.get(location)
		.then()
			.assertThat().statusCode(is(200)).and().body("solved", is(true));
	}
	
	@Test
	public void testSolveFail() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("outputs", new JSONArray().put("1").put("3"))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/solution")
		.then()
			.assertThat().statusCode(is(200)).and().body("solved", is(false));
	}
	
	@Test
	public void testSolveUnpublishedProblem() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("name", "potencia")
					.put("description", "defina potencia")
					.put("tip", "eleve ao quadrado")
					.put("published", false)
					.toString())
		.when()
			.port(this.port)
			.put(location)
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("outputs", new JSONArray().put("1").put("4"))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/solution")
		.then()
			.assertThat().statusCode(is(400));
	}
	
	@Test
	public void testSolveWrongParameters() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("outputs", new JSONArray().put("1").put("4").put("5"))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/solution")
		.then()
			.assertThat().statusCode(is(400));
	}
	
}
