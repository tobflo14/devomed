package devomed;

public class Exercise {
	private Integer exerciseID;
	private Integer pathID;
	private Integer patientID;
	private String instructions;
	
	public Exercise(Integer exerciseID, Integer pathID, Integer patientID, String instructions) {
		super();
		this.exerciseID = exerciseID;
		this.pathID = pathID;
		this.patientID = patientID;
		this.instructions = instructions;
	}

	public Integer getExerciseID() {
		return exerciseID;
	}

	public void setExerciseID(Integer exerciseID) {
		this.exerciseID = exerciseID;
	}

	public Integer getPathID() {
		return pathID;
	}

	public void setPathID(Integer pathID) {
		this.pathID = pathID;
	}

	public Integer getPatientID() {
		return patientID;
	}

	public void setPatientID(Integer patientID) {
		this.patientID = patientID;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	
	
}
