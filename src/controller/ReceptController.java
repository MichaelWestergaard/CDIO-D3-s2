package controller;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.FormParam;

import com.google.gson.Gson;

import datalag.IngredientDAO;
import datalag.IngredientDTO;
import datalag.ReceptComponentDAO;
import datalag.ReceptComponentDTO;
import datalag.ReceptDAO;
import datalag.ReceptDTO;
import datalag.ResponseHandler;

public class ReceptController extends ResponseHandler {
 
	private ReceptDAO receptDAO = new ReceptDAO();
	private ReceptComponentDAO receptComponentDAO = new ReceptComponentDAO();
	private IngredientDAO ingredientDAO = new IngredientDAO();
	
	public String getRecept(int receptID) {
		try {
			return createResponse("success", 1, new Gson().toJson(receptDAO.read(receptID)));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
	}
	
	public String getReceptList() {
		try {
			return createResponse("success", 1, new Gson().toJson(receptDAO.list()));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
	}
	
	public String createRecept(int receptID, String receptName) {
		try {
			if(receptID >= 1 && receptID <= 99999999) {
				if(receptName.length() >= 2 && receptName.length() <= 20) {
					ReceptDTO receptDTO = new ReceptDTO(receptID, receptName);
					
					if(receptDAO.create(receptDTO)) {
						return createResponse("success", 1, "Recepten \"" + receptDTO.getReceptName() + "\" blev oprettet");
					}
				} else {
					return createResponse("error", 0, "Recept navnet skal være 2-20 tegn!");
				}
			} else {
				return createResponse("error", 0, "Recept ID skal være i mellem 1-99999999!");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke oprette Recepten");
	}
	
	public String createReceptComponent(int receptID, int ingredientID, double nomNetto, double tolerance) {
		if(receptID < 1 || receptID > 99999999) {
			return createResponse("error", 0, "Recept ID skal være mellem 1 - 99999999");
		}
		
		if(ingredientID < 1 || ingredientID > 99999999) {
			return createResponse("error", 0, "Råvare ID skal være mellem 1 - 99999999");
		}
		
		if(nomNetto < 0.05 || nomNetto > 20.0) {
			return createResponse("error", 0, "Nettovæten skal være mellem 0.05 - 20.0");
		}
		
		if(tolerance < 0.1 || tolerance > 10.0) {
			return createResponse("error", 0, "Tolerancen skal være mellem 0.1 - 10.0");
		}
		
		try {
			boolean receptFound = false;
			boolean ingredientFound = false;		
			
			if(receptComponentDAO.read(receptID, ingredientID) != null) {
				return createResponse("error", 0, "receptkomponenten eksisterer allerede");
			}
			
			if(receptDAO.read(receptID) != null) {
				receptFound = true;
			}
			if(ingredientDAO.read(ingredientID) != null) {
				ingredientFound = true;
			}
			if(receptFound && ingredientFound) {
				ReceptComponentDTO receptComponentDTO = new ReceptComponentDTO(receptID, ingredientID, "", nomNetto, tolerance);
				if(receptComponentDAO.create(receptComponentDTO)) {
					return createResponse("success", 1, "Recepkomponenten med recepten \"" + receptDAO.read(receptComponentDTO.getReceptID()).getReceptName() + "\" blev oprettet");
				} else {
					return createResponse("error", 0, "Kunne ikke oprette receptkomponenten");
				}
			} else if (!receptFound && ingredientFound ) {
				return createResponse("error", 0, "receptID eksistere ikke");
			} else if (receptFound && !ingredientFound ) {
				return createResponse("error", 0, "ingredientID eksistere ikke");
			}
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
		return createResponse("error", 0, "Kunne ikke oprette receptkomponenten");
	}
	
	public String getReceptComponentList() {
		try {
			return createResponse("success", 1, new Gson().toJson(receptComponentDAO.list()));
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		}
	}
}
