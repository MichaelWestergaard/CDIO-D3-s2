package datalag;

import java.util.List;

import datalag.UserDTO;

public interface IUserDAO{

	UserDTO getUser(int userID);
	void createUser(int userID, String userName, String firstName, String lastName, String cpr, String password, List<String> role, int active);
	void updateUser(int userID, String userName, String firstName, String lastName, String cpr, String password, List<String> role, int active);
	void deleteUser(int userID);	
	
	
	
	
}



