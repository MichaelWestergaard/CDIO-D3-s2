package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import DTO.IngredientDTO;
import datalag.MySQLConnector;


public class IngredientDAO implements BaseDAO<IngredientDTO> {
	
	@Override
	public boolean create(IngredientDTO ingredient) throws SQLException, ClassNotFoundException  {	
		MySQLConnector connector = MySQLConnector.getInstance();	
		if(read(ingredient.getIngredientID()) == null) {								
			String query = "Call opretRaavare(?, ?)";
			PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
			preparedStatement.setInt(1, ingredient.getIngredientID());
			preparedStatement.setString(2, ingredient.getIngredientName());
			if(connector.execute(preparedStatement)) {
				return true;
			} else {
				return false;
			}			
		} else {
			return false;
		}
	}

	@Override
	public IngredientDTO read(int ingredientID) throws ClassNotFoundException, SQLException  {
		MySQLConnector connector = MySQLConnector.getInstance();
		IngredientDTO ingredient = null;

		String query = "Select * from raavare WHERE raavare_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, ingredientID);
		ResultSet results = connector.doQuery(preparedStatement);
		
		if(results.next()) {
			ingredient = new IngredientDTO(results.getInt("raavare_id"), results.getString("raavare_navn"));
			preparedStatement.close();
			return ingredient;
		}
		preparedStatement.close();
		return null;		
	}


	@Override
	public boolean update(IngredientDTO ingredient) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		if(read(ingredient.getIngredientID()) != null) {
			String query = "call redigerRaavare(?, ?)";
			PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
			preparedStatement.setInt(1, ingredient.getIngredientID());
			preparedStatement.setString(2, ingredient.getIngredientName());
			if(connector.execute(preparedStatement)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public List<IngredientDTO> list() throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		List<IngredientDTO> ingredients = new ArrayList<IngredientDTO>();

		String query = "SELECT * FROM raavare";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		ResultSet results = connector.doQuery(preparedStatement);

		while(results.next()) {
			IngredientDTO ingredient = new IngredientDTO(results.getInt("raavare_id"), results.getString("raavare_navn"));
			ingredients.add(ingredient);
		}
		preparedStatement.close();
		return ingredients;
	}
	
	@Override
	public IngredientDTO delete(int ingredientID) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}
}
