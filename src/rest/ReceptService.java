package rest;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import controller.ReceptController;
import datalag.ResponseHandler;

@Path("recept")
public class ReceptService extends ResponseHandler{
	
	private ReceptController controller = new ReceptController();
	
	@GET
	@Path("getReceptList")
	public String getReceptList() {
		return controller.getReceptList();
	}
	
	@POST
	@Path("createReceptComponent")
	public String createReceptComponent(@FormParam("receptID") int receptID, @FormParam("ingredientID") int ingredientID, @FormParam("nomNetto") double nomNetto, @FormParam("tolerance") double tolerance, @Context ServletContext context) throws IOException  {
		return controller.createReceptComponent(receptID, ingredientID, nomNetto, tolerance);
	}

	@GET
	@Path("getRecept")
	public String getRecept(@QueryParam("receptID") int receptID) {
		return controller.getRecept(receptID);
	}
			
	@POST
	@Path("createRecept")
	public String createRecept(@FormParam("receptID") int receptID, @FormParam("receptName") String receptName, @Context ServletContext context) throws IOException  {
		return controller.createRecept(receptID, receptName);
	}

	@GET
	@Path("getReceptComponentList")
	public String getReceptComponentList() {
		return controller.getReceptComponentList();
	}	
		
}
