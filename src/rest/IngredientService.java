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
	private MySQLController mySQLController;
	private IngredientDAO ingDAO = new IngredientDAO();
	
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
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		
		return returnMsg;
	}
	
	@GET
	@Path("getIngredient")
	public String getIngredient(@QueryParam("ingredientID") int ingredientID) {
		String returnMsg = "";
		
		IngredientDTO ingredient = ingDAO.read(ingredientID);
		String json = new Gson().toJson(ingredient);
		returnMsg = json;
		
		return createResponse("success", 1, new Gson().toJson(ingDAO.read(ingredientID)));
	}
	
	@POST
	@Path("editIngredient")
	public String editIngredient(@FormParam("ingredientID") int ingredientID, @FormParam("ingredientName") String ingredientName) {
		if(ingredientName.length() < 2 || ingredientName.length() > 20) {
			return createResponse("error", 0, "Råvarenavnet skal være mellem 2 - 20 tegn");
		}
		
		String[] parameters = {ingredientName};
		
		if(ingDAO.update(ingredientID, parameters)) {
			return createResponse("success", 1, "Råvaren blev opdateret");
		} else {
			return createResponse("error", 0, "Råvaren kunne ikke opdateres");
		}
			
	}
		
	//Råvarebatchliste
	@GET
	@Path("getIngBatchList")
	public String getIngBatchList() {
		try {
			return createResponse("success", 1, new Gson().toJson(mySQLController.getIngBatches()));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}	

	//Tilføj en Råvare
	@POST
	@Path("createIngredient")
	public String createIngredient(@FormParam("ingredientID") int ingredientID, @FormParam("ingredientName") String ingredientName, @Context ServletContext context) throws IOException  {
		try {
			//Validering af data
			if(ingredientID >= 1 && ingredientID <= 99999999) {
				if(ingredientName.length() >= 2 && ingredientName.length() <= 20) {
					
				} else {
					return createResponse("error", 0, "Råvare navnet skal være 2-20 tegn!");
				}
			} else {
				return createResponse("error", 0, "Råvare ID skal være i mellem 1-99999999!");
			}
			
			String[] parameters = {ingredientName};
			
			if(ingDAO.create(ingredientID, parameters)) {
			
				if(ingDAO.read(ingredientID) != null) {
					return createResponse("success", 1, "Råvaren \"" + ingredientName + "\" blev oprettet");
				}
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke oprette Råvare");
	}
	
	@POST
	@Path("createIngredientBatch")
	public String createIngBatch(@FormParam("ingredientBatchID") int ingredientBatchID, @FormParam("ingredientID") int ingredientID, @FormParam("amount") double amount, @FormParam("supplier") String supplier, @Context ServletContext context) throws IOException  {
		try {
			//Validering af data
			if(ingredientBatchID >= 1 && ingredientBatchID <= 99999999) {
				if(ingredientID >= 1 && ingredientID <= 99999999) {
					if(supplier.length() < 2 || supplier.length() > 20) {
						return createResponse("error", 0, "Leverandørnavnet skal være mellem 2 - 20 tegn");
					}
				} else {
					return createResponse("error", 0, "Råvare ID skal være i mellem 1-99999999!");
				}
			} else {
				return createResponse("error", 0, "Råvarebatch ID skal være i mellem 1-99999999!");
			}
			
			String[] parameters = {Integer.toString(ingredientID), String.valueOf(amount), supplier};
			
			if(ingBatchDAO.create(ingredientID) != null && ingBatchDAO.getIngBatch(ingredientBatchID) == null) {
				
				if(mySQLController.createIngBatch(ingredientBatchID, ingredientID, amount, supplier)) {
					IngBatchDTO createdIngBatch = mySQLController.getIngBatch(ingredientBatchID);
					
					if(createdIngBatch != null) {
						return createResponse("success", 1, "Råvarebatchen med råvaren \"" + mySQLController.getIngredient(createdIngBatch.getIngredientID()).getIngredientName() + "\" blev oprettet");
					}
				}
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
		return createResponse("error", 0, String.valueOf(amount));
	}
	
	
	@GET
	@Path("getIngBatch")
	public String getIngBatch(@QueryParam("ingBatchID") int ingBatchID) {
		try {
			return createResponse("success", 1, new Gson().toJson(mySQLController.getIngBatch(ingBatchID)));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	
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