package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Test extends Model<Long> {

	private static final long serialVersionUID = 1L;

	private String name;
	private String tip;
	private String input;
	@JsonProperty("expected_output")
	private String expectedOutput;
	@JsonProperty("public")
	private boolean isPublic;
	
	public Test() {
		
	}
	
	public Test(String name, String tip, String input, String expectedOutput, boolean isPublic) {
		if (input == null || input.trim().equals("")) {
			throw new InvalidFieldException("input", "Input cannot be empty.");
		} else if (expectedOutput == null || expectedOutput.trim().equals("")) {
			throw new InvalidFieldException("expected_output", "Expected output cannot be empty.");
		} 
		setName(name);
		setTip(tip);
		setInput(input);
		setExpectedOutput(expectedOutput);
		setPublic(isPublic);
	}
	
	// copy constructor
	public Test(Test other) {
		this.id = other.id;
		this.name = other.name;
		this.tip = other.tip;
		this.input = other.input;
		this.expectedOutput = other.expectedOutput;
		this.isPublic = other.isPublic;
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
		this.isPublic = isPublic;
	}
	
}
