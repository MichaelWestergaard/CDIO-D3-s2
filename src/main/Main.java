package main;

import controller.SocketController;

public class Main {
	
	public static void main(String[] args) throws ClassNotFoundException {
		SocketController socketcontroller = new SocketController();
		socketcontroller.init();
		new Thread(socketcontroller).start();
		
		while(true) {
			socketcontroller.completeProcedure();
		}
	}
}
