package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import controller.UserController;

@Path("user")
public class UserService {
	
	private UserController userController = new UserController();

	@POST
	@Path("login")
	public String login(@FormParam("username") String username) {		
		return userController.login(username);
	}
	
	@POST
	@Path("createUser")
	public String createUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("initial") String initial, @FormParam("role") List<String> role, @FormParam("active") int active, @Context ServletContext context)  {
		return userController.createUser(userID, userName, firstName, lastName, CPR, password, initial, role, active);
	}
	
	@GET 
	@Path("getUserList")
	public String getUserList() {
		return userController.getUsers();
	}

	@GET
	@Path("getUser")
	public String getUser(@QueryParam("userID") int userID) {
		return userController.getUser(userID);
	}

	@GET
	@Path("changeStatus")
	public String changeStatus(@QueryParam("userID") int userID) {
		return userController.changeStatus(userID);
	}
	
	@POST
	@Path("updateUser")
	public String updateUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("initial") String initial, @FormParam("role") String role, @FormParam("active") int active) throws IOException  {
		return userController.updateUser(userID, userName, firstName, lastName, CPR, password, initial, role, active);
	}

	@POST
	@Path("resetPassword")
	public String resetPassword(@FormParam("userID") int userID, @FormParam("password") String password) {
		return userController.resetPassword(userID, password);
	}
}