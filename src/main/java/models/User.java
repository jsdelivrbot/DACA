package models;

import java.util.regex.Pattern;

public class User extends Model<String> {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
	public static final Pattern VALID_PASSWORD_REGEX =
			Pattern.compile("^.{6,20}$");
	
	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private boolean isAdmin;
	
	public User() {
		
	}
	
	public User(String email, String password) {
		if (email == null || !VALID_EMAIL_ADDRESS_REGEX.matcher(email).matches()) {
			throw new InvalidFieldException("email", "Invalid e-mail.");
		} else if (password == null || !VALID_PASSWORD_REGEX.matcher(password).matches()) {
			throw new InvalidFieldException("password", "Password must be between 6 and 20 characters.");
		}
		setEmail(email);
		setPassword(password);
	}
	
	
	public String getEmail() {
		return this.getId();
	}
	
	public void setEmail(String email) {
		this.setId(email);
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return this.isAdmin;
	}
	
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
}
