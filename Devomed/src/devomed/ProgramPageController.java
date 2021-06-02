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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ProgramPageController extends MainPageController{
	@FXML TableView<Program> programTable;
	@FXML TableView<ProgramExercise> programExercisesTable;
	@FXML Label name;
	
	public void initialize() {
		TableColumn<Program, String> programColumn = new TableColumn<>("Program");
	    programColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
	    
	    TableColumn<ProgramExercise, String> exerciseColumn1 = new TableColumn<>("Exercise");
	    exerciseColumn1.setCellValueFactory((p) -> new SimpleStringProperty(p.getValue().toString()));
	    TableColumn<ProgramExercise, Integer> exerciseColumn2 = new TableColumn<>("Repetitions");
	    exerciseColumn2.setCellValueFactory(new PropertyValueFactory<>("repetitions"));
	    TableColumn<ProgramExercise, Integer> exerciseColumn3 = new TableColumn<>("Sets");
	    exerciseColumn3.setCellValueFactory(new PropertyValueFactory<>("sets"));
	    TableColumn<ProgramExercise, Integer> exerciseColumn4 = new TableColumn<>("Resistance");
	    exerciseColumn4.setCellValueFactory(new PropertyValueFactory<>("resistance"));
	    
	    programTable.getColumns().add(programColumn);
	    programExercisesTable.getColumns().add(exerciseColumn1);
	    programExercisesTable.getColumns().add(exerciseColumn2);
	    programExercisesTable.getColumns().add(exerciseColumn3);
	    programExercisesTable.getColumns().add(exerciseColumn4);
	    programTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	    	programExercisesTable.getItems().clear();
	    	ArrayList<ProgramExercise> exercises = newSelection.getExercises();
	    	for (ProgramExercise exercise : exercises) {
	    		programExercisesTable.getItems().add(exercise);
	    	}
	    });
	}
	
	@Override
	public void userSetup() {
		ArrayList<Program> programs = currentPatient.getPrograms();
		for (Program program: programs) {
			programTable.getItems().add(program);
		}
		name.setText(currentPatient.getName());
	}
	
	public void changeSceneNewUser(ActionEvent event) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("createUser.fxml"));
		Scene scene = new Scene(parent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
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
}
