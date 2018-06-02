package datalag;

public class IngBatchDTO {

	int ingBatchID;
	int ingredientID;
	double amount;
	
	public IngBatchDTO(int ingBatchID, int ingredientID, double amount) {
		this.ingBatchID = ingBatchID;
		this.ingredientID = ingredientID;
		this.amount = amount;
	}

	public int getIngBatchID() {
		return ingBatchID;
	}

	public void setIngBatchID(int ingBatchID) {
		this.ingBatchID = ingBatchID;
	}

	public int getIngredientID() {
		return ingredientID;
	}

	public void setIngredientID(int ingredientID) {
		this.ingredientID = ingredientID;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
	
}
