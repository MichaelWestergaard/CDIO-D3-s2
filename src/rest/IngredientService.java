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

import datalag.MySQLController;
import datalag.IngredientDTO;
import datalag.IngBatchDTO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;

@Path("ingredient")
public class IngredientService {

	private MySQLController mySQLController;
	
	public IngredientService() {
		try {
			mySQLController = new MySQLController();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Råvareliste
	@GET
	@Path("getIngredientList")
	public String getIngredientList() {
		String returnMsg = "";
		
		try {
			List<IngredientDTO> ingredients = mySQLController.getIngredients();
			String json = new Gson().toJson(ingredients);
			returnMsg = json;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnMsg;
	}
	
	@GET
	@Path("getIngredient")
	public String getIngredient(@QueryParam("ingredientID") int ingredientID) {
		String returnMsg = "";
		
		try {
			IngredientDTO ingredient = mySQLController.getIngredient(ingredientID);
			String json = new Gson().toJson(ingredient);
			returnMsg = json;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return returnMsg;
	}
	
	
	
	
	//Råvarebatchliste
	@GET
	@Path("getIngBatchList")
	public String getIngBatchList() {
		String returnMsg = "";
			
		try {
			List<IngBatchDTO> ingBatches = mySQLController.getIngBatches();
			String json = new Gson().toJson(ingBatches);
			returnMsg = json;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return returnMsg;
	}	

	//Tilføj en Råvare
	@POST
	@Path("createIngredient")
	public Response createIngredient(@FormParam("ingredientID") int ingredientID, @FormParam("ingredientName") String ingredientName, @FormParam("supplier") String supplier, @Context ServletContext context) throws IOException  {
		try {
			mySQLController.createIngredient(ingredientID, ingredientName, supplier);
			IngredientDTO createIngredient = mySQLController.getIngredient(ingredientID);
			
			if(createIngredient != null) {
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

