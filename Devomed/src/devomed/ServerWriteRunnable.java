package devomed;

import java.io.IOException;

public class ServerWriteRunnable implements Runnable{
	
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