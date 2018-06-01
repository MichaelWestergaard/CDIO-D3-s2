package datalag;

public class ReceptComponentDTO {

	int receptID;
	int ingredientID;                     
	double nomNetto;                    
	double tolerance;
	
	public ReceptComponentDTO(int receptID, int ingredientID, double nomNetto, double tolerance) {
		this.receptID = receptID;
		this.ingredientID = ingredientID;
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
