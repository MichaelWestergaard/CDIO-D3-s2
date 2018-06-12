package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;

import controller.ProductBatchController;
import datalag.IngBatchDTO;
import datalag.IngredientDTO;
import datalag.MySQLController;
import datalag.ProductBatchComponentDTO;
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
		
		private ProductBatchController productBatchController;
		
		public ProductService() {
			
		}
		
		//ProduktBatchkomponent-Liste
		@GET
		@Path("getProductBatchComponentList")
		public String getProductBatchComponentList() {
			try {
				return createResponse("success", 1, new Gson().toJson(mySQLController.getProductBatchComponents()));
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
		}
		
		//Tilføj en ProduktBatchKomponent. Der skal laves en fejl besked
		@POST
		@Path("createProductBatchComponent")
		public String createProductBatchComponent(@FormParam("productBatchID") int productBatchID, @FormParam("raavareBatchID") int raavareBatchID, @FormParam("operatorID") int operatorID, @FormParam("netto") double netto, @FormParam("tara") double tara, @Context ServletContext context) throws IOException  {
			if(productBatchID < 1 || productBatchID > 99999999) {
				return createResponse("error", 0, "ProduktBatch ID skal være mellem 1 - 99999999");
			}
			
			if(raavareBatchID < 1 || raavareBatchID > 99999999) {
				return createResponse("error", 0, "RåvareBatch ID skal være mellem 1 - 99999999");
			}
			
			if(operatorID < 1 || operatorID > 99999999) {
				return createResponse("error", 0, "Operatør ID skal være mellem 1 - 99999999");
			}
			
			if(netto < 0.05 || netto > 20.0) {
				return createResponse("error", 0, "Nettovægten skal være mellem 0.05 - 20.0");
			}
			
			if(tara < 0.1 || tara > 10.0) {
				return createResponse("error", 0, "Tara skal være mellem 0.1 - 10.0");
			}
			
			try {
				List<ProductBatchDTO> productBatches = mySQLController.getProductBatches();
				List<IngBatchDTO> ingBatches = mySQLController.getIngBatches();
				List<UserDTO> users = mySQLController.getUsers();
				boolean productBatchFound = false;
				boolean ingredientBatchFound = false;		
				boolean userFound = false;
				
				if(mySQLController.getProductBatchComponent(productBatchID, raavareBatchID) != null) {
					return createResponse("error", 0, "ProdukBatchkomponenten eksisterer allerede");
				}
				
				for (ProductBatchDTO productBatch : productBatches) {
					if(mySQLController.getProductBatch(productBatchID) != null) {
						productBatchFound = true;
					}
				}	
				for (IngBatchDTO ingBatch : ingBatches) {
					if(mySQLController.getIngBatch(raavareBatchID) != null) {
						ingredientBatchFound = true;
					}
				}
				for (UserDTO user : users) {
					if(mySQLController.getUser(operatorID) != null) {
						userFound = true;
					}
				}
				
				if(productBatchFound && ingredientBatchFound && userFound) {					
					mySQLController.createProductBatchComponent(productBatchID, raavareBatchID, operatorID, netto, tara);
		
				} else if (!productBatchFound) {
					return createResponse("error", 0, "productBatchID eksistere ikke");
				} else if (!ingredientBatchFound) {
					return createResponse("error", 0, "raavareBatchID eksistere ikke");
				} else if (!userFound) {
					return createResponse("error", 0, "operatorID eksistere ikke");
				}
				
				if(mySQLController.getProductBatchComponent(productBatchID, raavareBatchID) != null) {
					ProductBatchComponentDTO createdProductBatchComponent = mySQLController.getProductBatchComponent(productBatchID, raavareBatchID);
					return createResponse("success", 1, "ProduktBatchkomponenten med produktbatchen \"" + createdProductBatchComponent.getProductBatchID() + "\" blev oprettet");

				} else {
					return createResponse("error", 0, "Kunne ikke oprette produktbatchkomponenten");
				}
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
		}
		
		@GET
		@Path("getProductBatchComponent")
		public String getProductBatchComponent(@QueryParam("productBatchID") int productBatchID, @QueryParam("raavareBatchID") int raavareBatchID) {
			try {
				return createResponse("success", 1, new Gson().toJson(mySQLController.getProductBatchComponent(productBatchID, raavareBatchID)));
			} catch (SQLException e) {
				return createResponse("error", e.getErrorCode(), e.getMessage());
			}
		
		}
		
		//ProduktBatch-Liste
		@GET
		@Path("getProductBatchList")
		public String getProductBatchList() {
			return productBatchController.getProductBatchList();
		}
		
		@POST
		@Path("createProductBatch")
		public String createProductBatch(@FormParam("productBatchID") int productBatchID, @FormParam("status") int status, @FormParam("receptID") int receptID, @Context ServletContext context) throws IOException  {
			return productBatchController.createProductBatch(productBatchID, status, receptID);
		}
		
		@GET
		@Path("getProductBatch")
		public String getProductBatch(@QueryParam("productBatchID") int productBatchID) {
			return productBatchController.getProductBatch(productBatchID);
		
		}
		
	}