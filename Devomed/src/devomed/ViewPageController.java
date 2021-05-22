package devomed;

import java.io.IOException;
import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ViewPageController extends MainPageController implements PoseListener{
	
	@FXML private SubScene subscene;
	private Box box;
	private PerspectiveCamera camera;
    private Group group;
	
	
	@FXML public void initialize() {
		App.server.getRobotData().addPoseListener(this);
		box = new Box(15, 25, 20);
		group = new Group();
		camera = new PerspectiveCamera();
        group.getChildren().add(box);
        
        box.translateXProperty().set(0);
        box.translateYProperty().set(0);
        box.translateZProperty().set(0);
        camera.translateXProperty().set(-subscene.getWidth()/2);
        camera.translateYProperty().set(-subscene.getHeight()/2-10);
        camera.translateZProperty().set(1100);
        
        
        //box.translateZProperty().set(-200);
        //camera.translateZProperty().set(900);
        
        subscene.setRoot(group);
        subscene.setFill(Color.SILVER);
        subscene.setCamera(camera);
	}
	
	public void keyPressedHandler (KeyEvent event) {
		if (event.getCode() == KeyCode.W) {
	        rotate(10, Rotate.X_AXIS);
	    }
		if (event.getCode() == KeyCode.S) {
	        rotate(-10, Rotate.X_AXIS);
	    }
		if (event.getCode() == KeyCode.A) {
	        rotate(10, Rotate.Y_AXIS);
	    }
		if (event.getCode() == KeyCode.D) {
	        rotate(-10, Rotate.Y_AXIS);
	    }
		if (event.getCode() == KeyCode.Q) {
	        rotate(10, Rotate.Z_AXIS);
	    }
		if (event.getCode() == KeyCode.E) {
	        rotate(-10, Rotate.Z_AXIS);
	    }
		if (event.getCode() == KeyCode.I) {
	        translate(10, 0, 0);
	    }
		if (event.getCode() == KeyCode.K) {
	        translate(-10, 0, 0);
	    }
		if (event.getCode() == KeyCode.J) {
	        translate(0, 10, 0);
	    }
		if (event.getCode() == KeyCode.L) {
	        translate(0, -10, 0);
	    }
		if (event.getCode() == KeyCode.U) {
	        translate(0, 0, 10);
	    }
		if (event.getCode() == KeyCode.O) {
	        translate(0, 0, -10);
	    }
	}
		
	private void rotate (double degrees, Point3D axis) {
		Transform transform = new Rotate(degrees, axis);
        box.getTransforms().add(transform);
	}
	
	private void translate (double x, double y, double z) {
		Transform transform = new Translate(x, y, z);
		box.getTransforms().add(transform);
	}
	
	private void rotateCamera (double degrees, Point3D axis) {
		Transform transform = new Rotate(degrees, axis);
        camera.getTransforms().add(transform);
	}
	
	private void translateCamera (double x, double y, double z) {
		Transform transform = new Translate(x, y, z);
		camera.getTransforms().add(transform);
	}
	
	public void setPose(double x, double y, double z) {
		box.getTransforms().clear();
		translate(x, y, z);
	}
	
	public void changeSceneUserPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("userPage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		UserPageController controller = loader.getController();
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

	@Override
	public void poseChanged() {
		setPose(App.server.getRobotData().getPose()[1][3]*100,
				-App.server.getRobotData().getPose()[2][3]*100,
				-App.server.getRobotData().getPose()[0][3]*100);
		
	}
	
}
