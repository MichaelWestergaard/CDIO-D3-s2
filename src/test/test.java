package test;

import controller.ReceptController;
import controller.UserController;

public class test {

	public static void main(String[] args) {
		UserController userController = new UserController();
		ReceptController receptController = new ReceptController();

		System.out.println(userController.getUsers());
		System.out.println(receptController.getReceptList());
	}

}
