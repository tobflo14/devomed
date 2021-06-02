package devomed;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PatientPageController{
	
	@FXML Label name;
	@FXML Label selectError;
	@FXML ChoiceBox<Patient> patientSelector;
	@FXML TableView<Exercise> exerciseTable;
	
	private Therapist currentUser;
	
	
	public void initialize() {
		TableColumn<Exercise, String> column1 = new TableColumn<>("Exercise");
	    column1.setCellValueFactory(new PropertyValueFactory<>("name"));

	    TableColumn<Exercise, Integer> column2 = new TableColumn<>("Count");
	    column2.setCellValueFactory(new PropertyValueFactory<>("timesPerformed"));

	    exerciseTable.getColumns().add(column1);
	    exerciseTable.getColumns().add(column2);
	    selectError.setText("");
	}
	
	public void userSetup() {
		name.setText(currentUser.getName());
		patientSelector.getItems().addAll(currentUser.getPatients());
	}
	
	public void initializeUser(String user) {
		SqliteDB database = new SqliteDB();
		initializeUser(database.getTherapist(user));
		database.closeConnection();
	}
	
	public void initializeUser(Therapist user) {
		currentUser = user;
		userSetup();
	}
	
	public void initializeUser(Therapist therapist, Patient patient ) {
		initializeUser(therapist);
		patientSelector.setValue(patient);
	}

	public void changeSceneExercisePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("exercisePage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		ExercisePageController controller = loader.getController();
		try {
			controller.initializeUser(currentUser, patientSelector.getValue());
		} catch (Exception e) {
			selectError.setText("Select Patient");
			return;
		}
		
		scene.setOnKeyPressed(keyPressed -> controller.keyPressedHandler(keyPressed));
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setOnHidden(e -> {
		    controller.shutdown();
		});
		window.show();
	}
	
	public void changeSceneRunExercisePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("runExercisePage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		RunExercisePageController controller = loader.getController();
		try {
			controller.initializeUser(currentUser, patientSelector.getValue());
		} catch (Exception e) {
			selectError.setText("Select Patient");
			return;
		}
		
		scene.setOnKeyPressed(keyPressed -> controller.keyPressedHandler(keyPressed));
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setOnHidden(e -> {
		    controller.shutdown();
		});
		window.show();
	}
	
	public void changeSceneProgramPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("programPage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		ProgramPageController controller = loader.getController();
		controller.initializeUser(currentUser, patientSelector.getValue());
		
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
	
	public void patientSelected() {
		exerciseTable.getItems().clear();
		ArrayList<Exercise> exercises = patientSelector.getValue().getExercises();
		for (Exercise exercise : exercises) {
			exerciseTable.getItems().add(exercise);
		}
		selectError.setText("");
	}

}
