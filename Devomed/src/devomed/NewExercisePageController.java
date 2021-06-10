package devomed;

import java.io.IOException;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;	
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class NewExercisePageController extends MainPageController{
	@FXML private SubScene subscene;
	@FXML private Slider resistanceSlider;
	@FXML private Label resistanceLabel;
	@FXML private Button startButton;
	@FXML private Button stopButton;
	@FXML private Button saveButton;
	@FXML private Label verifyLabel;
	@FXML private Label name;
	
	private PerspectiveCamera camera;
    private Group group;
    private Group modelGroup;
    
	
    @FXML
    public void initialize() {
    	if (App.server.isRunning()) {
    		App.server.getRobotData().addPoseListener(this);
    	}
		box = new Box(15, 25, 20);
		group = new Group();
		camera = new PerspectiveCamera();
        group.getChildren().add(box);
        
        ObjModelImporter modelImporter = new ObjModelImporter();
        modelImporter.read("src/devomed/male_head.obj");
        Node[] objMesh = (Node[]) modelImporter.getImport();
        modelImporter.close();
        modelGroup = new Group(objMesh);
        
        box.translateXProperty().set(0);
        box.translateYProperty().set(0);
        box.translateZProperty().set(0);
        camera.translateXProperty().set(-subscene.getWidth()/2);
        camera.translateYProperty().set(-subscene.getHeight()/2-10);
        camera.translateZProperty().set(1100);
        
        subscene.setRoot(modelGroup);
        subscene.setFill(Color.SILVER);
        subscene.setCamera(camera);
        stopButton.setDisable(true);
        saveButton.setDisable(true);
	}
    
    @Override
    public void userSetup() {
    	name.setText(currentPatient.getName());
    }
	
	public void changeSceneExercisePage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("exercisePage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		ExercisePageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
		
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
		controller.initializeUser(currentUser, currentPatient);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.setOnHidden(e -> {
	    controller.shutdown();
		});
		window.show();
	}
	
	public void changeScenePatientPage(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("patientPage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		PatientPageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
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
	
	public void startRecording () {
		if (App.server.isRunning()) {
			App.server.sendMessage("1:true");
		}
		startButton.setDisable(true);
		stopButton.setDisable(false);
		saveButton.setDisable(true);
	}
	
	public void stopRecording () {
		if (App.server.isRunning()) {
			App.server.sendMessage("1:false");
		}
		startButton.setDisable(false);
		stopButton.setDisable(true);
		saveButton.setDisable(false);
	}
	
	public void saveExercise () {
		
	}
	
	@Override
	public void setPose(double x, double y, double z, double[] eulerAngles, Group group) {
		group.getTransforms().clear();
		translate(x*100, y*100, z*100, group);
		rotate(eulerAngles[0], Rotate.Z_AXIS, group);
		rotate(eulerAngles[1], Rotate.Y_AXIS, group);
		rotate(eulerAngles[2], Rotate.X_AXIS, group);
		rotate(180, Rotate.Z_AXIS, group);
		
	}
	
	@Override
	public void poseChanged() {
		setPose(App.server.getRobotData().getYPosition(),
				-App.server.getRobotData().getZPosition(),
				-App.server.getRobotData().getXPosition(),
				App.server.getRobotData().getEulerAngles(), modelGroup);
	}
	
}
