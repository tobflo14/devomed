package devomed;

public class ProgramExercise {
	private Program program;//
	private Exercise exercise;//
	private Integer repetitions;
	private Integer sets;
	private Integer resistance;
	private Integer programExerciseID;//
	
	public ProgramExercise(Integer programExerciseID, Exercise exercise, Integer repetitions, Integer sets, Integer resistance) {
		super();
		this.exercise = exercise;
		this.repetitions = repetitions;
		this.sets = sets;
		this.resistance = resistance;
		this.programExerciseID = programExerciseID;
	}
	
	public ProgramExercise() {
		super();
		programExerciseID = SqliteDB.generateID();
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public Integer getRepetitions() {
		return repetitions;
	}

	public void setRepetitions(Integer repetitions) {
		this.repetitions = repetitions;
	}

	public Integer getSets() {
		return sets;
	}

	public void setSets(Integer sets) {
		this.sets = sets;
	}

	public Integer getResistance() {
		return resistance;
	}

	public void setResistance(Integer resistance) {
		this.resistance = resistance;
	}
	
	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}
	
	public int getProgramExerciseID() {
		return programExerciseID;
	}

	
	public void saveToDatabase() {
		String query = "INSERT INTO programExercises(id, exercise, repetitions, sets, resistance, program) VALUES ("+
				Integer.toString(getProgramExerciseID()) + ", \"" + 
				Integer.toString(getExercise().getExerciseID()) + "\", \"" +
				Integer.toString(repetitions) + "\", \"" +
				Integer.toString(sets) + "\", \"" +
				Integer.toString(resistance) + "\", \"" +
				Integer.toString(getProgram().getProgramID()) + "\")";
		SqliteDB database = new SqliteDB();
		database.executeUpdate(query);
		database.closeConnection();
		System.out.println("Program Exercise saved to database");
	}
	
	@Override
	public String toString() {
		return getExercise().getName();
	}
	
}
