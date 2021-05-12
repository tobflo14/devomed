package devomed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteDB {
	Connection c = null;
	Statement stmt = null;
	
	public SqliteDB () {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:devomedDB");
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public void closeConnection() {
		try {
			c.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public void executeUpdate (String query) {
		try {
			this.stmt = c.createStatement();
			stmt.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public void executeQuery (String query) {
		try {
			this.stmt = c.createStatement();
			stmt.executeQuery(query);
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public ArrayList<Therapist> getTherapists() {
		ArrayList<Therapist> therapists = new ArrayList();
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM therapists");
			while (rs.next()) {
				int userID = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				String name = rs.getString("name");
				therapists.add(new Therapist(userID, username, password, name));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return therapists;
	}
	
	public String getPassword (String username) {
		String password = "";
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT password FROM therapists WHERE username=\'"+username + "\'"
					 						+" UNION SELECT password FROM patients WHERE username=\'"+username+"\'");
			password = rs.getString("password");
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
			return password;
	}
	
	public Therapist getTherapist(int userID) {
		Therapist therapist = null;
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM therapists WHERE id="+Integer.toString(userID));
			if (rs.next()) {
				therapist = new Therapist(rs.getInt("id"), rs.getString("username"), rs.getString("password"),rs.getString("name"));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return therapist;
	}
	
	public Therapist getTherapist(String username) {
		Therapist therapist = null;
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM therapists WHERE username=\'"+username + "\'");
			if (rs.next()) {
				therapist = new Therapist(rs.getInt("id"), rs.getString("username"), rs.getString("password"),rs.getString("name"));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return therapist;
	}
	
	public Patient getPatient(int userID) {
		Patient patient = null;
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM patients WHERE id="+Integer.toString(userID));
			if (rs.next()) {
				patient = new Patient(rs.getInt("id"), rs.getString("username"), rs.getString("password"),rs.getString("name"), rs.getString("info"), this.getTherapist(rs.getInt("therapist")));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return patient;
	}
	
	public Patient getPatient(String username) {
		Patient patient = null;
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM patients WHERE username=\'"+username+"\'");
			if (rs.next()) {
				patient = new Patient(rs.getInt("id"), rs.getString("username"), rs.getString("password"),rs.getString("name"), rs.getString("info"), this.getTherapist(rs.getInt("therapist")));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return patient;
	}
	
	public User getUser(String username) {
		if (this.getPatient(username) != null) {
			return this.getPatient(username);
		}
		else if (this.getTherapist(username) != null) {
			return this.getTherapist(username);
		}
		else {
			return null;
		}
	}
	
	public User getUser(int userID) {
		if (this.getPatient(userID) != null) {
			return this.getPatient(userID);
		}
		else if (this.getTherapist(userID) != null) {
			return this.getTherapist(userID);
		}
		else {
			return null;
		}
	}
}
