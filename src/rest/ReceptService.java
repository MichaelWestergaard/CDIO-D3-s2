package rest;

import java.io.IOException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import controller.ReceptController;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;

@Path("recept")
public class ReceptService {

	private ReceptController receptController;
	
	public ReceptService() {
	}
	
	//Receptliste 
	@GET
	@Path("getReceptList")
	public String getReceptList() {
		return receptController.getReceptList();
	}
	
	//Tilføj en ReceptKomponent, der skal laves en fejl besked
		@POST
		@Path("createReceptComponent")
	public String createReceptComponent(@FormParam("receptID") int receptID, @FormParam("ingredientID") int ingredientID, @FormParam("nomNetto") double nomNetto, @FormParam("tolerance") double tolerance, @Context ServletContext context) throws IOException  {
		return receptController.createReceptComponent(receptID, ingredientID, nomNetto, tolerance);
	}

	@GET
	@Path("getRecept")
	public String getRecept(@QueryParam("receptID") int receptID) {
		return receptController.getRecept(receptID);
	
	}
			
	@POST
	@Path("createRecept")
	public String createRecept(@FormParam("receptID") int receptID, @FormParam("receptName") String receptName, @Context ServletContext context) throws IOException  {
		return receptController.createRecept(receptID, receptName);
	}

	//Receptkomponentliste
	@GET
	@Path("getReceptComponentList")
	public String getReceptComponentList() {
		return receptController.getReceptComponentList();
	}	
	
}
