package datalag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;


import datalag.UserDTO;

public class IUserDAO{

static IUserDAO instance;	
static ArrayList<UserDTO> user;
	
	IUserDAO (ArrayList<UserDTO> user, IUserDAO instance) {
		this.user = user;
		this.instance = instance;
	}
	
	private void Users() { //test data
		user = new ArrayList<>();
		user.add(new UserDTO(1, "Hej123", "Jan", "Erik", "123456-9823", "hejsa2", Roller.Admin, true));
	}
	
	public static IUserDAO getInstance() {
		if (instance == null) 
		instance = new IUserDAO(user, instance);
		return instance;
	} 

}



