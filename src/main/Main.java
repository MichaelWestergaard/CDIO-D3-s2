package main;

import controller.SocketController;

public class Main {
	
	public static void main(String[] args) throws ClassNotFoundException {
		SocketController socketcontroller = new SocketController();
		socketcontroller.init();
		new Thread(socketcontroller).start();
		
		socketcontroller.sendMessage("P111 \"Tast x for log ud\"");
		socketcontroller.sleep();
		socketcontroller.sleep();
		socketcontroller.sleep();
		socketcontroller.sleep();		
		while(true) {
			socketcontroller.completeProcedure();
		}
	}
}