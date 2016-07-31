package models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Model<T extends Comparable<T>> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected T id;
	
	public Model() {
		
	}
	
	public Model(T id) {
		if (id == null) {
			throw new InvalidFieldException("id", "Invalid id.");
		}
		setId(id);
	}
	
	public T getId() {
		return this.id;
	}
	
	public void setId(T id) {
		this.id = id;
	}
	
}
