package devomed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

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
	
	public ResultSet executeQuery (String query) {
		try {
			this.stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			return rs;
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
			return null;
		}
	}
	
	public ArrayList<Therapist> getTherapists() {
		ArrayList<Therapist> therapists = new ArrayList<Therapist>();
		try {
			ResultSet rs = executeQuery("SELECT * FROM therapists");
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
			ResultSet rs = executeQuery("SELECT password FROM therapists WHERE username=\'"+username + "\'");
			password = rs.getString("password");
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
			return password;
	}
	
	public Therapist getTherapist(int userID) {
		Therapist therapist = null;
		try {
			ResultSet rs = executeQuery("SELECT * FROM therapists WHERE id="+Integer.toString(userID));
			if (rs.next()) {
				therapist = new Therapist(rs.getInt("id"), rs.getString("username"), rs.getString("password"),rs.getString("name"));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		addPatients(therapist);
		return therapist;
	}
	
	public Therapist getTherapist(String username) {
		Therapist therapist = null;
		try {
			ResultSet rs = executeQuery("SELECT * FROM therapists WHERE username=\'"+username + "\'");
			if (rs.next()) {
				therapist = getTherapist(rs.getInt("id"));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return therapist;
	}
	
	public Patient getPatient(int userID) {
		Patient patient = null;
		try {
			ResultSet rs = executeQuery("SELECT * FROM patients WHERE id="+Integer.toString(userID));
			if (rs.next()) {
				patient = new Patient(rs.getInt("id"), rs.getString("name"), rs.getString("info"), this.getTherapist(rs.getInt("therapist")));
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		return patient;
	}
	
	public Patient getPatient(String username) {
		Patient patient = null;
		try {
			ResultSet rs = executeQuery("SELECT * FROM patients WHERE username=\'"+username+"\'");
			if (rs.next()) {
				patient = new Patient(rs.getInt("id"), rs.getString("name"), rs.getString("info"), this.getTherapist(rs.getInt("therapist")));
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
	
	public static int generateID() {
	    int minimum = (int) Math.pow(10, 7); // minimum value with 2 digits is 10 (10^1)
	    int maximum = (int) Math.pow(10, 8) - 1; // maximum value with 2 digits is 99 (10^2 - 1)
	    Random random = new Random();
	    return minimum + random.nextInt((maximum - minimum) + 1);
	}
	
	public void addPatients(Therapist therapist) {
		try {
			ResultSet rs = executeQuery("SELECT * FROM patients WHERE therapist="+Integer.toString(therapist.getUserID()));
			while (rs.next()) {
				Patient patient = new Patient(rs.getInt("id"), rs.getString("name"), rs.getString("info"), therapist);
				therapist.addPatient(patient);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		for (Patient patient : therapist.getPatients()) {
			addExercises(patient);
		}
	}
	
	public void addExercises(Patient patient) {
		try {
			ResultSet rs = executeQuery("SELECT * FROM exercises WHERE patient="+Integer.toString(patient.getUserID()));
			while (rs.next()) {
				Exercise exercise = new Exercise(rs.getInt("id"), patient, rs.getString("name"), rs.getInt("count"));
				patient.addExercise(exercise);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
		for (Exercise exercise : patient.getExercises()) {
			addPath(exercise);
		}
		addPrograms(patient);
	}
	
	public void addPath(Exercise exercise) {
		try {
			ResultSet rs = executeQuery("SELECT * FROM paths WHERE exercise="+Integer.toString(exercise.getExerciseID()));
			while (rs.next()) {
				Path path = new Path(rs.getInt("id"), exercise);
				path.setPoints(rs.getString("points"));
				exercise.setPath(path);
			}
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	public void addPrograms(Patient patient) {
		try {
			ResultSet rs = executeQuery("SELECT * FROM programs WHERE patient="+Integer.toString(patient.getUserID()));
			while (rs.next()) {
				Program program = new Program(rs.getInt("timesPerformed"), rs.getString("name"), patient, rs.getInt("id"));
				ResultSet rs2 = executeQuery("SELECT * FROM programExercises WHERE program="+Integer.toString(rs.getInt("id")));
				while (rs2.next()) {
					ProgramExercise programExercise = new ProgramExercise(rs2.getInt("id"),
																		  patient.getExercise(rs2.getInt("exercise")), 
																		  rs2.getInt("repetitions"), 
																		  rs2.getInt("sets"), 
																		  rs2.getInt("resistance"));
					program.addExercise(programExercise);
				}
				patient.addProgram(program);
			}
		} catch (Exception e) {
			System.out.println("Error came from addProgram()");
			System.out.println("Error: " + e.getMessage());
		}
	}
}
