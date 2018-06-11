package controller;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

import datalag.IngBatchDAO;
import datalag.IngBatchDTO;
import datalag.IngredientDAO;
import datalag.IngredientDTO;
import datalag.ResponseHandler;

public class IngredientController extends ResponseHandler{
//	IngredientDAO ingr = MySQLController.getIngredientDAO();
//	
//	IngredientDTO saveIngredient(IngredientDTO dto) {
//		//Do some validation??
//		ValidationCOntroller??
//		ingr.save(dto);
//	}
	
	private IngredientDAO ingDAO = new IngredientDAO();
	private IngBatchDAO ingBatchDAO = new IngBatchDAO();
	
	public String getIngredient(int ingredientID) {
		try {
			return createResponse("success", 1, new Gson().toJson(ingDAO.read(ingredientID)));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}
	
	public String createIngredient(int ingredientID, String ingredientName) {
		if(ingredientID >= 1 && ingredientID <= 99999999) {
			if(ingredientName.length() >= 2 && ingredientName.length() <= 20) {
				IngredientDTO ingredient = new IngredientDTO(ingredientID, ingredientName);
				
				try {
					if(ingDAO.create(ingredient)) {			
						return createResponse("success", 1, "Råvaren \"" + ingredientName + "\" blev oprettet");
					}
				} catch (ClassNotFoundException e) {
					return createResponse("error", 0, e.getMessage());
				} catch (SQLException e) {
					return createResponse("error", e.getErrorCode(), e.getMessage());
				}
			} else {
				return createResponse("error", 0, "Råvare navnet skal være 2-20 tegn!");
			}
		} else {
			return createResponse("error", 0, "Råvare ID skal være i mellem 1-99999999!");
		}
		
		return createResponse("error", 0, "Kunne ikke oprette Råvare");
	}

	public String editIngredient(int ingredientID, String ingredientName) {
		if(ingredientName.length() < 2 || ingredientName.length() > 20) {
			return createResponse("error", 0, "Råvarenavnet skal være mellem 2 - 20 tegn");
		}
		
		IngredientDTO ingredient = new IngredientDTO(ingredientID, ingredientName);
		
		try {
			if(ingDAO.update(ingredient)) {
				return createResponse("success", 1, "Råvaren blev opdateret");
			} else {
				return createResponse("error", 0, "Råvaren kunne ikke opdateres");
			}
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

	public String getIngBatch(int ingBatchID) {
		try {
			return createResponse("success", 1, new Gson().toJson(ingBatchDAO.read(ingBatchID)));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}
	
	public String createIngBatch(int ingBatchID, int ingredientID, double amount, String supplier) {
		if(ingredientID >= 1 && ingredientID <= 99999999) {
			if(supplier.length() < 2 || supplier.length() > 20) {
				return createResponse("error", 0, "Leverandørnavnet skal være mellem 2 - 20 tegn");
			} else {
				IngBatchDTO ingBatch = new IngBatchDTO(ingBatchID, ingredientID, amount, "", supplier);
				
				try {
					if(ingBatchDAO.create(ingBatch)) {
						return createResponse("success", 1, "Råvarebatchen med råvaren \"" + ingDAO.read(ingredientID).getIngredientName() + "\" blev oprettet");
					}
				} catch (ClassNotFoundException e) {
					return createResponse("error", 0, e.getMessage());
				} catch (SQLException e) {
					return createResponse("error", e.getErrorCode(), e.getMessage());
				}
			}
		} else {
			return createResponse("error", 0, "Råvare ID skal være i mellem 1-99999999!");
		}
		
		return createResponse("error", 0, String.valueOf(amount));
	}

	public String getIngredientList() {
		String returnMsg = "";
				
		try {
			List<IngredientDTO> ingredients;
			ingredients = ingDAO.list();
			String json = new Gson().toJson(ingredients);
			returnMsg = json;
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}		
		
		return returnMsg;
	}

	public String getIngBatchList() {
		try {
			return createResponse("success", 1, new Gson().toJson(ingBatchDAO.list()));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}

	public String getIngredientBatchesByIngredient(int ingredientID) {
		try {
			return createResponse("success", 1, new Gson().toJson(ingBatchDAO.getIngredientBatchesByIngredient(ingredientID)));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		}
	}
}
