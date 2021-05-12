package devomed;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartPageController {
	
	@FXML Pane image;
	double imageWidth = 868;
	double imageHeight = 480;
	Image picture = new Image("file:src/devomed/franka-panda.jpg", imageWidth, imageHeight, true, false, true);
	
	public void initialize() {
		image.getChildren().add(new ImageView(picture));
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
}
