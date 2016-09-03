package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Solution {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private User submitter;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private Problem problem;
	
	@Column
	private String body;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.ALL)
	private List<Output> outputs;
	
	protected Solution() {
		
	}

	public Solution(User submitter, Problem problem, String body) {
		setSubmitter(submitter);
		setProblem(problem);
		setBody(body);
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getSubmitter() {
		return this.submitter;
	}
	
	public void setSubmitter(User submitter) {
		this.submitter = submitter;
	}
	
	public Problem getProblem() {
		return this.problem;
	}
	
	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public String getBody() {
		return this.body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}

	public List<Output> getOutputs() {
		return this.outputs;
	}
	
	public void setOutputs(List<Output> outputs) {
		this.outputs = outputs;
	}
	
	public boolean isSolved() {
		for (Output o : this.outputs) {
			if (!o.isCorrect()) {
				return false;
			}
		}
		return true;
	}
	
}
