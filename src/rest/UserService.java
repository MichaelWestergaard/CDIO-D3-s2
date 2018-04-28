package rest;

import java.util.List;
import javax.ws.rs.Path;
import datalag.UserDTO;
import datalag.IUserDAO;
import datalag.Roles;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.FormParam;


@Path("user")
public class UserService {

	//Tilføj en bruger
	@POST
	@Path("form")
	public String createUser(@FormParam("UserID") int UserID,@FormParam("UserName") String UserName, @FormParam("FirstName") String FirstName, @FormParam("LastName") String LastName, @FormParam("CPR") String CPR, @FormParam("Password") String Password, @FormParam("Role") Roles Role, @FormParam("Active") boolean Active)  {
		return "user added";
	}

	@GET
	@Path("test")
	public String createUser1()  {
		return "user added";
	}
	

//Få hele brugerlsiten
/*@GET 
public String getUserList() {
List<UserDTO> list = IUserDAO.getInstance().getUserList();
return new JSONArray(list).toString();
}*/

	/*@GET
@PATH("users"
public list<User> getUser() {
return IUserDAO.getAllUsers();

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
