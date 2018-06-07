package controller;

import java.util.ArrayList;
import java.util.List;
import datalag.ProductBatchComponentDTO;
import datalag.ProductBatchDTO;
import rest.ProductService;
import datalag.ReceptDTO;
import java.util.Scanner;
import datalag.UserDTO;
import rest.UserService;


public class DAO {

	private double load;
	private List<UserDTO> users = new ArrayList<UserDTO>();	
	private List<ReceptDTO> recepts = new ArrayList<ReceptDTO>();
	private List<ProductBatchComponentDTO> batches = new ArrayList<ProductBatchComponentDTO>();
	Scanner scan = new Scanner(System.in);

	
	public boolean checkUserID(int userID) {
		if(userID >= 11 && userID <= 99){
			for(int i = 0; i < users.size(); i++) {
				if(users.get(i).getUserID() == userID) {
					return true;
				}
			}
		}
		return false;
	}

	
	public String getUsername(int userID) {
		for(int i = 0; i < users.size(); i++) {
			if (userID == users.get(i).getUserID() ) {
				return users.get(i).getUserName();
			}
		}
		return null;
	}
	
	
	public String getReceptName(int receptID) {
		for(int i = 0; i < recepts.size(); i++) {
			if (receptID == recepts.get(i).getReceptID()) {
				return recepts.get(i).getReceptName();
			}
		}
		return null;
	}
	
	
	public boolean checkReceptId(int receptID) {
		if(receptID >= 1000 && receptID <= 9999) {
			for(int i = 0; i < recepts.size(); i++) {
				if(recepts.get(i).getReceptID() == receptID) {
					return true;
				}
			}
		}
		return false;
	}

	
	public boolean checkUnloaded(double weight) {
		if(weight == 0.0) {
			return true;
		} else {
			return false;
		}
	}

	
	public void setBatchTara(int productBatchID, double tara) {
		for(int i = 0; i < batches.size(); i++) {
			if (productBatchID == batches.get(i).getProductBatchID()) {
				batches.get(i).setTara(tara);
			}
		}
	}
	
	
	public double getBatchTara(int productBatchID) {
		for(int i = 0; i < batches.size(); i++) {
			if (productBatchID == batches.get(i).getProductBatchID()) {
				return batches.get(i).getTara();
			}
		}
		return 0.0;	//Findes der en bedre l�sning?
	
	}
	
	
	public void setBatchNetto(int productBatchID, double netto) {
		for(int i = 0; i < batches.size(); i++) {
			if (productBatchID == batches.get(i).getProductBatchID()) {
				batches.get(i).setNetto(netto);
			}
		}
	}
	
/*	
	public void setBatchBrutto(int batchID, double negativeBatchBrutto) {
		for(int i = 0; i < batches.size(); i++) {
			if (batchID == batches.get(i).getBatchID()) {
				batches.get(i).setBatchBrutto(Math.abs(negativeBatchBrutto));
			}
		}
	}
	*/
}