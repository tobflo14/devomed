package devomed;

import java.util.ArrayList;

public class Patient extends User{
	
	private String info;
	private Therapist therapist;
	private ArrayList<Exercise> exercises;
	private ArrayList<Program> programs;
	
	
	public Patient(String name, String info, Therapist therapist) {
		super(name);
		this.info = info;
		this.therapist = therapist;
		exercises = new ArrayList<Exercise>();
		programs = new ArrayList<Program>();
	}
	
	public Patient(int userID, String name, String info, Therapist therapist) {
		super(userID, name);
		this.info = info;
		this.therapist = therapist;
		exercises = new ArrayList<Exercise>();
		programs = new ArrayList<Program>();
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
	
	public void addExercise(Exercise exercise) {
		if (!exercises.contains(exercise)) {
			exercises.add(exercise);
		}
	}
	
	public void removeExercise(Exercise exercise) {
		if (exercises.contains(exercise)) {
			exercises.remove(exercise);
		}
	}
	
	public ArrayList<Exercise> getExercises() {
		return exercises;
	}
	
	@Override
	public void saveToDatabase() {
		String query = "INSERT INTO patients (id, name, info, therapist) VALUES ("+
						Integer.toString(getUserID()) + ", \"" + 
						getName() + "\", \"" +
						getInfo() + "\", " +
						Integer.toString(therapist.getUserID()) + ")";
		SqliteDB database = new SqliteDB();
		database.executeUpdate(query);
		database.closeConnection();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public void addProgram(Program program) {
		if (!programs.contains(program)) {
			programs.add(program);
		}
	}
	
	public void removeProgram(Program program) {
		if (programs.contains(program)) {
			programs.remove(program);
		}
	}
	
	public ArrayList<Program> getPrograms() {
		return programs;
	}
	
	public Exercise getExercise(int id) {
		for (Exercise exercise : exercises) {
			if (exercise.getExerciseID() == id) {
				return exercise;
			}
		}
		return null;
	}
}
