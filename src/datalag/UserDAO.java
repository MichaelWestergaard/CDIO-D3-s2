package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datalag.UserDTO;

public class UserDAO implements IUserDAO {
	
	private List<UserDTO> users = new ArrayList<UserDTO>();
	
	public List<UserDTO> getUserList() {
		return users;
	}
	
	public UserDTO getUser(int userID) {
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getUserID()==userID) {
				return users.get(i);
			}
		}
		return null;
	}

	public void createUser(int userID, String userName, String firstName, String lastName, String cpr, String password, List<String> role, int active) {
		if(getUser(userID) == null) {
			UserDTO newUser = new UserDTO(userID, userName, firstName, lastName, cpr, password, role, active);
			users.add(newUser);
		}
	}

	public void updateUser(int userID, String userName, String firstName, String lastName, String cpr, String password, List<String> role, int active) {
		// TODO Auto-generated method stub
		if(users.contains(getUser(userID)))  {
		UserDTO user = getUser(userID);
		user.setActive(active);
		user.setCpr(cpr);
		user.setRole(role);
		user.setPassword(password);
		user.setName(firstName);
		user.setLastName(lastName);
		}
	}

	public boolean deleteUser(int userID) {
		if(users.contains(getUser(userID))) {
			if(users.remove(getUser(userID))) {
				return true;
			}
		}
		return false;
	}

}
