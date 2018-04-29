package rest;

import java.io.IOException;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import datalag.UserDTO;
import datalag.IUserDAO;
import datalag.Roles;
import datalag.UserDAO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
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
			return "user created";
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


	//Slette UserDTO createdUser = userDAO.getUser(userID);
	@DELETE
	@Path("deleteUser") 
	public String deleteUser(@PathParam("userID") int userID) {
		boolean state = userDAO.deleteUser(userID);
		if(state) {
			return "true";
		} else {
			return "false";
		}
	}
	
	//Opdatere
//	@PUT
//	@Path("updateUser")
//	public String updateUser(@FormParam("userID") int userID,@FormParam("userName") String userName, @FormParam("name") String name, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("Password") String Password, @FormParam("role") List<String> role, @FormParam("active") int active) throws IOException  {
//		UserDTO user = new UserDTO(userID, userName, name, lastName, CPR, Password, role, active); //Brugernavn, fornavn, efternavn, password, rolle og active skal kunne opdateres
//		int updatedUser = userDAO.updateUser(user); 
//		if(updatedUser == 1) {
//			return "Result success";
//		}
//		return "Result failed";
//	}

}
