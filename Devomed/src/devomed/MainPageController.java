package devomed;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;

public abstract class MainPageController implements PoseListener{
	protected Therapist currentUser;
	protected Patient currentPatient;
	protected Box box;
	protected Box animationBox;
	
	public void initializeUser(String user) {
		SqliteDB database = new SqliteDB();
		currentUser = database.getTherapist(user);
		database.closeConnection();
		userSetup();
	}
	
	public void initializeUser(Therapist user, Patient patient) {
		currentUser = user;
		currentPatient = patient;
		userSetup();
	}
	
	public void initializeUser(Therapist user) {
		currentUser = user;
		userSetup();
	}
	
	public void userSetup() {
	};
	
	public void shutdown() {
	}
	
	public void keyPressedHandler (KeyEvent event) {
		if (event.getCode() == KeyCode.W) {
	        rotate(10, Rotate.X_AXIS, box);
	    }
		if (event.getCode() == KeyCode.S) {
	        rotate(-10, Rotate.X_AXIS, box);
	    }
		if (event.getCode() == KeyCode.A) {
	        rotate(10, Rotate.Y_AXIS, box);
	    }
		if (event.getCode() == KeyCode.D) {
	        rotate(-10, Rotate.Y_AXIS, box);
	    }
		if (event.getCode() == KeyCode.Q) {
	        rotate(10, Rotate.Z_AXIS, box);
	    }
		if (event.getCode() == KeyCode.E) {
	        rotate(-10, Rotate.Z_AXIS, box);
	    }
		if (event.getCode() == KeyCode.I) {
	        rotate(10, Rotate.X_AXIS, box);
	    }
		if (event.getCode() == KeyCode.K) {
	        rotate(-10, Rotate.X_AXIS, box);
	    }
		if (event.getCode() == KeyCode.J) {
	        rotate(10, Rotate.Y_AXIS, box);
	    }
		if (event.getCode() == KeyCode.L) {
	        rotate(-10, Rotate.Y_AXIS, box);
	    }
		if (event.getCode() == KeyCode.U) {
	        rotate(10, Rotate.Z_AXIS, box);
	    }
		if (event.getCode() == KeyCode.O) {
	        rotate(-10, Rotate.Z_AXIS, box);
	    }
		if (event.getCode() == KeyCode.Z) {
	        rotate(-10, Rotate.Z_AXIS, animationBox);
	    }
		if (event.getCode() == KeyCode.X) {
	        rotate(-10, Rotate.X_AXIS, animationBox);
	    }
		if (event.getCode() == KeyCode.Y) {
	        rotate(-10, Rotate.Y_AXIS, animationBox);
	    }
	}
	
	protected void rotate (double degrees, Point3D axis, Box box) {
		Transform transform = new Rotate(degrees, axis);
        box.getTransforms().add(transform);
	}
	
	protected void translate (double x, double y, double z, Box box) {
		Transform transform = new Translate(x, y, z);
		box.getTransforms().add(transform);
	}
	
	protected void rotate (double degrees, Point3D axis, Group group) {
		Transform transform = new Rotate(degrees, axis);
        group.getTransforms().add(transform);
	}
	
	protected void translate (double x, double y, double z, Group group) {
		Transform transform = new Translate(x, y, z);
		group.getTransforms().add(transform);
	}
	
	@Override
	public void poseChanged() {
		setPose(App.server.getRobotData().getYPosition()*100,
				-App.server.getRobotData().getZPosition()*100,
				-App.server.getRobotData().getXPosition()*100,
				App.server.getRobotData().getEulerAngles(), box);
		
	}
	
	public void setPose(double x, double y, double z, double[] eulerAngles, Box box) {
		box.getTransforms().clear();
		translate(x*100, y*100, z*100, box);
		rotate(eulerAngles[0], Rotate.Z_AXIS, box);
		rotate(eulerAngles[1], Rotate.Y_AXIS, box);
		rotate(eulerAngles[2], Rotate.X_AXIS, box);
	}
	
	public void setPose(double[][] points, Box box) {
		setPose(points[0][3], points[1][3], points[2][3], getEulerAngles(points), box);
	}
	
	public void setPose(double x, double y, double z, double[] eulerAngles, Group group) {
		group.getTransforms().clear();
		translate(x*100, y*100, z*100, group);
		rotate(eulerAngles[0], Rotate.Z_AXIS, group);
		rotate(eulerAngles[1], Rotate.Y_AXIS, group);
		rotate(eulerAngles[2], Rotate.X_AXIS, group);
	}
	
	public void setPose(double[][] points, Group group) {
		setPose(points[0][3], points[1][3], points[2][3], getEulerAngles(points), group);
	}
	
	protected double[] getEulerAngles(double[][] pose) {
		double[] eulerAngles = new double[3];
		eulerAngles[0] = getYaw(pose);
		eulerAngles[1] = getPitch(pose);
		eulerAngles[2] = getRoll(pose);
		return eulerAngles;
	}
	
	protected double getRoll(double[][] pose) {
		return Math.atan2(pose[2][1], pose[2][2]) * (180.0/Math.PI);
	}
	
	protected double getPitch(double[][] pose) {
		return Math.asin(pose[2][0]) * (180.0/Math.PI);
	}
	
	protected double getYaw(double[][] pose) {
		return Math.atan2(pose[1][0], pose[0][0]) * (180.0/Math.PI);
	}
}
