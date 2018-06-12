package rest;

import java.io.IOException;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import controller.ProductBatchController;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;


	@Path("Product")
	public class ProductService {
		
		private ProductBatchController productBatchController;
		
		public ProductService() {
			
		}
		
		//ProduktBatchkomponent-Liste
		@GET
		@Path("getProductBatchComponentList")
		public String getProductBatchComponentList() {
			return productBatchController.getProductBatchComponentList();
		}
		
		//Tilføj en ProduktBatchKomponent. Der skal laves en fejl besked
		@POST
		@Path("createProductBatchComponent")
		public String createProductBatchComponent(@FormParam("productBatchID") int productBatchID, @FormParam("raavareBatchID") int raavareBatchID, @FormParam("operatorID") int operatorID, @FormParam("netto") double netto, @FormParam("tara") double tara, @Context ServletContext context) throws IOException  {
			return productBatchController.createProductBatchComponent(productBatchID, raavareBatchID, operatorID, netto, tara);
		}
		
		@GET
		@Path("getProductBatchComponent")
		public String getProductBatchComponent(@QueryParam("productBatchID") int productBatchID, @QueryParam("raavareBatchID") int raavareBatchID) {
			return productBatchController.getProductBatchComponent(productBatchID, raavareBatchID);
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