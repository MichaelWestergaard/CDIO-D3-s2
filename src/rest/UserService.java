package rest;

import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import datalag.UserDTO;
import datalag.IUserDAO;
import datalag.Roles;
import datalag.UserDAO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.FormParam;


@Path("user")
public class UserService {
	
	UserDAO userDAO = new UserDAO();

	//Tilføj en bruger
	@POST
	@Path("createUser")
	public String createUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("role") List<String> role, @FormParam("active") int active)  {
		userDAO.createUser(userID, userName, firstName, lastName, CPR, password, role, active);
		
		UserDTO createdUser = userDAO.getUser(userID);
		
		if(createdUser != null) {
			return createdUser.toString();
		} else {
			return "error";
		}
	}

	@GET
	@Path("test")
	public String createUser1()  {
		return "user added";
	}
	
//BRuerliste
@GET 
@Path("getUserList")
public List<UserDTO> getUserList() {
	return userDAO.getUserList();
}

//få en bruger ud fra id
@GET
@Path("getUser")
public UserDTO getUser(@PathParam("userID") int userID) {
return userDAO.getUser(userID);
}


//Slette
@Delete
public string deleteUser(@PathPharam("BrugerID") int BrugerID) {

//Opdatere
@PUT
@Path("form")
public String updateUser(@FormParam("BrugerID") int BrugerID,@FormParam("Brugernavn") String Brugernavn, @FormParam("Fornavn") String Fornavn, @FormParam("Efternavn") String Efternavn, @FormParam("CPR") String CPR, @FormParam("Password") String Password, @FormParam("Rolle") Roller Rolle, @FormParam("Aktiv") boolean Aktiv)  {
	return "user updated";
}
}




	 */	
}
