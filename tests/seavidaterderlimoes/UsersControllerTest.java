package seavidaterderlimoes;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import org.json.JSONObject;
import org.junit.After;
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
import services.UserRepository;

@SpringApplicationConfiguration(classes=Application.class)
@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsersControllerTest {

	@Value("${local.server.port}")
	private int port;
	@Autowired
	private UserRepository repository;
	
	@After
	public void tearDown() {
		this.repository.clear();
	}
	
	@Test
	public void testCreateNewAccount() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "user@gmail.com")
					.put("password", "123456")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(201)).body(containsString("token"));
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "user@gmail.com")
					.put("password", "123456")
					.toString())
			.when()
				.port(this.port)
				.post("auth")
			.then()
				.assertThat().statusCode(is(200)).body(containsString("token"));
	}
	
	@Test
	public void testCreateAlreadyExistingAccount() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "test@gmail.com")
					.put("password", "abc1234")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(201)).body(containsString("token"));
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "test@gmail.com")
					.put("password", "abc1234")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(400));
	}
	
	@Test
	public void testCreateInvalidUser() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "13i213o2n")
					.put("password", "123456")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("email"));
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("password", "123456")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("email"));
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "valid.email@gmail.com")
					.put("password", "123")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("password"));
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "valid.email@gmail.com")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(400)).and().body("field", is("password"));
		given()
			.accept("application/json")
			.contentType("application/json")
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(400)).and().body(is(""));
	}
	
	@Test
	public void testInvalidLogin() {
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "maria@gmail.com")
					.put("password", "123456")
					.toString())
		.when()
			.port(this.port)
			.post("users")
		.then()
			.assertThat().statusCode(is(201));
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "maria@gmail.com")
					.put("password", "1234567")
					.toString())
		.when()
			.port(this.port)
			.post("auth")
		.then()
			.assertThat().statusCode(is(401));
		given()
			.accept("application/json")
			.contentType("application/json")
			.body(new JSONObject()
					.put("email", "maria2@gmail.com")
					.put("password", "123456")
					.toString())
		.when()
			.port(this.port)
			.post("auth")
		.then()
			.assertThat().statusCode(is(401));
	}
	
}
