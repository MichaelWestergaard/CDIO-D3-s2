package datalag;

public class ReceptDTO {

	int receptID;
	String receptName;
	
	public ReceptDTO(int receptID, String receptName) {
		this.receptID = receptID;
		this.receptName = receptName;
	}

	public int getReceptID() {
		return receptID;
	}

	public void setReceptID(int receptID) {
		this.receptID = receptID;
	}

	public String getReceptName() {
		return receptName;
	}

	public void setReceptName(String receptName) {
		this.receptName = receptName;
	}
	
	
	
}
