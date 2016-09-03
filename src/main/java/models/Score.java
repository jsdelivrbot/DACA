package models;

public class Score implements Comparable<Score> {
	
	private User user;
	private int numSolved;
	
	public Score(User user, int numSolved) {
		this.user = user;
		this.numSolved = numSolved;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public int getNumSolved() {
		return this.numSolved;
	}
	
	public void setNumSolved(int numSolved) {
		this.numSolved = numSolved;
	}
	
	@Override
	public int compareTo(Score o) {
		return o.numSolved - this.numSolved;
	}
	
}