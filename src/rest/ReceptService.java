package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;

import datalag.IngredientDTO;
import datalag.MySQLController;
import datalag.ReceptComponentDTO;
import datalag.ReceptDTO;
import datalag.UserDTO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;

@Path("recept")
public class ReceptService {

	private MySQLController mySQLController;
	
	public ReceptService() {
		try {
			mySQLController = new MySQLController();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Receptliste
	@GET
	@Path("getReceptList")
	public String getReceptList() {
		String returnMsg = "";
		
		try {
			List<ReceptDTO> recepter = mySQLController.getRecepter();
			String json = new Gson().toJson(recepter);
			returnMsg = json;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnMsg;
	}
	
	//Tilføj en ReceptKomponent, der skal laves en fejl besked
		@POST
		@Path("createReceptComponent")
		public Response createReceptComponent(@FormParam("receptID") int receptID, @FormParam("ingredientID") int ingredientID, @FormParam("nomNetto") double nomNetto, @FormParam("tolerance") double tolerance, @Context ServletContext context) throws IOException  {
			try {
				
				List<ReceptDTO> recepts = mySQLController.getRecepter();
				List<IngredientDTO> ingredients = mySQLController.getIngredients();
				boolean receptFound = false;
				boolean ingredientFound = false;		
						
				for (ReceptDTO recept : recepts) {
					if(mySQLController.getRecept() != null) {
						receptFound = true;
					}
				}	
				for (IngredientDTO ingredient : ingredients) {
					if(mySQLController.getIngredient(ingredientID) != null) {
						ingredientFound = true;
					}
				}
				if(receptFound && ingredientFound) {
				
					
					
				mySQLController.createReceptComponent(receptID, ingredientID, nomNetto, tolerance);
		
				}
				
				if(mySQLController.getReceptComponent(receptID, ingredientID) != null) {
					UriBuilder builder = UriBuilder.fromPath(context.getContextPath());
			        builder.path("index.html");
			        return Response.seeOther(builder.build()).build();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UriBuilder builder = UriBuilder.fromPath(context.getContextPath());
			builder.path("index.html");
			return Response.seeOther(builder.build()).build();
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
