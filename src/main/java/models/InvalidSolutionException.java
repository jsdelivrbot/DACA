package models;

public class InvalidSolutionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidSolutionException(String message) {
		super(message);
	}
	
}
