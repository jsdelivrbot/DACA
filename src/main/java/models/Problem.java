package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Problem {

	@Id
	private Long id;
	
	@Column
	@ManyToOne
	private User owner;
	
	@Column(nullable = false)
	private String name;
	
	@Column
	private String description;
	
	@Column
	private String tip;
	
	@Column(nullable = false)
	private boolean published;
	
	@Column
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	@JsonProperty("created")
	private Date creationDate; 
	
	@Column
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private List<Test> tests;
	
	@Transient
	@JsonProperty("solved")
	private Boolean isSolved;
	
	protected Problem() {
		
	}
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getOwner() {
		return this.owner;
	}
	
	public void setOwner(User owner) {
		if (owner == null) {
			throw new InvalidFieldException("owner", "Owner can't be null.");
		}
		this.owner = owner;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		if (name == null || name.trim().equals("")) {
			throw new InvalidFieldException("name", "Name cannot be empty.");
		}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Problem other = (Problem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
