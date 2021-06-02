package devomed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class AnimateExerciseRunnable implements Runnable{
	private boolean running;
	private boolean stopRequested ;
	private double[][] animationPose;
	private ArrayList<double[][]> points;
	private Collection<AnimationPoseListener> poseListeners = new ArrayList<AnimationPoseListener>();
	
	public void addListener(AnimationPoseListener poseListener) {
		poseListeners.add(poseListener);
	}
	
	public void removeListener(AnimationPoseListener poseListener) {
		if (poseListeners.contains(poseListener)) {
			poseListeners.remove(poseListener);
		}
	}
	
	public void removeAllListeners(AnimationPoseListener poseListener) {
		poseListeners.clear();
	}
	
	public AnimateExerciseRunnable(ArrayList<double[][]> points) {
		stopRequested = false;
		running = false;
		this.points = points;
	}
	
	public void requestStop() {
		stopRequested = true;
	}
	
	public boolean isStopRequested() {
		return stopRequested;
	}
	
	public void animationPoseChanged() {
		for (AnimationPoseListener poseListener : poseListeners) {
            poseListener.animationPoseChanged(animationPose);
        }
	}
	
	public boolean isRunning() {
		return running;
	}
	
	@Override
	public void run(){
		running = true;
		while (!isStopRequested()) {
			for (double[][] point : points) {
				if (isStopRequested()) {
					break;
				}
				animationPose = point;
				animationPoseChanged();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int i=points.size()-1; i>0; i--) {
				if (isStopRequested()) {
					break;
				}
				animationPose = points.get(i);
				animationPoseChanged();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		running = false;
	}
}
