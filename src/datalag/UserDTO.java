package datalag;

import java.util.List;

public class UserDTO {
	
	int userID;                     
	String userName;                    
	String name;
	String lastName;
	String cpr;                 
	String password;
	String initial;
	List<String> role;
	int active;
	
	public UserDTO(int userID, String userName, String name, String lastName, String cpr, String password, String initial, List<String> role, int active) {
		this.userID = userID;
		this.userName = userName;
		this.name = name;
		this.lastName = lastName;
		this.cpr = cpr;
		this.password = password;
		this.initial = initial;
		this.role = role;
		this.active = active;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getInitial() {
		return initial;
	}

	public void setInitial(String initial) {
		this.initial = initial;
	}

	public String getCpr() {
		return cpr;
	}

	public void setCpr(String cpr) {
		this.cpr = cpr;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return "[userID=" + userID + ", userName=" + userName + ", name=" + name + ", lastName=" + lastName + ", cpr=" + cpr + ", password=" + password + ", initial=" + initial + ", role=" + role + ", active=" + active + "]";
	}
	
	
	
}
