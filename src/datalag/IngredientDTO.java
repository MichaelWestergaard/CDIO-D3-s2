package datalag;

import java.util.List;

public class IngredientDTO {

	int ingredientID;                     
	String ingredientName;                    
	String supplier;
	
	public IngredientDTO(int ingredientID, String ingredientName, String supplier) {
		this.ingredientID = ingredientID;
		this.ingredientName = ingredientName;
		this.supplier = supplier;
	
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

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	
	@Override
	public String toString() {
		return "[ingredientID=" + ingredientID + ", ingredientName=" + ingredientName + ", supplier=" + supplier + "]";
	}
}
