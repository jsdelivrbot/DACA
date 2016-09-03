package models;

import java.util.List;

public class SolutionDTO {

	private String body;
	private List<String> outputs;
	
	public String getBody() {
		return this.body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public List<String> getOutputs() {
		return this.outputs;
	}
	
	public void setOutputs(List<String> outputs) {
		this.outputs = outputs;
	}
	
}
