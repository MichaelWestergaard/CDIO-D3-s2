package DTO;

import java.util.List;

public class IngredientDTO {

	int ingredientID;                     
	String ingredientName;                    
	
	
	public IngredientDTO(int ingredientID, String ingredientName) {
		this.ingredientID = ingredientID;
		this.ingredientName = ingredientName;
		
	
	}

	public int getIngredientID() {
		return ingredientID;
	}

	public void setIngredientID(int ingredientID) {
		this.ingredientID = ingredientID;
	}

	public String getIngredientName() {
		return ingredientName;
	}

	public void setIngredientName(String ingredientName) {
		this.ingredientName = ingredientName;
	}

	
	@Override
	public String toString() {
		return "[ingredientID=" + ingredientID + ", ingredientName=" + ingredientName + "]";
	}
}
