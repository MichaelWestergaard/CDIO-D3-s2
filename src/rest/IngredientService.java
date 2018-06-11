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

import controller.IngredientController;
import datalag.MySQLController;
import datalag.ProductBatchDTO;
import datalag.ResponseHandler;
import datalag.IngredientDTO;
import datalag.IngBatchDAO;
import datalag.IngBatchDTO;
import datalag.IngredientDAO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;

@Path("ingredient")
public class IngredientService extends ResponseHandler {
	//private IngredientController ctrl = new IngredientController();  //nyt
	private IngredientController ingredientController;
	private IngredientDAO ingDAO = new IngredientDAO();
	private IngBatchDAO ingBatchDAO = new IngBatchDAO();
	
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
		try {
			return ingredientController.getIngredientList();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		
		return null;
	}
	
	@GET
	@Path("getIngredient")
	public String getIngredient(@QueryParam("ingredientID") int ingredientID) {
		try {
			return ingredientController.getIngredient(ingredientID);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;	
	}
	
	@POST
	@Path("editIngredient")
	public String editIngredient(@FormParam("ingredientID") int ingredientID, @FormParam("ingredientName") String ingredientName) {
		try {
			return ingredientController.editIngredient(ingredientID, ingredientName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;			
	}
		
	//Råvarebatchliste
	@GET
	@Path("getIngBatchList")
	public String getIngBatchList() {
		try {
			ingredientController.getIngBatchList();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		
		return null;
	}	

	//Tilføj en Råvare
	@POST
	@Path("createIngredient")
	public String createIngredient(@FormParam("ingredientID") int ingredientID, @FormParam("ingredientName") String ingredientName, @Context ServletContext context) throws IOException  {
		try {
			return ingredientController.createIngredient(ingredientID, ingredientName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@POST
	@Path("createIngredientBatch")
	public String createIngBatch(@FormParam("ingredientBatchID") int ingredientBatchID, @FormParam("ingredientID") int ingredientID, @FormParam("amount") double amount, @FormParam("supplier") String supplier, @Context ServletContext context) throws IOException  {
		try {
			ingredientController.createIngBatch(ingredientBatchID, ingredientID, amount, supplier);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	@GET
	@Path("getIngBatch")
	public String getIngBatch(@QueryParam("ingBatchID") int ingBatchID) {
		try {
			return ingredientController.getIngBatch(ingBatchID);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;		
	}
	
	@GET
	@Path("getIngredientBatchesByIngredient")
	public String getIngredientBatchesByIngredient(@QueryParam("ingredientID") int ingredientID) {
		try {
			return createResponse("success", 1, new Gson().toJson(mySQLController.getIngredientBatchesByIngredient(ingredientID)));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

}