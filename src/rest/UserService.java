package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import datalag.MySQLController;
import datalag.ResponseHandler;
import datalag.UserDTO;

@Path("user")
public class UserService extends ResponseHandler {
	
	private MySQLController mySQLController;
	
	public UserService() {
		try {
			mySQLController = new MySQLController();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Login
	@POST
	@Path("login")
	public String login(@FormParam("username") String username) {		
		try {
			List<UserDTO> users = mySQLController.getUsers();
			
			for (UserDTO user : users) {
				if(user.getUserName().equals(username)) {
					return createResponse("success", 1, String.valueOf(user.getUserID()));
				}
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		
		return createResponse("error", 0, "Du kunne ikke logge ind, prøv igen.");
	}
	
	//Tilføj en bruger
	@POST
	@Path("createUser")
	public String createUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("role") List<String> role, @FormParam("active") int active, @Context ServletContext context)  {
		try {
			if(mySQLController.createUser(userID, userName, firstName, lastName, CPR, password, role, active)) {
				UserDTO createdUser = mySQLController.getUser(userID);
					
				if(createdUser != null) {
					return createResponse("success", 1, "Brugeren \"" + createdUser.getUserName() + "\" blev oprettet");
				}
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke oprette brugeren");
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
	public String getUser(@QueryParam("userID") int userID) {
		try {
			String json = new Gson().toJson(mySQLController.getUser(userID));
			return json;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	//Slette UserDTO createdUser = userDAO.getUser(userID);
	@GET
	@Path("deleteUser")
	public String deleteUser(@QueryParam("userID") int userID) {
		try {
			mySQLController.deleteUser(userID);
			return "true";
						
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "false";
	}
	
	//Opdatere
	@POST
	@Path("updateUser")
	public String updateUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("role") String role, @FormParam("active") int active) throws IOException  {
		try {
			boolean state = mySQLController.updateUser(userID, userName, firstName, lastName, CPR, password, role, active); 
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

	//Ikke testet
	@POST
	@Path("resetPassword")
	public String resetPassword(@FormParam("userID") int userID, @FormParam("password") String password) {
		try {
			boolean state = mySQLController.resetPassword(userID, password);
			if(state) {
				return "true";
			} else {
				return "false";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "false";
	}
	
	
}
