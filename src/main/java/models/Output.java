package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Output {

	@Id
	private Long id;
	@Column
	@ManyToOne(fetch = FetchType.LAZY)
	private Test test;
	@Column(nullable = false)
	private String output;
	
	protected Output() {
		
	}
	
	public Test getTest() {
		return this.test;
	}
	
	public void setTest(Test test) {
		this.test = test;
	}
	
	public String getOutput() {
		return this.output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public boolean isCorrect() {
		return this.test.getExpectedOutput().equals(this.output);
	}
	
}
