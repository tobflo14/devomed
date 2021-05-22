package devomed;

import java.util.ArrayList;
import java.util.Collection;

public class RobotData {
	private double x;
	private double y;
	private double z;
	private double rotx;
	private double roty;
	private double rotz;
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
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
		poseChanged();
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
		poseChanged();
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
		poseChanged();
	}

	public double getRotx() {
		return rotx;
	}

	public void setRotx(double rotx) {
		this.rotx = rotx;
		poseChanged();
	}

	public double getRoty() {
		return roty;
	}

	public void setRoty(double roty) {
		this.roty = roty;
		poseChanged();
	}

	public double getRotz() {
		return rotz;
	}

	public void setRotz(double rotz) {
		this.rotz = rotz;
		poseChanged();
	}

	@Override
	public String toString() {
		//String string = "";
		//string += "x: " + Double.toString(x) + "\n";
		//string += "y: " + Double.toString(y) + "\n";
		//string += "z: " + Double.toString(z) + "\n";
		//string += "rotx: " + Double.toString(rotx) + "\n";
		//string += "roty: " + Double.toString(roty) + "\n";
		//string += "rotz: " + Double.toString(rotz) + "\n";
		return pose.toString();
	}
}
