package datalag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;


public class IngredientDAO implements BaseDAO<IngredientDTO> {
	
	@Override
	public boolean create(IngredientDTO ingredient) throws SQLException, ClassNotFoundException  {		
		if(read(ingredient.getIngredientID()) == null) {								
			String query = "Call opretRaavare(?, ?)";
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = connector.getStatement(query);
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
		IngredientDTO ingredient = null;
		ResultSet results = null;

		String query = "Select * from raavare WHERE raavare_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, ingredientID);
		results = preparedStatement.executeQuery();
		
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
		if(read(ingredient.getIngredientID()) != null) {
			String query = "call redigerRaavare(?, ?)";
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = connector.getStatement(query);
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
		List<IngredientDTO> ingredients = new ArrayList<IngredientDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM raavare";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);

		while(results.next()) {
			IngredientDTO ingredient = new IngredientDTO(results.getInt("raavare_id"), results.getString("raavare_navn"));
			ingredients.add(ingredient);
		}
		preparedStatement.close();
		return ingredients;
	}
	
	@Override
	public IngredientDTO delete(int ingredientID) throws ClassNotFoundException {
		throw new ClassNotFoundException("Denne metode er ikke lavet");
	}
}
