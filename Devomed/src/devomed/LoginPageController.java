package devomed;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPageController {
	
	@FXML TextField username;
	@FXML PasswordField password;
	@FXML Label label;
	SqliteDB database;
	
	public void initialize() {
	}
	
	public void changeSceneNewUser(ActionEvent event) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("createUser.fxml"));
		Scene scene = new Scene(parent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeSceneStartPage(ActionEvent event) throws IOException {
		Parent parent = FXMLLoader.load(getClass().getResource("startPage.fxml"));
		Scene scene = new Scene(parent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeScenePatientPage(ActionEvent event, String user) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("patientPage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		PatientPageController controller = loader.getController();
		controller.initializeUser(user);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void login(ActionEvent event) throws IOException {
		database = new SqliteDB();
		String hashedPassword = database.getPassword(this.username.getText());
		database.closeConnection();
		if (BCrypt.checkpw(this.password.getText(), hashedPassword)) {
			changeScenePatientPage(event, this.username.getText());
		}
		else {
			label.setText("Login failed");
		}
	}
		
}
