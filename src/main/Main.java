package main;

import controller.SocketController;

public class Main {
	
	public static void main(String[] args) {	
		try {
			SocketController socketcontroller = new SocketController();

			socketcontroller.init();
			new Thread(socketcontroller).start();
			
			while(true) {
				socketcontroller.completeProcedure();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
