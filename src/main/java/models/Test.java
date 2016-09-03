package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Test {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String tip;
	
	@Column(nullable = false)
	private String input;
	
	@Column(nullable = false)
	@JsonProperty("expected_output")
	private String expectedOutput;
	
	@Column(nullable = false)
	@JsonProperty("public")
	private boolean isPublic;
	
	protected Test() {
		
	}
	
	public Test(String name, String tip, String input, String expectedOutput, boolean isPublic) {
		setName(name);
		setTip(tip);
		setInput(input);
		setExpectedOutput(expectedOutput);
		setPublic(isPublic);
	}

	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTip() {
		return this.tip;
	}
	
	public void setTip(String tip) {
		this.tip = tip;
	}
	
	public String getInput() {
		return this.input;
	}
	
	public void setInput(String input) {
		if (input == null || input.trim().equals("")) {
			throw new InvalidFieldException("input", "Input cannot be empty.");
		}
		this.input = input;
	}
	
	public String getExpectedOutput() {
		return this.expectedOutput;
	}
	
	public void setExpectedOutput(String expectedOutput) {
		this.expectedOutput = expectedOutput;
	}
	
	public boolean isPublic() {
		return this.isPublic;
	}
	
	public void setPublic(boolean isPublic) {
		if (expectedOutput == null || expectedOutput.trim().equals("")) {
			throw new InvalidFieldException("expected_output", "Expected output cannot be empty.");
		}
		this.isPublic = isPublic;
	}
	
}
