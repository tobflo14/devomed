package devomed;

import java.io.IOException;
import java.util.ArrayList;

import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class ExercisePageController extends MainPageController implements AnimationPoseListener{
	
	@FXML private SubScene subscene;
	@FXML private TableView<Exercise> exerciseTable;
	@FXML private Label name;
	
	private PerspectiveCamera camera;
    private Group group;
    private Thread animateThread;
    AnimateExerciseRunnable animateRunnable;
	
	
	@FXML public void initialize() {
		if (App.server.run()) {
			App.server.getRobotData().addPoseListener(this);
		}
		box = new Box(15, 25, 20);
		group = new Group();
		camera = new PerspectiveCamera();
        group.getChildren().add(box);
        
        animationBox = new Box(15, 25, 20);
        animationBox.translateXProperty().set(0);
        animationBox.translateYProperty().set(0);
        animationBox.translateZProperty().set(0);
        
        box.translateXProperty().set(0);
        box.translateYProperty().set(0);
        box.translateZProperty().set(0);
        camera.translateXProperty().set(-subscene.getWidth()/2);
        camera.translateYProperty().set(-subscene.getHeight()/2-10);
        camera.translateZProperty().set(1100);
        
        subscene.setRoot(group);
        subscene.setFill(Color.SILVER);
        subscene.setCamera(camera);
        
        TableColumn<Exercise, String> column1 = new TableColumn<>("Exercise");
	    column1.setCellValueFactory(new PropertyValueFactory<>("name"));

	    TableColumn<Exercise, Integer> column2 = new TableColumn<>("Count");
	    column2.setCellValueFactory(new PropertyValueFactory<>("timesPerformed"));

	    exerciseTable.getColumns().add(column1);
	    exerciseTable.getColumns().add(column2);
	    exerciseTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	    	if (oldSelection != null) {
	    		try {
					stopAnimation();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	    	}
	    	animateRunnable = new AnimateExerciseRunnable(newSelection.getPath().getPoints());
	    	animateRunnable.addListener(this);
	        animateThread = new Thread(animateRunnable);
	        switchBox(animationBox);
	        animateThread.start();
	    });
	}
	
	@Override
	public void userSetup() {
		ArrayList<Exercise> exercises = currentPatient.getExercises();
		for (Exercise exercise : exercises) {
			exerciseTable.getItems().add(exercise);
		}
		name.setText(currentPatient.getName());
	}
		

	
	public void switchBox(Box box) {
		group.getChildren().clear();
		group.getChildren().add(box);
	}
	
	
	private void rotateCamera (double degrees, Point3D axis) {
		Transform transform = new Rotate(degrees, axis);
        camera.getTransforms().add(transform);
	}
	
	private void translateCamera (double x, double y, double z) {
		Transform transform = new Translate(x, y, z);
		camera.getTransforms().add(transform);
	}
	
	public void changeScenePatientPage(ActionEvent event) throws IOException {
		shutdownAnimationThread();
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
	
	public void changeSceneRunExercisePage(ActionEvent event) throws IOException {
		shutdownAnimationThread();
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("runExercisePage.fxml"));
		Parent parent = loader.load();
		
		Scene scene = new Scene(parent);
		
		RunExercisePageController controller = loader.getController();
		controller.initializeUser(currentUser, currentPatient);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeSceneProgramPage(ActionEvent event) throws IOException {
		shutdownAnimationThread();
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
	
	public void changeSceneNewUser(ActionEvent event) throws IOException {
		shutdownAnimationThread();
		Parent parent = FXMLLoader.load(getClass().getResource("createUser.fxml"));
		Scene scene = new Scene(parent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}
	
	public void changeSceneLoginPage(ActionEvent event) throws IOException {
		shutdownAnimationThread();
		Parent parent = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
		Scene scene = new Scene(parent);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
	}


	
	@Override
	public void animationPoseChanged(double[][] pose) {
		setPose(pose, animationBox);
	}
	
	public Affine createAffineTransform(double[][] rotationMatrix) {
		Affine transition = new Affine();
		transition.setMxx(rotationMatrix[0][0]);
		transition.setMxy(rotationMatrix[0][1]);
		transition.setMxz(rotationMatrix[0][2]);
		transition.setTx(rotationMatrix[0][3]);
		transition.setMyx(rotationMatrix[1][0]);
		transition.setMyy(rotationMatrix[1][1]);
		transition.setMyz(rotationMatrix[1][2]);
		transition.setTy(rotationMatrix[1][3]);
		transition.setMzx(rotationMatrix[2][0]);
		transition.setMzy(rotationMatrix[2][1]);
		transition.setMzz(rotationMatrix[2][2]);
		transition.setTz(rotationMatrix[2][3]);
		return transition;
	}
	
	private double[][] invertRotationMatrix(double[][] rotationMatrix) {
		double[][] inverseRotationMatrix = new double[3][3];
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				inverseRotationMatrix[i][j] = rotationMatrix[j][i];
			}
		}
		return inverseRotationMatrix;
	}
	
	private double[][] getRotationMatrix(double[][] transformationMatrix) {
		double[][] rotationMatrix = new double[3][3];
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				rotationMatrix[i][j] = transformationMatrix[i][j];
			}
		}
		return rotationMatrix;
	}
	
	private double[][] matrixMultiplication(double[][] matrix1, double[][] matrix2) {
		double[][] result = new double[3][3]; 	
	       
		for (int i=0;i<3;i++) {    
			for(int j=0;j<3;j++){    
				result[i][j]=0;      
				for (int k=0;k<3;k++) {      
					result[i][j] += matrix1[i][k] * matrix2[k][j];      
				}    
			}
		}
		return result;
	}
	
	private double[] getEulerAngles(double[][] poseFrom, double[][] poseTo) {
		double[][] m1 = invertRotationMatrix(poseFrom);
		double[][] m2 = getRotationMatrix(poseTo);
		double[][] rotationMatrix = matrixMultiplication(m1, m2);
		
		double[] eulerAngles = new double[3];
		eulerAngles[0] = getYaw(rotationMatrix);
		eulerAngles[1] = getPitch(rotationMatrix);
		eulerAngles[2] = getRoll(rotationMatrix);
		return eulerAngles;
	}
	
	public ParallelTransition[] createParallelTransitions(ArrayList<double[][]> points) {
		ParallelTransition[] transitions= new ParallelTransition[points.size()-1];
		for (int i=0; i<points.size()-1; i++) {
			double[] eulerAngles = getEulerAngles(points.get(i), points.get(i+1));
			
			TranslateTransition translateTransition = new TranslateTransition();
			translateTransition.setFromX(points.get(i)[0][3]);
			translateTransition.setFromY(points.get(i)[1][3]);
			translateTransition.setFromZ(points.get(i)[2][3]);
			translateTransition.setToX(points.get(i+1)[0][3]);
			translateTransition.setToY(points.get(i+1)[1][3]);
			translateTransition.setToZ(points.get(i+1)[2][3]);
			translateTransition.setDuration(Duration.millis(500));
			translateTransition.setNode(animationBox);
			translateTransition.setAutoReverse(true);
			translateTransition.setCycleCount( Animation.INDEFINITE );
			
			RotateTransition rotationX = new RotateTransition();
			rotationX.setAxis(Rotate.X_AXIS);
	        rotationX.setDuration(Duration.seconds(5));
	        rotationX.setByAngle(eulerAngles[2]);
	        rotationX.setNode(animationBox);
	        rotationX.setAutoReverse(true);
	        rotationX.setCycleCount( Animation.INDEFINITE );
	        
			RotateTransition rotationY = new RotateTransition();
			rotationY.setAxis(Rotate.Y_AXIS);
	        rotationY.setDuration(Duration.seconds(5));
	        rotationY.setByAngle(eulerAngles[1]);
	        rotationY.setNode(animationBox);
	        rotationY.setAutoReverse(true);
	        rotationY.setCycleCount( Animation.INDEFINITE );
	        
			RotateTransition rotationZ = new RotateTransition();
			rotationZ.setAxis(Rotate.Z_AXIS);
	        rotationZ.setDuration(Duration.seconds(5));
	        rotationZ.setByAngle(eulerAngles[0]);
	        rotationZ.setNode(animationBox);
	        rotationZ.setAutoReverse(true);
	        rotationZ.setCycleCount( Animation.INDEFINITE );
	        
			ParallelTransition transition = new ParallelTransition(translateTransition, rotationZ, rotationY, rotationX);
			transitions[i] = transition;
			System.out.println(i);
		}
		System.out.println("transitions Created");
		return transitions;
	}
	
	public void playAnimation(Exercise exercise) {
		Duration duration = Duration.millis(500);
		ParallelTransition[] transitionArray = createParallelTransitions(exercise.getPath().getPoints());
		System.out.println("Generating animation");
		SequentialTransition transition = new SequentialTransition(transitionArray);
		System.out.println("Animation completed");
		switchBox(animationBox);
		transition.play();
	}
	
	public void animateExercise() throws InterruptedException {
		Exercise exercise = exerciseTable.getSelectionModel().getSelectedItem();
		for (int i=0; i<exercise.getPath().getPoints().size(); i++) {
			setPose(exercise.getPath().getPoints().get(i), animationBox);
			Thread.sleep(500);
		}
		for (int i=0; i<exercise.getPath().getPoints().size(); i++) {
			setPose(exercise.getPath().getPoints().get(exercise.getPath().getPoints().size()-i), animationBox);
			Thread.sleep(500);
		}
	}
	public void stopAnimation() throws InterruptedException {
		animateRunnable.requestStop();
		animateThread.join();
		switchBox(box);
	}
	
	public void exerciseSelected(Exercise exercise) {
		playAnimation(exercise);
	}
	
	public void shutdownAnimationThread() {
		if (animateRunnable != null) {
			if (animateRunnable.isRunning()) {
				animateRunnable.requestStop();
			}
		}
		switchBox(box);
	}
	
	@Override
	public void shutdown() {
		shutdownAnimationThread();
	}

}
