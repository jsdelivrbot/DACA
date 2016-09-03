package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Output {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Test test;
	
	@Column(nullable = false)
	private String output;
	
	protected Output() {
		
	}
	
	public Output(Test test, String output) {
		setTest(test);
		setOutput(output);
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
