package test;

import controller.ProductBatchController;
import controller.ReceptController;
import controller.UserController;

public class test {

	public static void main(String[] args) {
		UserController userController = new UserController();
		ReceptController receptController = new ReceptController();
		ProductBatchController productBatchController = new ProductBatchController();

		System.out.println(receptController.getReceptComponentList());
	}

}
