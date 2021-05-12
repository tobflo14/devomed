package devomed;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UserPageController extends MainPageController{
	
	@FXML Label name;
	
	
	public void initialize() {
	}
	
	@Override
	public void initializeUser(User user) {
		currentUser = user;
		name.setText(currentUser.getName());
	}
	
	public void changeSceneViewPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("viewPage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		ViewPageController controller = loader.getController();
		controller.initializeUser(currentUser);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
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
}
