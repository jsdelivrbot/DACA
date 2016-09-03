package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class ProblemDTO {

	private User owner;
	@JsonProperty("owner_email")
	private String ownerEmail;
	private String name;
	private String description;
	private String tip;
	private boolean published;
	private boolean solved;
	
	public ProblemDTO() {
		
	}
	
	public User getOwner() {
		return this.owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public String getOwnerEmail() {
		return this.ownerEmail;
	}
	
	public void setOwnerEmail(String email) {
		this.ownerEmail = email;
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
	
	public boolean isSolved() {
		return this.solved;
	}
	
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	
}
