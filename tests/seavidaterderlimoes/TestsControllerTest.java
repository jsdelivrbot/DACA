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
	private String location;
	
	@Before
	public void setUp() {
		this.token1 = this.tokenService.generateToken(new User("abner@gmail.com", "123456"));
		this.token2 = this.tokenService.generateToken(new User("default@gmail.com", "123456"));
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
	}
	
	@After
	public void tearDown() {
		this.repository.deleteAll();
	}
	
	@Test
	public void testCreateTests() {
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get(location + "/test")
		.then()
			.assertThat().statusCode(is(200)).and().body(is("[]"));
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
		.when()
			.port(this.port)
			.get(location + "/test")
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("input", hasItems("1", "2")).and()
				.body("expectedOutput", hasItems("1", "4"));
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
							.put("expectedOutput", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expectedOutput", "4"))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/test")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("input"));
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
							.put("expectedOutput", ""))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/test")
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
							.put("expectedOutput", "1"))
					.put(new JSONObject()
							.put("input", "2")
							.put("expectedOutput", "4"))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/test")
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
		.when()
			.port(this.port)
			.get(location + "/test")
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("input", hasItems("1", "2")).and()
				.body("expectedOutput", not(hasItems("1", "4")));
		given()
			.accept("application/json")
			.contentType("application/json")
			.header("Authorization", "Token " + this.token1)
			.body(new JSONArray()
					.put(new JSONObject()
							.put("input", "1")
							.put("expectedOutput", "1")
							.put("public", true))
					.put(new JSONObject()
							.put("input", "2")
							.put("expectedOutput", "4")
							.put("public", true))
					.toString())
		.when()
			.port(this.port)
			.put(location + "/test")
		.then()
			.assertThat().statusCode(is(204));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get(location + "/test")
		.then()
			.assertThat().statusCode(is(200)).and()
				.body("input", hasItems("1", "2")).and()
				.body("expectedOutput", hasItems("1", "4"));
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
					.put("name", "testing")
					.put("published", true)
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
			.put(location)
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("published"));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.get(location)
		.then()
			.assertThat().statusCode(is(200)).and().body("published", is(false));
	}
	
}
