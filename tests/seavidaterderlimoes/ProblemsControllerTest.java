package seavidaterderlimoes;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

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
import services.TokenService;

@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProblemsControllerTest {
  
	@Autowired
	private TokenService tokenService;
	@Autowired
	private ProblemRepository repository;
	
	@Value("${local.server.port}")
	private int port;
	
	private String token1;
	private String token2;
	private String token3;
	
	@Before
	public void setUp() {
		this.token1 = this.tokenService.generateToken(new User("abner@gmail.com", "123456"));
		this.token2 = this.tokenService.generateToken(new User("vitor@gmail.com", "123456"));
		this.token3 = this.tokenService.generateToken(new User("default@gmail.com", "123456"));
	}
	
	@After
	public void tearDown() {
		this.repository.deleteAll();
	}
	
	@Test
	public void testCreateNewProblem() {
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get("problems")
		.then()
			.assertThat().statusCode(is(200)).and().body(is("[]"));
		String location = given()
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
			.post("problems")
		.then()
			.assertThat().statusCode(is(201))
		.extract().header("Location");
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get("problems")
		.then()
			.assertThat().statusCode(is(200)).and().body(is("[]"));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get(location)
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("name", is("potencia"))
				.body("description", is("defina potencia"))
				.body("tip", is("eleve ao quadrado"))
				.body("published", is(false))
				.body("solved", is(false));
	}
	
	@Test
	public void testCreateInvalidProblem() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("name", "")
					.put("description", "defina potencia")
					.put("tip", "eleve ao quadrado")
					.toString())
		.when()
			.port(this.port)
			.post("problems")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("name"));
	}
	
	@Test
	public void testUpdateProblem() {
		String location = given()
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
			.post("problems")
		.then()
			.assertThat().statusCode(is(201))
		.extract().header("Location");
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("name", "nova potencia")
					.put("description", "nova defina potencia")
					.put("tip", "nova eleve ao quadrado")
					.toString())
		.when()
			.port(this.port)
			.put(location)
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get(location)
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("name", is("nova potencia"))
				.body("description", is("nova defina potencia"))
				.body("tip", is("nova eleve ao quadrado"))
				.body("published", is(false))
				.body("solved", is(false));
	}
	
	@Test
	public void testUpdateNotAuthorized() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token3)
			.body(new JSONObject()
					.put("name", "nova potencia")
					.put("description", "nova defina potencia")
					.put("tip", "nova eleve ao quadrado")
					.toString())
		.when()
			.port(this.port)
			.put("problems/1")
		.then()
			.assertThat().statusCode(is(404));
		String location = given()
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
			.post("problems")
		.then()
			.assertThat().statusCode(is(201))
		.extract().header("Location");
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token3)
			.body(new JSONObject()
					.put("name", "nova potencia")
					.put("description", "nova defina potencia")
					.put("tip", "nova eleve ao quadrado")
					.toString())
		.when()
			.port(this.port)
			.put(location)
		.then()
			.assertThat().statusCode(is(401));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token2)
			.body(new JSONObject()
					.put("name", "nova potencia")
					.put("description", "nova defina potencia")
					.put("tip", "nova eleve ao quadrado")
					.toString())
		.when()
			.port(this.port)
			.put(location)
		.then()
			.assertThat().statusCode(is(204));
	}
	
	@Test
	public void testDeleteProblem() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
		.when()
			.port(this.port)
			.delete("problems/1")
		.then()
			.assertThat().statusCode(is(404));
		String location = given()
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
			.post("problems")
		.then()
			.assertThat().statusCode(is(201))
		.extract().header("Location");
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get(location)
		.then()
			.assertThat().statusCode(is(200));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
		.when()
			.port(this.port)
			.delete(location)
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get(location)
		.then()
			.assertThat().statusCode(is(404));
	}
	
	@Test
	public void testDeleteNotAuthorized() {
		String location = given()
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
			.post("problems")
		.then()
			.assertThat().statusCode(is(201))
		.extract().header("Location");
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token3)
		.when()
			.port(this.port)
			.delete(location)
		.then()
			.assertThat().statusCode(is(401));
	}
	
}
