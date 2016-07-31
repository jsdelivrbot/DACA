package seavidaterderlimoes;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

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
import services.TokenService;

@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestsControllerTest {
 
	@Autowired
	private TokenService tokenService;
	@Autowired
	private ProblemRepository repository;
	
	@Value("${local.server.port}")
	private int port;
	
	private String token1;
	private String token2;
	
	@Before
	public void setUp() {
		this.token1 = this.tokenService.generateToken(new User("abner@gmail.com", "123456"));
		this.token2 = this.tokenService.generateToken(new User("default@gmail.com", "123456"));
		given()
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
			.assertThat().statusCode(is(201));
	}
	
	@After
	public void tearDown() {
		this.repository.clear();
	}
	
	@Test
	public void testCreateTests() {
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get("problems/0/tests")
		.then()
			.assertThat().statusCode(is(200)).and().body(is("[]"));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expected_output", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expected_output", "4"))
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0/tests")
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
		.when()
			.port(this.port)
			.get("problems/0/tests")
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("input", hasItems("1", "2")).and()
				.body("expected_output", hasItems("1", "4"));
	}
	
	@Test
	public void testCreateInvalidTests() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "")
							.put("expected_output", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expected_output", "4"))
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0/tests")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("input"));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expected_output", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expected_output", ""))
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0/tests")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("expected_output"));
	}
	
	@Test
	public void testCreateTestsNotAuthorized() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token2)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expected_output", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expected_output", "4"))
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0/tests")
		.then()
			.assertThat().statusCode(is(401));
	}
	
	@Test
	public void testNotPublicTests() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expected_output", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expected_output", "4"))
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0/tests")
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get("problems/0/tests")
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("input", hasItems("1", "2")).and()
				.body("expected_output", not(hasItems("1", "4")));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expected_output", "1")
							.put("public", true))
					.put(new JSONObject()
							.put("input", "2")
							.put("expected_output", "4")
							.put("public", true))
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0/tests")
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get("problems/0/tests")
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("input", hasItems("1", "2")).and()
				.body("expected_output", hasItems("1", "4"));
	}
	
	@Test
	public void testPublishWithTests() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expected_output", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expected_output", "4"))
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0/tests")
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("name", "testing")
					.put("published", true)
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0")
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get("problems/0")
		.then()
			.assertThat().statusCode(is(200)).and().body("published", is(true));
	}
	
	@Test
	public void testPublishWithoutTests() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONObject()
					.put("name", "testing")
					.put("published", true)
					.toString())
		.when()
			.port(this.port)
			.put("/problems/0")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("published"));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get("problems/0")
		.then()
			.assertThat().statusCode(is(200)).and().body("published", is(false));
	}
	
}
