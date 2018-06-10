package datalag;

public class IngBatchDTO {

	int ingBatchID;
	int ingredientID;
	double amount;
	String ingredientName;
	String supplier;
	
	public IngBatchDTO(int ingBatchID, int ingredientID, double amount, String ingredientName, String supplier) {
		this.ingBatchID = ingBatchID;
		this.ingredientID = ingredientID;
		this.amount = amount;
		this.ingredientName = ingredientName;
		this.supplier = supplier;
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
		
	public String getSupplier() {
		return supplier;
	}
}
