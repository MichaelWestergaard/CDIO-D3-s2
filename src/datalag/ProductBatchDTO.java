package datalag;

import java.sql.Timestamp;

public class ProductBatchDTO {
	int productBatchID;
	int status;                     
	int receptID;     
	String startdato;
	String slutdato;
	
	public ProductBatchDTO(int productBatchID, int status, int receptID, String startdato, String slutdato) {
		this.productBatchID = productBatchID;
		this.status = status;
		this.receptID = receptID;
		this.startdato = startdato;
		this.slutdato = slutdato;
	}

	public String getStartdato() {
		return startdato;
	}

	public void setStartdato(String startdato) {
		this.startdato = startdato;
	}

	public String getSlutdato() {
		return slutdato;
	}

	public void setSlutdato(String slutdato) {
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
