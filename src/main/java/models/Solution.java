package models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Solution extends Model<String> {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String submitter;
	@JsonIgnore
	private Long problemId;
	private String body;
	private List<Test> tests;
	private List<String> outputs;
	
	public Solution() {
		tests = new ArrayList<Test>();
		outputs = new ArrayList<String>();
	}
	
	public String getSubmitter() {
		return this.submitter;
	}
	
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	
	public Long getProblemId() {
		return this.problemId;
	}
	
	public void setProblemId(Long problemId) {
		this.problemId = problemId;
	}

	public String getBody() {
		return this.body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}

	public List<Test> getTests() {
		return this.tests;
	}
	
	public void setTests(List<Test> tests) {
		this.tests = tests;
	}
	
	public List<String> getOutputs() {
		return this.outputs;
	}
	
	public void setOutputs(List<String> outputs) {
		this.outputs = outputs;
	}
	
	public boolean isSolved() {
		for (int i = 0; i < this.tests.size(); i++) {
			if (!this.tests.get(i).getExpectedOutput().equals(this.outputs.get(i))) {
				return false;
			}
		}
		return true;
	}
	
}
