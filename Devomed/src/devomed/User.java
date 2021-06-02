package devomed;

public abstract class User {
	
	private String name;
	private int userID;
	
	public User(String name) {
		super();
		this.name = name;
		this.userID = SqliteDB.generateID();
	}
	
	public User(int userID, String name) {
		super();
		this.name = name;
		this.userID = userID;
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
	
	public abstract void saveToDatabase();
		
}
