package datalag;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

public class IngredientDAO implements BaseDAO<IngredientDTO> {

	
	
	@Override
	public boolean create(IngredientDTO element) throws SQLException  {
			if(getIngredient(ingredientID) == null) {
				IngredientDTO ingredient = new IngredientDTO(ingredientID, ingredientName);

				
				String query = "Call opretRaavare(?, ?)";
				preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
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
	public IngredientDTO read(String id)  {
		// TODO Auto-generated method stub
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
