package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.google.gson.Gson;

import datalag.UserDTO;
import datalag.IUserDAO;
import datalag.MySQLController;
import datalag.Roles;
import datalag.UserDAO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;


@Path("user")
public class UserService {

	private MySQLController mySQLController;
	
	public UserService() {
		try {
			mySQLController = new MySQLController();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Tilføj en bruger
	@POST
	@Path("createUser")
	public String createUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("role") List<String> role, @FormParam("active") int active)  {
		try {
			mySQLController.createUser(userID, userName, firstName, lastName, CPR, password, role, active);
			UserDTO createdUser = mySQLController.getUser(userID);
				
			if(createdUser != null) {
				return "user created";
			} else {
				return "error";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error";
	}

	//BRuerliste
	@GET 
	@Path("getUserList")
	public String getUserList() {
		String returnMsg = "";
		try {
			List<UserDTO> users = mySQLController.getUsers();

			String json = new Gson().toJson(users);
			returnMsg = json;
						
//			for (UserDTO user : users) {
//				returnMsg += user.toString() + "\n";
//			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnMsg;
	}

	//få en bruger ud fra id
	@GET
	@Path("getUser")
	public UserDTO getUser(@PathParam("userID") int userID) {
		try {
			return mySQLController.getUser(userID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	//Slette UserDTO createdUser = userDAO.getUser(userID);
	@DELETE
	@Path("deleteUser")
	public String deleteUser(@PathParam("userID") int userID) {
		try {
			boolean state = mySQLController.deleteUser(userID);
			if(state) {
				return "true";
			} else {
				return "false";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	//Opdatere
	@POST
	@Path("updateUser")
	public String updateUser(@FormParam("userID") int userID,@FormParam("userName") String userName, @FormParam("name") String name, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("Password") String Password, @FormParam("role") List<String> role, @FormParam("active") int active) throws IOException  {
		try {
			boolean state = mySQLController.updateUser(userID, userName, name, lastName, CPR, Password, role, active); 
			if(state) {
				return "true";
			} else {
				return "false";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}

}
