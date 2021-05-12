package devomed;

import java.util.Random;

import javafx.scene.image.Image;

public abstract class User {
	
	private String username;
	private String password;
	private String name;
	private int userID;
	protected SqliteDB database;
	
	public User(String username, String password, String name) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.userID = generateUserID();
	}
	
	public User(int userID, String username, String password, String name) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public static int generateUserID() {
	    int minimum = (int) Math.pow(10, 7); // minimum value with 2 digits is 10 (10^1)
	    int maximum = (int) Math.pow(10, 8) - 1; // maximum value with 2 digits is 99 (10^2 - 1)
	    Random random = new Random();
	    return minimum + random.nextInt((maximum - minimum) + 1);
	}
	
	public abstract void saveToDatabase();
	
	public static void main(String[] args) {
		System.out.println(generateUserID());
	}
	
	
}
