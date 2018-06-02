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
import datalag.ResponseHandler;
import datalag.UserDTO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;

@Path("recept")
public class ReceptService extends ResponseHandler{

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
		try {
			return createResponse("success", 1, new Gson().toJson(mySQLController.getRecepter()));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}
	
	//Tilføj en ReceptKomponent, der skal laves en fejl besked
		@POST
		@Path("createReceptComponent")
		public String createReceptComponent(@FormParam("receptID") int receptID, @FormParam("ingredientID") int ingredientID, @FormParam("nomNetto") double nomNetto, @FormParam("tolerance") double tolerance, @Context ServletContext context) throws IOException  {
			try {
				List<ReceptDTO> recepts = mySQLController.getRecepter();
				List<IngredientDTO> ingredients = mySQLController.getIngredients();
				boolean receptFound = false;
				boolean ingredientFound = false;		
						
				for (ReceptDTO recept : recepts) {
					if(mySQLController.getRecept(receptID) != null) {
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
		
				} else if (!receptFound && ingredientFound ) {
					return createResponse("error", 0, "ReceptID eksistere ikke");
				} else if (receptFound && !ingredientFound ) {
					return createResponse("error", 0, "ingredientID eksistere ikke");
				}
				
				if(mySQLController.getReceptComponent(receptID, ingredientID) == null) {
					ReceptComponentDTO createdReceptComponent = mySQLController.getReceptComponent(receptID, ingredientID);
					
					if(createdReceptComponent != null) {
						return createResponse("success", 1, "RecepKomponenten med Recepten \"" + mySQLController.getRecept(createdReceptComponent.getReceptID()).getReceptName() + "\" blev oprettet");
					}
				} else {
					return createResponse("error", 0, "ReceptKomponenten eksitere allereade");
				}
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
			return createResponse("error", 0, "Kunne ikke oprette ReceptKomponenten");
		}
	
		@GET
		@Path("getRecept")
		public String getRecept(@QueryParam("receptID") int receptID) {
			try {
				return createResponse("success", 1, new Gson().toJson(mySQLController.getRecept(receptID)));
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
		
		}

		@POST
		@Path("createRecept")
		public String createRecept(@FormParam("receptID") int receptID, @FormParam("receptName") String receptName, @Context ServletContext context) throws IOException  {
			try {
				if(mySQLController.createRecept(receptID, receptName)) {
				ReceptDTO createdRecept = mySQLController.getRecept(receptID);
				
				if(createdRecept != null) {
					return createResponse("success", 1, "Recepten \"" + createdRecept.getReceptName() + "\" blev oprettet");
				}
				}
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
			return createResponse("error", 0, "Kunne ikke oprette Recepten");
		}
	
		//Receptkomponentliste
		@GET
		@Path("getReceptComponentList")
		public String getReceptComponentList() {
			try {
				return createResponse("success", 1, new Gson().toJson(mySQLController.getReceptComponents()));
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
		}	
		
}
