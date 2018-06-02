package datalag;

public class ProductBatchDTO {
	int productBatchID;
	int status;                     
	int receptID;                    
	
	public ProductBatchDTO(int productBatchID, int status, int receptID) {
		this.productBatchID = productBatchID;
		this.status = status;
		this.receptID = receptID;
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
