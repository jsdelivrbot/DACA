package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Problem extends Model<Long> {

	private static final long serialVersionUID = 1L;
	
	private String owner;
	private String name;
	private String description;
	private String tip;
	private boolean published;
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("created")
	private Date creationDate; 
	private List<Test> tests;
	@JsonProperty("solved")
	private Boolean isSolved;
	
	public Problem() {
		this.tests = new ArrayList<>();
	}
	
	// copy constructor
	public Problem(Problem other) {
		this.id = other.id;
		this.owner = other.owner;
		this.name = other.name;
		this.description = other.description;
		this.tip = other.tip;
		this.published = other.published;
		this.creationDate = other.creationDate;
		this.tests = new ArrayList<>();
		for (Test t : other.tests) {
			this.tests.add(new Test(t));
		}
	}
	
	public Problem(User owner, String name, String description, String tip, boolean isPublished) {
		if (owner == null) {
			throw new InvalidFieldException("owner", "Owner can't be null.");
		} else if (name == null || name.trim().equals("")) {
			throw new InvalidFieldException("name", "Name cannot be empty.");
		}
		setOwner(owner.getId());
		setName(name);
		setDescription(description);
		setTip(tip);
		setPublished(isPublished);
		setTests(new ArrayList<Test>());
	}

	public String getOwner() {
		return this.owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getTip() {
		return this.tip;
	}
	
	public void setTip(String tip) {
		this.tip = tip;
	}
	
	public boolean isPublished() {
		return this.published;
	}
	
	public void setPublished(boolean published) {
		this.published = published;
	}
	
	public Date getCreationDate() {
		return this.creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public List<Test> getTests() {
		return this.tests;
	}
	
	public void setTests(List<Test> tests) {
		this.tests = tests;
	}
	
	public Boolean isSolved() {
		return this.isSolved;
	}
	
	public void setSolved(boolean isSolved) {
		this.isSolved = isSolved;
	}

	public boolean isTestOutputVisible(Test test, User requestor) {
		return test.isPublic() || (requestor != null && this.getOwner().equals(requestor.getEmail()));
	}
	
}
