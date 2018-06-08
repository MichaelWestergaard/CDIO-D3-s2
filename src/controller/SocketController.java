package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import datalag.MySQLController;
import datalag.UserDTO;

public class SocketController implements Runnable {
	Socket socket;
	static String readLine = null;
	MySQLController mySQLController;
	int operatorID;
	int productBatchID;
	int ingredientBatchID;
	double tara;
	double netto;

	public SocketController() throws ClassNotFoundException {
		mySQLController = new MySQLController();
	}

	public void init() {
		try {
			socket = new Socket("169.254.2.2", 8000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			while(true) {
				readLine = bufferedReader.readLine();
				System.out.println(readLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void sleep() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void loginProcedure() {
		try {
			InputStream is = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			
			sendMessage("RM20 8 \"Indtast dit ID:\" \"\" \"&3\"");
			
			boolean userConfirmed = false;
			while(!userConfirmed) {
				String inputString = reader.readLine();
				String[] inputArr = inputString.split(" ");
				sleep();
				int input = Integer.parseInt(inputArr[2].replace("\"", ""));
				UserDTO user = mySQLController.getUser(input);
				if(user != null) {
					sendMessage("RM20 8 \"Er du " + user.getInitial() + "?\" \"\" \"&3\"");
					
					inputString = reader.readLine();
					inputArr = inputString.split(" ");

					if(inputArr[1].equals("A")) {
						operatorID = user.getUserID();
						userConfirmed = true;
					} else {
						sendMessage("RM20 8 \"Indtast ID igen:\" \"\" \"&3\"");
					}
				} else {
					sendMessage("RM20 8 \"Forkert ID! Proev igen\" \"\" \"&3\"");
				}
			}			
		} catch (IOException e) {
			sendMessage("RM20 8 \"Fejl i indtastningen\" \"\" \"&3\"");
		} catch (SQLException e) {
			sendMessage("RM20 8 \"Fejl: "+e.getErrorCode()+"! Fejl i database\" \"\" \"&3\"");
		}
	}
	
	public void sendMessage(String msg) {
		try {
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.println(msg);
			pw.flush();	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void completeProcedure() {
		loginProcedure();
	}
	
}