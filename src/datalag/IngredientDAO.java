package datalag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datalag.IngredientDTO;

public class IngredientDAO implements BaseDAO<IngredientDTO> {

	
	
	@Override
	public boolean create(IngredientDTO element) throws SQLException  {
			
		
		if(IngredientDAO.read(getIngredientID(element.ingredientID)) == null) {
				IngredientDTO ingredient = new IngredientDTO(ingredientID, ingredientName);
								
				String query = "Call opretRaavare(?, ?)";
				PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
//				preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
				preparedStatement.setInt(1, ingredient.getIngredientID());
				preparedStatement.setString(2, ingredient.getIngredientName());
				preparedStatement.execute();
				preparedStatement.close();
				return true;
			} else {
				return false;
			}
		}

	@Override
	public IngredientDTO read(int id)  {
		IngredientDTO ingredient = null;
		ResultSet results = null;

		String query = "Select * from raavare WHERE raavare_id = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
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
	public IngredientDTO update(IngredientDTO element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IngredientDTO delele(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
