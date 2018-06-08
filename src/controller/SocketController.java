package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import datalag.MySQLController;

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
			socket = new Socket("169.254.2.3", 8000);
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
	
	public void loginProcedure() {
		
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
	
}