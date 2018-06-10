package datalag;

public class IngBatchDTO {

	int ingBatchID;
	int ingredientID;
	double amount;
	String ingredientName;
	String leverandoer;
	
	public IngBatchDTO(int ingBatchID, int ingredientID, double amount, String ingredientName, String leverandoer) {
		this.ingBatchID = ingBatchID;
		this.ingredientID = ingredientID;
		this.amount = amount;
		this.ingredientName = ingredientName;
		this.leverandoer = leverandoer;
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
		
	public String getLeverandoer() {
		return leverandoer;
	}
}
