package datalag;

public class ProductBatchComponentDTO {
	int productBatchID;
	int raavareBatchID;
	int operatorID;
	double netto;                    
	double tara;
	
	public ProductBatchComponentDTO(int productBatchID, int raavareBatchID, int operatorID, double netto, double tara) {
		this.productBatchID = productBatchID;
		this.raavareBatchID = raavareBatchID;
		this.operatorID = operatorID;
		this.netto = netto;
		this.tara = tara;
	
	}

	public int getProductBatchID() {
		return productBatchID;
	}

	public void setProductBatchID(int productBatchID) {
		this.productBatchID = productBatchID;
	}

	public int getRaavareBatchID() {
		return raavareBatchID;
	}

	public void setRaavareBatchID(int raavareBatchID) {
		this.raavareBatchID = raavareBatchID;
	}
	
	public int getOperatorID() {
		return operatorID;
	}
	
	public void setOperatorID(int operatorID) {
		this.operatorID = operatorID;
	}

	public double getNetto() {
		return netto;
	}

	public void setNetto(double netto) {
		this.netto = netto;
	}

	public double getTara() {
		return tara;
	}

	public void setTara(double tara) {
		this.tara = tara;
	}	
}
