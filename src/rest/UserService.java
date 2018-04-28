package rest;

import java.util.List;
import javax.ws.rs.Path;
import datalag.UserDTO;
import datalag.IUserDAO;
import datalag.Roller;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.FormParam;


@Path("user")
public class UserService {

@POST
@Path("form")
public String createIngredient(@FormParam("BrugerID") int Brugerid,@FormParam("Brugernavn") String Brugernavn, @FormParam("Fornavn") String Fornavn, @FormParam("Efternavn") String Efternavn, @FormParam("CPR") String CPR, @FormParam("Password") String Password, @FormParam("Rolle") Roller Rolle, @FormParam("Aktiv") boolean Aktiv)  {
	return "user added";
}
	
	
}
