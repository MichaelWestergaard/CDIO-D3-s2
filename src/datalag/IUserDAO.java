package datalag;

import datalag.UserDTO;

public interface IUserDAO{

	UserDTO getUser(int userID);
	void createUser(int userID, String userName, String name, String lastName, String cpr, String password, Roles role, boolean active);
	void updateUser(UserDTO user);
	void deleteUser(int userID);	
	
	
	
	
}



