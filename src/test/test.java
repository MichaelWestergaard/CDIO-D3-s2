package test;

import controller.UserController;

public class test {
	public static void main(String[] args) {

		UserController userController = new UserController();
		System.out.println(userController.updateUser(1, "admmin", "Admin", "fwef", "010101-0101", "tester", "aaa", "Admin", 1));
	}

}
