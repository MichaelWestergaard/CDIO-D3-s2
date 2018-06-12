package controller;

import java.sql.SQLException;
import com.google.gson.Gson;

import datalag.IngBatchDAO;
import datalag.IngredientDAO;
import datalag.ProductBatchComponentDAO;
import datalag.ProductBatchComponentDTO;
import datalag.ProductBatchDAO;
import datalag.ProductBatchDTO;
import datalag.ReceptDAO;
import datalag.ResponseHandler;

public class ProductBatchController extends ResponseHandler {
	
	private ProductBatchDAO pbDAO = new ProductBatchDAO();
	private ProductBatchComponentDAO pbcDAO = new ProductBatchComponentDAO();
	private ReceptDAO receptDAO = new ReceptDAO();
	private IngBatchDAO ingBatchDAO = new IngBatchDAO();
	private UserController userController = new UserController();
	
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
						if(ingBatchDAO.read(receptID) != null && pbDAO.read(productBatchID) == null) {
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

	public String getProductBatchComponent(int pbID) {
		try {
			return createResponse("success", 1, new Gson().toJson(pbcDAO.read(pbID)));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

	public String getProductBatchComponentList() {
		try {
			return createResponse("success", 1, new Gson().toJson(pbcDAO.list()));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

	public String createProductBatchComponent(int productBatchID, int ingredientBatchID, int operatorID, double netto, double tara) {
		if(productBatchID < 1 || productBatchID > 99999999) {
			return createResponse("error", 0, "ProduktBatch ID skal være mellem 1 - 99999999");
		}
		
		if(ingredientBatchID < 1 || ingredientBatchID > 99999999) {
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
			boolean productBatchFound = false;
			boolean ingredientBatchFound = false;		
			boolean userFound = false;
			
			if(pbcDAO.read(productBatchID) != null) {
				return createResponse("error", 0, "ProdukBatchkomponenten eksisterer allerede");
			}
			
			if(pbDAO.read(productBatchID) != null) {
				productBatchFound = true;
			}	
			
			if(ingBatchDAO.read(ingredientBatchID) != null) {
				ingredientBatchFound = true;
			}
			
			if(userController.getUser(operatorID) != null) {
				userFound = true;
			}
			
			if(productBatchFound && ingredientBatchFound && userFound) {
				ProductBatchComponentDTO pbc = new ProductBatchComponentDTO(productBatchID, ingredientBatchID, operatorID, null, operatorID, null, netto, tara);
				pbcDAO.create(pbc);
	
			} else if (!productBatchFound) {
				return createResponse("error", 0, "productBatchID eksistere ikke");
			} else if (!ingredientBatchFound) {
				return createResponse("error", 0, "raavareBatchID eksistere ikke");
			} else if (!userFound) {
				return createResponse("error", 0, "operatorID eksistere ikke");
			}
			
			if(pbcDAO.read(productBatchID) != null) {
				return createResponse("success", 1, "ProduktBatchkomponenten med produktbatchen \"" + productBatchID + "\" blev oprettet");

			} else {
				return createResponse("error", 0, "Kunne ikke oprette produktbatchkomponenten");
			}
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} 
	}
}
