package datalag;

public class IngBatchDTO {

	int ingBatchID;
	int ingredientID;
	double amount;
	String ingredientName;
	
	public IngBatchDTO(int ingBatchID, int ingredientID, double amount, String ingredientName) {
		this.ingBatchID = ingBatchID;
		this.ingredientID = ingredientID;
		this.amount = amount;
		this.ingredientName = ingredientName;
	}

	public int getIngBatchID() {
		return ingBatchID;
	}

	public int getIngredientID() {
		return ingredientID;
	}

	public double getAmount() {
		return amount;
	}

	public String getIngredientName() {
		return ingredientName;
	}
		
}
