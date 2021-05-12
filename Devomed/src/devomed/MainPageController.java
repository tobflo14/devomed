package devomed;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public abstract class MainPageController {
	protected User currentUser;
	
	public void initializeUser(String user) {
		SqliteDB database = new SqliteDB();
		currentUser = database.getUser(user);
	}
	
	public void initializeUser(User user) {
		currentUser = user;
	}
	
}
