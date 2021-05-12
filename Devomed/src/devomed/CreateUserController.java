package devomed;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CreateUserController {
	
	@FXML TextField username, name;
	@FXML PasswordField password, confirmPassword;
	@FXML RadioButton therapistCheck, patientCheck;
	@FXML ChoiceBox<Therapist> therapistBox;
	@FXML TextArea info;
	ToggleGroup togglegroup;
	ArrayList <Therapist> therapists;
	SqliteDB database;
	
	
	
	public void initialize() {
		database = new SqliteDB();
		therapists = database.getTherapists();
		database.closeConnection();
		for (int i = 0; i < therapists.size(); i++) {
			therapistBox.getItems().add(therapists.get(i));
		}
		
		togglegroup = new ToggleGroup();
		therapistCheck.setToggleGroup(togglegroup);
		patientCheck.setToggleGroup(togglegroup);
		
	}
	
	public void createUser(ActionEvent event) throws IOException{
		if (therapistCheck.isSelected()) {
			String username = this.username.getText();
			String password = this.password.getText();
			String confirmPassword = this.confirmPassword.getText();
			String name = this.name.getText();
			
			Therapist therapist = new Therapist(username, password, name);
			therapist.saveToDatabase();
			changeSceneStartPage(event);
		}
		if (patientCheck.isSelected()) {
			String username = this.username.getText();
			String password = this.password.getText();
			String confirmPassword = this.confirmPassword.getText();
			String name = this.name.getText();
			String info = this.info.getText();
			Therapist therapist = therapistBox.getValue();
			
			Patient patient = new Patient(username, password, name, info, therapist);
			patient.saveToDatabase();
			changeSceneStartPage(event);
		}
		
	}
	
	public void changeSceneStartPage(ActionEvent event) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("startPage.fxml"));
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
}
