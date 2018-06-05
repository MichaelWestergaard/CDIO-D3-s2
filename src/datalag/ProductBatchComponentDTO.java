package datalag;

public class ProductBatchComponentDTO {
	int productBatchID;
	int ingredientBatchID;
	String ingredientName;
	int operatorID;
	String initials;
	double netto;                    
	double tara;
	
	public ProductBatchComponentDTO(int productBatchID, int ingredientBatchID, String ingredientName, int operatorID, String initials, double netto, double tara) {
		this.productBatchID = productBatchID;
		this.ingredientBatchID = ingredientBatchID;
		this.ingredientName = ingredientName;
		this.operatorID = operatorID;
		this.initials = initials;
		this.netto = netto;
		this.tara = tara;
	}

	public int getProductBatchID() {
		return productBatchID;
	}

	public int getIngredientBatchID() {
		return ingredientBatchID;
	}

	public String getIngredientName() {
		return ingredientName;
	}

	public int getOperatorID() {
		return operatorID;
	}

	public String getInitials() {
		return initials;
	}

	public double getNetto() {
		return netto;
	}

	public double getTara() {
		return tara;
	}
	
}
