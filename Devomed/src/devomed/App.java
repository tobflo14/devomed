package devomed;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
	
	public static Server server;
	ServerReadRunnable readThreadRunnable;
	
	@Override
	public void start(final Stage primaryStage) throws Exception {
		server = new Server();
		server.start();
		readThreadRunnable = new ServerReadRunnable();
		Thread readThread = new Thread(readThreadRunnable);
		readThread.start();
		primaryStage.setTitle("Devomed");
		primaryStage.setScene(new Scene(FXMLLoader.load(App.class.getResource("startPage.fxml"))));
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		readThreadRunnable.requestStop();
		server.stop();
		super.stop();
	}
	
	public static void main(final String[] args) {
		App.launch(args);
	}
}