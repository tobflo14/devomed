package devomed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class Server {
	
	private RobotData robotData;
	private ServerSocket serverReadSocket;
	private ServerSocket serverWriteSocket;
	private Socket readSocket;
	private Socket writeSocket;
	private PrintWriter out;
	private BufferedReader in;
	private boolean run;
	
	
	public void start() throws IOException {
		robotData = new RobotData();
        serverReadSocket = new ServerSocket(8080);
        serverWriteSocket = new ServerSocket(8081);
        readSocket = serverReadSocket.accept();
        writeSocket = serverWriteSocket.accept();
        System.out.println(readSocket.getInetAddress().toString());
        System.out.println(writeSocket.getInetAddress().toString());
        out = new PrintWriter(writeSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(readSocket.getInputStream()));
    }
	
	public void sendMessage(String message) {
		out.println(message);
	}
		
	public void receiveMessage() throws IOException {
		String message = in.readLine();
		if (message == null) {
			return;
		}
		String[] splitString = message.split(":");
		int messageType = Integer.parseInt(splitString[0]);
		String content = splitString[1];
		switch(messageType) {
		case 1:
			dataMessageHandler(content);
			break;
		case 2:
			System.out.println(content);
		}
		//receiveMessage(); 
	}
	
	public void dataMessageHandler(String message) {
		message = message.replace(",", ".");  //For some reason commas are used as decimals in the input, should be changed
		String[] data = message.split(";");
		if(data.length == 16) {
			double[][] pose = new double[4][4];
			int column = 0;
			int row = 0;
			for (int i=0; i<16; i++) {
				pose[row][column] = Double.parseDouble(data[i]);
				row += 1;
				if (row == 4) {
					row = 0;
					column +=1;
				}
			}
			robotData.setPose(pose);
			//for (double[] rad : pose) {
			//	  System.out.println(Double.toString(rad[0]) + " " + Double.toString(rad[1]) + " " + Double.toString(rad[2]) + " " + Double.toString(rad[3]));
			//}
		}
	}
	
	public void stop() throws IOException {
        in.close();
        out.close();
        readSocket.close();
        writeSocket.close();
        serverReadSocket.close();
        serverWriteSocket.close();
        System.out.println("Server closed");
    }
	
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server=new Server();
        server.start();
        while (true) {
        	server.receiveMessage();
        }
    }
    
    public RobotData getRobotData() {
    	return robotData;
    }
    
    public void setRun(boolean run) {
    	this.run = run;
    }
    
    public boolean isRunning() {
    	return run;
    }
    
}
