package devomed;

import java.io.IOException;
import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ViewPageController extends MainPageController{
	
	@FXML private SubScene subscene;
	private Box box = new Box();
	private PerspectiveCamera camera = new PerspectiveCamera(true);
    private Group group = new Group();
    
    final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, Rotate.Z_AXIS);
    private Timeline animation;
	
	
	@FXML public void initialize() {
		box.setMaterial(new PhongMaterial(Color.ORANGE));
        box.setDepth(10);
        box.setWidth(10);
        box.setHeight(10);
        rx.setAngle(90);
        ry.setAngle(25);
        box.getTransforms().addAll(rz, ry, rx);


        group.getChildren().add(box);

        animation = new Timeline();
        animation.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO,

                        new KeyValue(box.depthProperty(), 0d),
                        new KeyValue(box.translateYProperty(),400d)),
                new KeyFrame(Duration.seconds(5),

                        new KeyValue(box.depthProperty(), 800d),
                        new KeyValue(box.translateYProperty(), 0d)));

        animation.setCycleCount(Timeline.INDEFINITE);


        camera.getTransforms().add(new Translate(0, 0, -80));
        camera.getTransforms().addAll (

                new Rotate(-35, Rotate.X_AXIS),
                new Translate(0, 0, 10)
        );

        subscene.setRoot(group);
        subscene.setCamera(camera);
        animation.play();
	}
	
	
	public SubScene createScene3D(){
		Sphere sphere = new Sphere(50);
		Group group = new Group();
		group.getChildren().add(sphere);
		SubScene scene = new SubScene(group, 650, 920);
		return scene;
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
	
}
