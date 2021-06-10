package devomed;

import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class NewProgramPageController extends MainPageController{
	@FXML TextField programName, reps, sets, resistance;
	@FXML TableView<Exercise> exerciseTable;
	@FXML TableView<ProgramExercise> programTable;
	
	private ProgramExercise programSelection;
	private Exercise exerciseSelection;
	private boolean edit = false;
	
	@FXML public void initialize() {
		
       
        TableColumn<Exercise, String> column1 = new TableColumn<>("Exercise");
	    column1.setCellValueFactory(new PropertyValueFactory<>("name"));

	    TableColumn<Exercise, Integer> column2 = new TableColumn<>("Count");
	    column2.setCellValueFactory(new PropertyValueFactory<>("timesPerformed"));

	    exerciseTable.getColumns().add(column1);
	    exerciseTable.getColumns().add(column2);
	    exerciseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	    	exerciseSelection = newSelection;
	    });
	    
	    TableColumn<ProgramExercise, String> exerciseColumn1 = new TableColumn<>("Exercise");
	    exerciseColumn1.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue().toString()));
	    TableColumn<ProgramExercise, Integer> exerciseColumn2 = new TableColumn<>("Repetitions");
	    exerciseColumn2.setCellValueFactory(new PropertyValueFactory<>("repetitions"));
	    TableColumn<ProgramExercise, Integer> exerciseColumn3 = new TableColumn<>("Sets");
	    exerciseColumn3.setCellValueFactory(new PropertyValueFactory<>("sets"));
	    TableColumn<ProgramExercise, Integer> exerciseColumn4 = new TableColumn<>("Resistance");
	    exerciseColumn4.setCellValueFactory(new PropertyValueFactory<>("resistance"));
	    
	    programTable.getColumns().add(exerciseColumn1);
	    programTable.getColumns().add(exerciseColumn2);
	    programTable.getColumns().add(exerciseColumn3);
	    programTable.getColumns().add(exerciseColumn4);
	    programTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	    	programSelection = newSelection;
	    });
	}
	
	@Override
	public void userSetup() {
		ArrayList<Exercise> exercises = currentPatient.getExercises();
		for (Exercise exercise : exercises) {
			exerciseTable.getItems().add(exercise);
		}
	}
	
	public void addExercise () {
		if (exerciseSelection == null) {
			return;
		}
		ProgramExercise exercise = new ProgramExercise();
		exercise.setRepetitions(Integer.parseInt(reps.getText()));
		exercise.setSets(Integer.parseInt(sets.getText()));
		exercise.setResistance(Integer.parseInt(resistance.getText()));
		exercise.setExercise(exerciseSelection);
		programTable.getItems().add(exercise);
		
	}
	
	public void removeExercise () {
		programTable.getItems().remove(programSelection);
	}
	
	public void changeScenePatientPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("patientPage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		PatientPageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeSceneRunExercisePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("runExercisePage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		RunExercisePageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeSceneProgramPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("programPage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		ProgramPageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setOnHidden(e -> {
	    controller.shutdown();
		});
		window.show();
	}
	
	public void changeSceneNewExercisePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("newExercisePage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		NewExercisePageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setOnHidden(e -> {
	    controller.shutdown();
		});
		window.show();
	}
	
	public void changeSceneNewUser(ActionEvent event) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("createUser.fxml"));
		Scene scene = new Scene(parent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeSceneLoginPage(ActionEvent event) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
		Scene scene = new Scene(parent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeSceneExercisePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("exercisePage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		ExercisePageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
		
		scene.setOnKeyPressed(keyPressed -> controller.keyPressedHandler(keyPressed));
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setOnHidden(e -> {
	    controller.shutdown();
		});
		window.show();
	}
	
	public void save(ActionEvent event) throws IOException {
		if (edit) {
			
		} else {
			Program program = new Program();
			for (ProgramExercise exercise : programTable.getItems()) {
				program.addExercise(exercise);
			}
			program.setName(programName.getText());
			program.setPatient(currentPatient);
			program.saveToDatabase();
			currentPatient.addProgram(program);
			changeSceneProgramPage(event);
		}
	}
	
}
