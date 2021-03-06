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
		therapistBox.getItems().addAll(therapists);
		
		togglegroup = new ToggleGroup();
		therapistCheck.setToggleGroup(togglegroup);
		patientCheck.setToggleGroup(togglegroup);
		
	}
	
	public void createUser(ActionEvent event) throws IOException{
		if (therapistCheck.isSelected()) {
			String salt = BCrypt.gensalt();
			String username = this.username.getText();
			String password = BCrypt.hashpw(this.password.getText(), salt);
			String confirmPassword = BCrypt.hashpw(this.confirmPassword.getText(), salt);
			String name = this.name.getText();
			if (password.equals(confirmPassword)) {
				Therapist therapist = new Therapist(username, password, name, salt);
				therapist.saveToDatabase();
				changeSceneStartPage(event);
			}
			
		}
		if (patientCheck.isSelected()) {
			String name = this.name.getText();
			String info = this.info.getText();
			Therapist therapist = therapistBox.getValue();
			
			Patient patient = new Patient(name, info, therapist);
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
