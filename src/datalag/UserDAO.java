package datalag;

import java.util.ArrayList;
import java.util.List;

import datalag.UserDTO;

public class UserDAO implements IUserDAO {
	private List<UserDTO> users = new ArrayList<UserDTO>();

	
	public UserDTO getUser(int userID) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUserID()==userID) {
				return users.get(i);
			}
		}
		return null;
	}

	public void createUser(int userID, String userName, String name, String lastName, String cpr, String password,
			Roles role, boolean active) {
		if(getUser(userID) == null) {
			users.add(new UserDTO(userID, userName, name, lastName, cpr, password,
					role, active));
			
		}
		
	}

	public void updateUser(UserDTO user) {
		// TODO Auto-generated method stub
		
	}

	public void deleteUser(int userID) {
		// TODO Auto-generated method stub
		
	}

}
