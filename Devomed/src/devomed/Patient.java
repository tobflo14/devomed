package devomed;

public class Patient extends User{
	
	private String info;
	private Therapist therapist;
	
	
	public Patient(String username, String password, String name, String info,
			Therapist therapist) {
		super(username, password, name);
		this.info = info;
		this.therapist = therapist;
	}
	
	public Patient(int userID, String username, String password, String name, String info,
			Therapist therapist) {
		super(userID, username, password, name);
		this.info = info;
		this.therapist = therapist;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public Therapist getTherapist() {
		return therapist;
	}


	public void setTherapist(Therapist therapist) {
		this.therapist = therapist;
	}
	
	@Override
	public void saveToDatabase() {
		String query = "INSERT INTO patients (id, username, password, name, info, therapist) VALUES ("+
						Integer.toString(getUserID()) + ", \"" + 
						getUsername() + "\", \"" +
						getPassword() + "\", \"" +
						getName() + "\", \"" +
						getInfo() + "\", " +
						Integer.toString(therapist.getUserID()) + ")";
		database = new SqliteDB();
		database.executeUpdate(query);
		database.closeConnection();
	}
	
	public static void main(String[] args) {
		Therapist therapist = new Therapist("doktorproktor", "prompepulver", "Lege Legesen");
		Patient patient = new Patient("tobflo14", "passord", "Tobias Fløtre", "Kjørte ikke ned i en grøft", therapist);
		patient.saveToDatabase();
		
	}
	
}
