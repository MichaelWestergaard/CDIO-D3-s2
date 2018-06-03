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
import datalag.ProductBatchDTO;
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


	@Path("Product")
	public class ProductService extends ResponseHandler{

		private MySQLController mySQLController;
		
		public ProductService() {
			try {
				mySQLController = new MySQLController();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		@POST
		@Path("createProductBatch")
		public String createProductBatch(@FormParam("productBatchID") int productBatchID, @FormParam("status") int status, @FormParam("receptID") int receptID, @Context ServletContext context) throws IOException  {
			try {
				
				//Validering af data
				if(productBatchID >= 1 && productBatchID <= 99999999) {
					if(status == 0 || status == 1 || status == 2) {
						if(receptID >= 1 && receptID <= 99999999) {
							//All good
						} else {
							return createResponse("error", 0, "Recept ID skal være i mellem 1-99999999!");
						}
					} else {
						return createResponse("error", 0, "Status skal enten være 0, 1 eller 2!");
					}
				} else {
					return createResponse("error", 0, "Produktbatch ID skal være i mellem 1-99999999!");
				}
				
				if(mySQLController.getRecept(receptID) != null && mySQLController.getProductBatch(productBatchID) == null) {
					if(mySQLController.createProductBatch(productBatchID, status, receptID)) {
				ProductBatchDTO createdProductBatch = mySQLController.getProductBatch(productBatchID);
				
				if(createdProductBatch != null) {
					return createResponse("success", 1, "ProductBatchen med Recepten \"" + mySQLController.getRecept(createdProductBatch.getReceptID()).getReceptName() + "\" blev oprettet");
				}
					}
				}
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
			return createResponse("error", 0, "Kunne ikke oprette ProductBatchen");
		}
		
		@GET
		@Path("getProductBatch")
		public String getProductBatch(@QueryParam("productBatchID") int productBatchID) {
			try {
				return createResponse("success", 1, new Gson().toJson(mySQLController.getProductBatch(productBatchID)));
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
		
		}
}
