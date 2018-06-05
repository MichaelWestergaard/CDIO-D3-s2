package datalag;

import java.sql.Timestamp;

public class ProductBatchDTO {
	int productBatchID;
	int status;                     
	int receptID;     
	Timestamp startdato;
	Timestamp slutdato;
	
	public ProductBatchDTO(int productBatchID, int status, int receptID, Timestamp startdato, Timestamp slutdato) {
		this.productBatchID = productBatchID;
		this.status = status;
		this.receptID = receptID;
		this.startdato = startdato;
		this.slutdato = slutdato;
	}

	public Timestamp getStartdato() {
		return startdato;
	}

	public void setStartdato(Timestamp startdato) {
		this.startdato = startdato;
	}

	public Timestamp getSlutdato() {
		return slutdato;
	}

	public void setSlutdato(Timestamp slutdato) {
		this.slutdato = slutdato;
	}

	public int getProductBatchID() {
		return productBatchID;
	}

	public void setProductBatchID(int productBatchID) {
		this.productBatchID = productBatchID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReceptID() {
		return receptID;
	}

	public void setReceptID(int receptID) {
		this.receptID = receptID;
	}

}
