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
			e.printStackTrace();
		}
	}

	@POST
	@Path("login")
	public String login(@FormParam("username") String username) {		
		try {
			List<UserDTO> users = mySQLController.getUsers();
			for (UserDTO user : users) {
				if(user.getUserName().equalsIgnoreCase(username)) {
					if(user.getActive() == 1) {
						return createResponse("success", 1, String.valueOf(user.getUserID()));
					} else {
						return createResponse("error", 0, "Brugeren er inaktiv!");
					}
				}
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		
		return createResponse("error", 0, "Du kunne ikke logge ind, prøv igen.");
	}
	
	@POST
	@Path("createUser")
	public String createUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("initial") String initial, @FormParam("role") List<String> role, @FormParam("active") int active, @Context ServletContext context)  {
		try {
			//Validering af data
			if(userID >= 1 && userID <= 999) {
				if(userName.length() >= 2 && userName.length() <= 20) {
					if(CPR.length() == 11) {
						String[] splitCPR = CPR.split("-");
						String combinedCPR = splitCPR[0] + splitCPR[1];
						char[] cprChars = combinedCPR.toCharArray();
						int cprDay = Integer.parseInt(new StringBuilder().append(cprChars[0]).append(cprChars[1]).toString());
						int cprMonth = Integer.parseInt(new StringBuilder().append(cprChars[2]).append(cprChars[3]).toString());
						int cprYear = Integer.parseInt(new StringBuilder().append(cprChars[4]).append(cprChars[5]).toString());

						if(cprDay > 0 && cprDay < 32 && cprMonth > 0 && cprMonth < 13 && cprYear >= 0 && cprYear <= 99) {
							List<UserDTO> users = mySQLController.getUsers();
							for (UserDTO user : users) {
								if(user.getCpr().equals(CPR)) {
									return createResponse("error", 0, "CPR-nummeret findes allerede!");
								}
							}
						} else {
							return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
						}
					} else {
						return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
					}
					
					if(initial.length() >= 2 && initial.length() <= 4) {
						List<UserDTO> users = mySQLController.getUsers();
						for (UserDTO user : users) {
							if(user.getInitial().equals(initial)) {
								return createResponse("error", 0, "Initialerne findes allerede!");
							}
						}
					} else {
						return createResponse("error", 0, "Initialer skal være 2-4 tegn!");
					}
				} else {
					return createResponse("error", 0, "Brugernavnet skal være 2-20 tegn!");
				}
			} else {
				return createResponse("error", 0, "Bruger ID skal være i mellem 1-999!");
			}
			if(mySQLController.getUser(userID) == null) {
				if(mySQLController.createUser(userID, userName, firstName, lastName, CPR, password, initial, role, active)) {
					UserDTO createdUser = mySQLController.getUser(userID);
						
					if(createdUser != null) {
						return createResponse("success", 1, "Brugeren \"" + createdUser.getUserName() + "\" blev oprettet");
					}
				}
			} else {
				return createResponse("error", 0, "Bruger ID'et er taget");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke oprette brugeren");
	}
	
	@GET 
	@Path("getUserList")
	public String getUserList() {
		try {
			return createResponse("success", 1, new Gson().toJson(mySQLController.getUsers()));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

	@GET
	@Path("getUser")
	public String getUser(@QueryParam("userID") int userID) {
		try {
			return createResponse("success", 1, new Gson().toJson(mySQLController.getUser(userID)));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

	//Skal ændres til at ændre status
	@GET
	@Path("changeStatus")
	public String changeStatus(@QueryParam("userID") int userID) {
		try {
			UserDTO user = mySQLController.getUser(userID);
			if(user != null) {
				
				int newStatus = ((user.getActive() == 0) ? 1 : 0);
				if(mySQLController.changeStatus(userID, newStatus)) {
					String msg = "";
					if(newStatus == 0) {
						msg = "Brugeren er hermed inaktiveret!";
					} else {
						msg = "Brugeren blev aktiveret igen!";
					}
					return createResponse("success", 1, msg);
				}
			} else {
				return createResponse("error", 0, "Brugeren findes ikke");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke ændre statussen");
	}
	
	@POST
	@Path("updateUser")
	public String updateUser(@FormParam("userID") int userID, @FormParam("userName") String userName, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("CPR") String CPR, @FormParam("password") String password, @FormParam("initial") String initial, @FormParam("role") String role, @FormParam("active") int active) throws IOException  {
		try {
			if(userName.length() >= 2 && userName.length() <= 20) {
				if(CPR.length() == 11) {
					String[] splitCPR = CPR.split("-");
					String combinedCPR = splitCPR[0] + splitCPR[1];
					char[] cprChars = combinedCPR.toCharArray();
					int cprDay = Integer.parseInt(new StringBuilder().append(cprChars[0]).append(cprChars[1]).toString());
					int cprMonth = Integer.parseInt(new StringBuilder().append(cprChars[2]).append(cprChars[3]).toString());
					int cprYear = Integer.parseInt(new StringBuilder().append(cprChars[4]).append(cprChars[5]).toString());
					
					if(cprDay > 0 && cprDay < 32 && cprMonth > 0 && cprMonth < 13 && cprYear >= 0 && cprYear <= 99) {
						List<UserDTO> users = mySQLController.getUsers();
						for (UserDTO user : users) {
							if(user.getCpr().equals(CPR) && user.getUserID() != userID) {
								return createResponse("error", 0, "CPR-nummeret findes allerede!");
							}
						}
					} else {
						return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
					}
				} else {
					return createResponse("error", 0, "CPR-nummeret er ugyldigt!");
				}
			} else {
				return createResponse("error", 0, "Brugernavnet skal være 2-20 tegn!");
			}
			
			if(mySQLController.updateUser(userID, userName, firstName, lastName, initial, CPR, password, role, active)) {
				return createResponse("success", 1, "Brugeren blev opdateret");
			} else {
				return createResponse("error", 1, "Brugeren blev ikke opdateret");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

	@POST
	@Path("resetPassword")
	public String resetPassword(@FormParam("userID") int userID, @FormParam("password") String password) {
		try {
			if(mySQLController.resetPassword(userID, password)) {
				return createResponse("success", 1, "Adgangskoden blev opdateret");
			} else {
				return createResponse("error", 1, "Adgangskoden blev ikke opdateret");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}
}
