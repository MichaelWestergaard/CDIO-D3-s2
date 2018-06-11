package datalag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datalag.IngredientDTO;

public class IngredientDAO implements BaseDAO<IngredientDTO> {

	
	
	@Override
	public boolean create(int ingredientID, String[] parameters) throws SQLException  {
			
		
		if(read(ingredientID) == null) {
				IngredientDTO ingredient = new IngredientDTO(ingredientID, parameters[0]);
								
				String query = "Call opretRaavare(?, ?)";
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = MySQLConnector.getInstance().getStatement(query);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	public IngredientDTO read(int ingredientID)  {
		IngredientDTO ingredient = null;
		ResultSet results = null;

		String query = "Select * from raavare WHERE raavare_id = ?";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = MySQLConnector.getInstance().getStatement(query);
			preparedStatement.setInt(1, ingredientID);
			results = preparedStatement.executeQuery();
			
			if(results.next()) {
				ingredient = new IngredientDTO(results.getInt("raavare_id"), results.getString("raavare_navn"));
				preparedStatement.close();
				return ingredient;
			}
			preparedStatement.close();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}


	@Override
	public boolean update(int ID, String[] parameters) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IngredientDTO delete(int ID) {
		// TODO Auto-generated method stub
		return null;
	}

}
