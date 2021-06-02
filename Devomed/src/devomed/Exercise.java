package devomed;

import java.util.ArrayList;

public class Exercise {
	private Integer exerciseID;
	private Path path;
	private Patient patient;
	private String name;
	private Integer timesPerformed;
	
	public Exercise(Integer exerciseID, Path path, Patient patient, String name, int timesPerformed) {
		super();
		this.exerciseID = exerciseID;
		this.path = path;
		this.patient = patient;
		this.name = name;
		this.timesPerformed = timesPerformed;
	}
	
	public Exercise(Integer exerciseID, Patient patient, String name, int timesPerformed) {
		super();
		this.exerciseID = exerciseID;
		this.patient = patient;
		this.name = name;
		this.timesPerformed = timesPerformed;
	}
	
	public Exercise( Path path, Patient patient, String name) {
		super();
		this.exerciseID = SqliteDB.generateID();
		this.path = path;
		this.patient = patient;
		this.name = name;
		timesPerformed = 0;
	}
	
	public Exercise(Patient patient, String name) {
		super();
		this.exerciseID = SqliteDB.generateID();
		this.patient = patient;
		this.name = name;
		timesPerformed = 0;
	}

	public Integer getExerciseID() {
		return exerciseID;
	}

	public void setExerciseID(Integer exerciseID) {
		this.exerciseID = exerciseID;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getName() {
		return name;
	}

	public void setInstructions(String name) {
		this.name = name;
	}
	
	public void performExercise() {
		timesPerformed++;
	}
	
	public int getTimesPerformed() {
		return timesPerformed;
	}
	
	public void saveToDatabase() {
		String query = "INSERT INTO exercises (id, patient, path, count, name) VALUES ("+
				Integer.toString(getExerciseID()) + ", \"" + 
				Integer.toString(getPatient().getUserID()) + "\", \"" +
				Integer.toString(path.getPathID()) + "\", \"" +
				Integer.toString(getTimesPerformed()) + "\", \"" +
				getName() + "\")";
		SqliteDB database = new SqliteDB();
		database.executeUpdate(query);
		database.closeConnection();
		System.out.println("Exercise saved to database");
		path.saveToDatabase();
	}
	
	public static void main(String[] args) {
		SqliteDB database = new SqliteDB();
		Patient patient = database.getPatient(59408535);
		database.closeConnection();
		Exercise exercise = new Exercise(patient, "RotasjonZmedTranslasjon");
		Path path = new Path(exercise);
		for (int i=0; i<360; i++) {
			double[][] pointX = {{1,					  0,					   0,(double)i/360},
					 			 {0,Math.cos(i*Math.PI/180),-Math.sin(i*Math.PI/180),(double)i/360},
					 			 {0,Math.sin(i*Math.PI/180), Math.cos(i*Math.PI/180),(double)i/360},
					 			 {0,					  0,  					   0,			 1}};
			
			double[][] pointY = {{ Math.cos(i*Math.PI/180),0,Math.sin(i*Math.PI/180),(double)i/360},
								 {						 0,1,					   0,(double)i/360},
								 {-Math.sin(i*Math.PI/180),0,Math.cos(i*Math.PI/180),(double)i/360},
								 {						 0,0,					   0,			 1}};

			double[][] pointZ = {{Math.cos(i*Math.PI/180),-Math.sin(i*Math.PI/180),0,(double)i/360},
								 {Math.sin(i*Math.PI/180), Math.cos(i*Math.PI/180),0,(double)i/360},
								 {						0,						 0,1,(double)i/360},
								 {						0,						 0,0,			 1}};
			path.addPoint(pointZ);
		}
		exercise.setPath(path);
		exercise.saveToDatabase();
		SqliteDB database1 = new SqliteDB();
		System.out.println("database opened");
		Patient patient1 = database1.getPatient(59408535);
		System.out.println("patient loaded");
		database1.addExercises(patient1);
		System.out.println("Exercises added");
	}
	
	@Override
	public String toString() {
		return name;
	}
}
