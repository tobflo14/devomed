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
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private PrintWriter out;
	private BufferedReader in;
	
	
	public void start() throws IOException {
		robotData = new RobotData();
        serverSocket = new ServerSocket(8080);
        clientSocket = serverSocket.accept();
        System.out.println(clientSocket.getInetAddress().toString());
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
		receiveMessage();
	}
	
	public void dataMessageHandler(String message) {
		message = message.replace(",", ".");
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
			System.out.println(robotData);
		}
	}
	
	public void stop() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
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

}
