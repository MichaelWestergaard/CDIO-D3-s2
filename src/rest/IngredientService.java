package rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;

import datalag.MySQLController;
import datalag.IngredientDTO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;

@Path("ingredient")
public class IngredientService {

	private MySQLController mySQLController;
	
	public IngredientService() {
		try {
			mySQLController = new MySQLController();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Råvareliste
	@GET
	@Path("getIngredientList")
	public String getIngredientList() {
		String returnMsg = "";
		
		try {
			List<IngredientDTO> ingredients = mySQLController.getIngredients();
			String json = new Gson().toJson(ingredients);
			returnMsg = json;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnMsg;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

