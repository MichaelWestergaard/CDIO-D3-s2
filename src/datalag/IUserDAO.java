package datalag;

import datalag.UserDTO;

public interface IUserDAO{

	UserDTO getUser(int userID);
	void createUser(int userID, String userName, String firstName, String lastName, String cpr, String password, Roles role, boolean active);
	void updateUser(int userID, String userName, String firstName, String lastName, String cpr, String password, Roles role, boolean active);
	void deleteUser(int userID);	
	
	
	
	
}



