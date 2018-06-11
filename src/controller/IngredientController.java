package controller;

import datalag.IngredientDTO;
import datalag.MySQLController;

public class IngredientController {
	IngredientDAO ingr = MySQLController.getIngredientDAO();
	
	IngredientDTO saveIngredient(IngredientDTO dto) {
		//Do some validation??
		ValidationCOntroller??
		ingr.save(dto);
	}

}
