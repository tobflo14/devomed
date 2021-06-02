package devomed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class RobotData {

	private double[][] pose;
	private Collection<PoseListener> poseListeners = new ArrayList<PoseListener>();
	
	public void addPoseListener(PoseListener poseListener) {
		poseListeners.add(poseListener);
	}
	
	public void removePoseListener(PoseListener poseListener) {
		if (poseListeners.contains(poseListener)) {
			poseListeners.remove(poseListener);
		}
	}
	
	public void poseChanged() {
		for (PoseListener poseListener : poseListeners) {
            poseListener.poseChanged();
        }
	}
	public double[][] getPose() {
		return pose;
	}
	
	public void setPose(double[][] pose) {
		this.pose = pose;
		poseChanged();
	}
	
	public double getXPosition() {
		return pose[0][3];
	}

	public double getYPosition() {
		return pose[1][3];
	}

	public double getZPosition() {
		return pose[2][3];
	}
	
	public double getRoll() {
		return Math.atan2(pose[3][2], pose[3][3]) * (180.0/Math.PI);
	}
	
	public double getPitch() {
		return Math.asin(pose[3][1]) * (180.0/Math.PI);
	}
	
	public double getYaw() {
		return Math.atan2(pose[2][1], pose[1][1]) * (180.0/Math.PI);
	}
	
	public double[] getEulerAngles() {
		double[] eulerAngles = new double[3];
		eulerAngles[0] = getYaw();
		eulerAngles[1] = getPitch();
		eulerAngles[2] = getRoll();
		return eulerAngles;
	}
	
	public double[] getMinimalRepresentation() {
		double[] minimalRepresentation = new double[6];
		minimalRepresentation[0] = getXPosition();
		minimalRepresentation[1] = getYPosition();
		minimalRepresentation[2] = getZPosition();
		double[] eulerAngles = getEulerAngles();
		minimalRepresentation[3] = eulerAngles[0];
		minimalRepresentation[4] = eulerAngles[1];
		minimalRepresentation[5] = eulerAngles[2];
		return minimalRepresentation;
	}

	@Override
	public String toString() {
		String result = "";
		for (double[] row : pose) {
			result += Arrays.toString(row) + "\n";
		}
		return result;
	}
}
