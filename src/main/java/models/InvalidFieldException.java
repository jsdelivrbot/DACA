package models;

import java.util.HashMap;
import java.util.Map;

public class InvalidFieldException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	protected String field;
	
	public InvalidFieldException(String field, String reason) {
		super(reason);
		this.field = field;
	}
	
	public String getField() {
		return this.field;
	}
	
	public String getReason() {
		return this.getMessage();
	}
	
	public Map<String, String> toMap() {
		Map<String, String> m = new HashMap<>();
		m.put("error", "InvalidModel");
		m.put("field", getField());
		m.put("reason", getReason());
		return m;
	}
	
}
