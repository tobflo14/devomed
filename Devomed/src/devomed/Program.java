package devomed;

import java.util.ArrayList;

public class Program {
	private ArrayList<ProgramExercise> exercises;
	private Integer timesPerformed;
	private String name;
	private Patient patient;
	private Integer programID;
	
	public Program() {
		exercises = new ArrayList<ProgramExercise>();
		programID = SqliteDB.generateID();
		timesPerformed=0;
	}
	public Program(Integer timesPerformed, String name, Patient patient, Integer programID) {
		exercises = new ArrayList<ProgramExercise>();
		this.timesPerformed = timesPerformed;
		this.name = name;
		this.patient = patient;
		this.programID = programID;
	}
	
	public void addExercise(ProgramExercise exercise) {
		exercises.add(exercise);
	}
	
	public void removeExercise(ProgramExercise exercise) {
		exercises.remove(exercise);
	}

	public ArrayList<ProgramExercise> getExercises() {
		return exercises;
	}
	
	public String getName() {
		return name;
	}
	
	public Integer getTimesPerformed() {
		return timesPerformed;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public int getProgramID() {
		return programID;
	}
	
	public void saveToDatabase() {
		String query = "INSERT INTO programs(id, patient, timesPerformed, name) VALUES ("+
				Integer.toString(getProgramID()) + ", \"" + 
				Integer.toString(getPatient().getUserID()) + "\", \"" +
				Integer.toString(getTimesPerformed()) + "\", \"" +
				getName() + "\")";
		SqliteDB database = new SqliteDB();
		database.executeUpdate(query);
		database.closeConnection();
		System.out.println("Program saved to database");
	}
	
	public static void main(String[] args) {
		Program program1 = new Program();
		SqliteDB database = new SqliteDB();
		Patient patient = database.getPatient(59408535);
		database.addExercises(patient);
		database.closeConnection();
		program1.setPatient(patient);
		program1.setName("TestProgram2");
		
		ProgramExercise programExercise1 = new ProgramExercise();
		programExercise1.setExercise(patient.getExercises().get(1));
		programExercise1.setProgram(program1);
		programExercise1.setRepetitions(10);
		programExercise1.setSets(4);
		programExercise1.setResistance(250);
		
		ProgramExercise programExercise2 = new ProgramExercise();
		programExercise2.setExercise(patient.getExercises().get(2));
		programExercise2.setProgram(program1);
		programExercise2.setRepetitions(12);
		programExercise2.setSets(3);
		programExercise2.setResistance(300);
		
		program1.addExercise(programExercise1);
		program1.addExercise(programExercise2);
		programExercise1.saveToDatabase();
		programExercise2.saveToDatabase();
		program1.saveToDatabase();
	}
}
