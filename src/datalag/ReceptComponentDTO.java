package datalag;

public class ReceptComponentDTO {

	int receptID;
	int ingredientID;  
	String ingredientName;
	double nomNetto;                    
	double tolerance;
	
	public ReceptComponentDTO(int receptID, int ingredientID, String ingredientName, double nomNetto, double tolerance) {
		this.receptID = receptID;
		this.ingredientID = ingredientID;
		this.ingredientName = ingredientName;
		this.nomNetto = nomNetto;
		this.tolerance = tolerance;
	}

	public int getReceptID() {
		return receptID;
	}

	public void setReceptID(int receptID) {
		this.receptID = receptID;
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

	public double getNomNetto() {
		return nomNetto;
	}

	public void setNomNetto(double nomNetto) {
		this.nomNetto = nomNetto;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
		
}
