package datalag;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import datalag.IngBatchDTO;

public class IngBatchDAO implements BaseDAO<IngBatchDTO> {

	@Override
	public boolean create(int ingBatchID, String[] parameters) throws SQLException {
		
		
		if(read(ingBatchID) == null) {
			IngBatchDTO ingBatch = new IngBatchDTO(ingBatchID, Integer.parseInt(parameters[0]), Double.parseDouble (parameters[1]), "", parameters[2]);

			String query = "Call opretRaavarebatch(?, ?, ?, ?)";
			PreparedStatement preparedStatement = null;
			
			
			try {
				preparedStatement = MySQLConnector.getInstance().getStatement(query);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			preparedStatement.setInt(1, ingBatch.getIngBatchID());
			preparedStatement.setInt(2, ingBatch.getIngredientID());
			preparedStatement.setDouble(3, ingBatch.getAmount());
			preparedStatement.setString(4, ingBatch.getSupplier());
			preparedStatement.execute();
			preparedStatement.close();
			return true;
		} else {
			return false;	
		}
	}

	@Override
	public IngBatchDTO read(int ingBatchID) {
		IngBatchDTO ingBatch = null;
		ResultSet results = null;

		String query = "Select * from raavare_batch WHERE rb_id = ?";
		PreparedStatement preparedStatement = null;
		try {
		preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, ingBatchID);
		results = preparedStatement.executeQuery();

		if(results.next()) {
			ingBatch = new IngBatchDTO(results.getInt("rb_id"), results.getInt("raavare_id"), results.getDouble("maengde"), results.getString("raavare_navn"), results.getString("leverandoer"));
			preparedStatement.close();
			return ingBatch;
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
	public IngBatchDTO delete(int ID) {
		// TODO Auto-generated method stub
		return null;
	}

}
