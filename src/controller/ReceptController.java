package controller;

import java.sql.SQLException;
import com.google.gson.Gson;

import datalag.ReceptComponentDAO;
import datalag.ReceptComponentDTO;
import datalag.ReceptDAO;
import datalag.ReceptDTO;
import datalag.ResponseHandler;

public class ReceptController extends ResponseHandler {
 
	private ReceptDAO receptDAO = new ReceptDAO();
	private ReceptComponentDAO rcDAO = new ReceptComponentDAO();
	private IngredientController ingController = new IngredientController();
	
	public String getRecept(int receptID) {
		try {
			return createResponse("success", 1, new Gson().toJson(receptDAO.read(receptID)));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} 
	}
	
	public String getReceptList() {
		try {
			return createResponse("success", 1, new Gson().toJson(receptDAO.list()));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} 
	}
	
	public String createRecept(int receptID, String receptName) {
		//Validering af data
		if(receptID >= 1 && receptID <= 99999999) {
			if(receptName.length() >= 2 && receptName.length() <= 20) {
				ReceptDTO recept = new ReceptDTO(receptID, receptName);
				
				try {
					if(receptDAO.create(recept)) {			
						if(receptDAO.read(receptID) != null) {
							return createResponse("success", 1, "Recepten \"" + receptDAO.read(receptID).getReceptName() + "\" blev oprettet");
						}
					}
				} catch (ClassNotFoundException e) {
					return createResponse("error", 0, e.getMessage());
				} catch (SQLException e) {
					return createResponse("error", e.getErrorCode(), e.getMessage());
				} 
			} else {
				return createResponse("error", 0, "Recept navnet skal være 2-20 tegn!");
			}
		} else {
			return createResponse("error", 0, "Recept ID skal være i mellem 1-99999999!");
		}
		return createResponse("error", 0, "Kunne ikke oprette Recepten");
	}

	public String getReceptComponentList() {
		try {
			return createResponse("success", 1, new Gson().toJson(rcDAO.list()));
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} 
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
			
			if(rcDAO.read(receptID) != null) {
				return createResponse("error", 0, "receptkomponenten eksisterer allerede");
			}
			
			if(receptDAO.read(receptID) != null) {
				receptFound = true;
			}
			
			if(ingController.getIngredient(ingredientID) != null) {
				ingredientFound = true;
			}
			
			if(receptFound && ingredientFound) {					
			rcDAO.create(new ReceptComponentDTO(receptID, ingredientID, null, nomNetto, tolerance));
	
			} else if (!receptFound && ingredientFound ) {
				return createResponse("error", 0, "receptID eksistere ikke");
			} else if (receptFound && !ingredientFound ) {
				return createResponse("error", 0, "ingredientID eksistere ikke");
			}
			
			if(rcDAO.read(receptID) != null) {
				return createResponse("success", 1, "Recepkomponenten med recepten \"" + receptDAO.read(receptID).getReceptName() + "\" blev oprettet");

			} else {
				return createResponse("error", 0, "Kunne ikke oprette receptkomponenten");
			}
		} catch (ClassNotFoundException e) {
			return createResponse("error", 0, e.getMessage());
		} catch (SQLException e) {
			return createResponse("error", e.getErrorCode(), e.getMessage());
		} 
	}
}
