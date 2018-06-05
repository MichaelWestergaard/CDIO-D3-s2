package datalag;

import java.sql.Timestamp;

public class ProductBatchDTO {
	int productBatchID;
	int status;                     
	int receptID;     
	Timestamp startTime;
	Timestamp endTime;
	
	public ProductBatchDTO(int productBatchID, int status, int receptID, Timestamp startTime, Timestamp endTime) {
		this.productBatchID = productBatchID;
		this.status = status;
		this.receptID = receptID;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Timestamp getStartDate() {
		return startTime;
	}

	public void setStartDate(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndDate() {
		return endTime;
	}

	public void setEndDate(Timestamp endTime) {
		this.endTime = endTime;
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
