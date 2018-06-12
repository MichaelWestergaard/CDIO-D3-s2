package controller;

import java.sql.SQLException;

import com.google.gson.Gson;

import datalag.ReceptDAO;
import datalag.ProductBatchDAO;
import datalag.ProductBatchDTO;
import datalag.ResponseHandler;

public class ProductBatchController extends ResponseHandler {
	
	private ProductBatchDAO pbDAO = new ProductBatchDAO();
	private ReceptDAO receptDAO = new ReceptDAO();
	
	public String getProductBatch(int productBatchID) {		
		try {
			return createResponse("success", 1, new Gson().toJson(pbDAO.read(productBatchID)));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}		
	}
	
	public String getProductBatchList() {
		try {
			return createResponse("success", 1, new Gson().toJson(pbDAO.list()));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}
	
	public String createProductBatch(int productBatchID, int status, int receptID) {
		if(productBatchID >= 1 && productBatchID <= 99999999) {
			if(status == 0 || status == 1 || status == 2) {
				if(receptID >= 1 && receptID <= 99999999) {
					ProductBatchDTO pbDTO = new ProductBatchDTO(productBatchID, status, receptID, null, null);
					
					try {
						if(receptDAO.read(receptID) != null && pbDAO.read(productBatchID) == null) {
							if(pbDAO.create(pbDTO)) {
								if(pbDAO.read(productBatchID) != null) {
									return createResponse("success", 1, "ProductBatchen med Recepten \"" + receptDAO.read(receptID).getReceptName() + "\" blev oprettet");
								}
							}
						}
					} catch (ClassNotFoundException e) {
						return createResponse("error", 0, e.getMessage());
					} catch (SQLException e) {
						return createResponse("error", e.getErrorCode(), e.getMessage());
					}	
				} else {
					return createResponse("error", 0, "Recept ID skal være i mellem 1-99999999!");
				}
			} else {
				return createResponse("error", 0, "Status skal enten være 0, 1 eller 2!");
			}
		} else {
			return createResponse("error", 0, "Produktbatch ID skal være i mellem 1-99999999!");
		}
		
		return createResponse("error", 0, "ProduktBatch kunne ikke oprettes");
	}

	
}
