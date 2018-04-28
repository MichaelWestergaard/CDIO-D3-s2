package datalag;

public class UserDTO {
	int userID;                     
	String userName;                    
	String name;
	String lastName;
	String cpr;                 
	String password;
	Roles role;
	boolean active;
	public UserDTO(int userID, String userName, String name, String lastName, String cpr, String password, Roles role,
			boolean active) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.name = name;
		this.lastName = lastName;
		this.cpr = cpr;
		this.password = password;
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
	public Roles getRole() {
		return role;
	}
	public void setRole(Roles role) {
		this.role = role;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
}
