package devomed;

import java.io.IOException;

public class ServerReadRunnable implements Runnable{
	
	private boolean stopRequested;
	
	public void requestStop() {
		stopRequested = true;
	}
	
	public boolean isStopRequested() {
		return stopRequested;
	}
	
	@Override
	public void run(){
		while (!isStopRequested()) {
			try {
				App.server.receiveMessage();
			} catch (IOException e) {
				
			}
			
		}
	}

}
