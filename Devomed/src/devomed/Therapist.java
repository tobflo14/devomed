package devomed;

import java.util.ArrayList;

public class Therapist extends User{
	
	private ArrayList<Patient> patients;
	private String username;
	private String password;
	
	public Therapist(String username, String password, String name) {
		super(name);
		this.username = username;
		this.password = password;
		this.patients = new ArrayList<Patient>();
	}
	
	public Therapist(int userID, String username, String password, String name) {
		super(userID, name);
		this.username = username;
		this.password = password;
		this.patients = new ArrayList<Patient>();
	}
	
	public void addPatient(Patient patient) {
		patients.add(patient);
		patient.setTherapist(this);
	}
	

	public ArrayList<Patient> getPatients() {
		return patients;
	}

	public void setPatients(ArrayList<Patient> patients) {
		this.patients = patients;
	}

	@Override
	public void saveToDatabase() {
		String query = "INSERT INTO therapists (id, username, password, name) VALUES ("+
				Integer.toString(getUserID()) + ", \"" + 
				getUsername() + "\", \"" +
				getPassword() + "\", \"" +
				getName() + "\")";
		SqliteDB database = new SqliteDB();
		database.executeUpdate(query);
		database.closeConnection();
		
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
	public void numberOfPatients() {
		System.out.println(Integer.toString(patients.size()));
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
	
}

