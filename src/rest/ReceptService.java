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
import datalag.ReceptDTO;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.servlet.ServletContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;

@Path("recept")
public class ReceptService {

	private MySQLController mySQLController;
	
	public ReceptService() {
		try {
			mySQLController = new MySQLController();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Receptliste
	@GET
	@Path("getReceptList")
	public String getReceptList() {
		String returnMsg = "";
		
		try {
			List<ReceptDTO> recepter = mySQLController.getRecepter();
			String json = new Gson().toJson(recepter);
			returnMsg = json;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnMsg;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
