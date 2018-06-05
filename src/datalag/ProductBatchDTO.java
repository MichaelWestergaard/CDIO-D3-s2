package datalag;

public class ProductBatchDTO {
	int productBatchID;
	int status;                     
	int receptID;     
	String startDate;
	String endDate;
	
	public ProductBatchDTO(int productBatchID, int status, int receptID, String startDate, String endDate) {
		this.productBatchID = productBatchID;
		this.status = status;
		this.receptID = receptID;
		this.startDate =startDate;
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
