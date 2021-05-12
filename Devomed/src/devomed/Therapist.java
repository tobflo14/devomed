package devomed;

import java.util.ArrayList;

public class Therapist extends User{
	
	private ArrayList<Patient> patients;
	
	public Therapist(String username, String password, String name) {
		super(username, password, name);
		this.patients = new ArrayList<Patient>();
	}
	
	public Therapist(int userID, String username, String password, String name) {
		super(userID, username, password, name);
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
		database = new SqliteDB();
		database.executeUpdate(query);
		database.closeConnection();
		
	}
	
	public static void main(String[] args) {
		Therapist therapist = new Therapist("doktorproktor", "prompepulver", "Lege Legesen");
		Patient patient = new Patient("tobflo14", "passord", "Tobias Fløtre", "Kjørte ikke ned i en grøft", therapist);
		therapist.saveToDatabase();
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getName();
	}
	
}

