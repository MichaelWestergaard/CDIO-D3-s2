package rest;

import java.io.IOException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import controller.IngredientController;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;

@Path("ingredient")
public class IngredientService {
	
	private IngredientController ingredientController = new IngredientController();
	
	//Råvareliste
	@GET
	@Path("getIngredientList")
	public String getIngredientList() {		
		return ingredientController.getIngredientList();
	}
	
	@GET
	@Path("getIngredient")
	public String getIngredient(@QueryParam("ingredientID") int ingredientID) {
		return ingredientController.getIngredient(ingredientID);
	}
	
	@POST
	@Path("editIngredient")
	public String editIngredient(@FormParam("ingredientID") int ingredientID, @FormParam("ingredientName") String ingredientName) {
		return ingredientController.editIngredient(ingredientID, ingredientName);
	}
		
	//Råvarebatchliste
	@GET
	@Path("getIngBatchList")
	public String getIngBatchList() {
		return ingredientController.getIngBatchList();
	}

	//Tilføj en Råvare
	@POST
	@Path("createIngredient")
	public String createIngredient(@FormParam("ingredientID") int ingredientID, @FormParam("ingredientName") String ingredientName, @Context ServletContext context) throws IOException  {
		return ingredientController.createIngredient(ingredientID, ingredientName);		
	}
	
	@POST
	@Path("createIngredientBatch")
	public String createIngBatch(@FormParam("ingredientBatchID") int ingredientBatchID, @FormParam("ingredientID") int ingredientID, @FormParam("amount") double amount, @FormParam("supplier") String supplier, @Context ServletContext context) throws IOException  {
		return ingredientController.createIngBatch(ingredientBatchID, ingredientID, amount, supplier);
		
	}
	
	
	@GET
	@Path("getIngBatch")
	public String getIngBatch(@QueryParam("ingBatchID") int ingBatchID) {
		return ingredientController.getIngBatch(ingBatchID);
				
	}
	
	@GET
	@Path("getIngredientBatchesByIngredient")
	public String getIngredientBatchesByIngredient(@QueryParam("ingredientID") int ingredientID) {
		return ingredientController.getIngredientBatchesByIngredient(ingredientID);
	}

}