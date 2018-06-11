package controller;

import java.sql.SQLException;

import com.google.gson.Gson;

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
	
	public String getIngredient(int ingredientID) throws ClassNotFoundException, SQLException {
		return createResponse("success", 1, new Gson().toJson(ingDAO.read(ingredientID)));
	}
	
	public String createIngredient(int ingredientID, String ingredientName) throws ClassNotFoundException, SQLException {
		if(ingredientID >= 1 && ingredientID <= 99999999) {
			if(ingredientName.length() >= 2 && ingredientName.length() <= 20) {
				IngredientDTO ingredient = new IngredientDTO(ingredientID, ingredientName);
				
				if(ingDAO.create(ingredient)) {			
					if(ingDAO.read(ingredientID) != null) {
						return createResponse("success", 1, "Råvaren \"" + ingredientName + "\" blev oprettet");
					}
				}
			} else {
				return createResponse("error", 0, "Råvare navnet skal være 2-20 tegn!");
			}
		} else {
			return createResponse("error", 0, "Råvare ID skal være i mellem 1-99999999!");
		}
		
		return createResponse("error", 0, "Kunne ikke oprette Råvare");
	}

}
