package datalag;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import datalag.IngBatchDTO;

public class IngBatchDAO implements BaseDAO<IngBatchDTO> {

	@Override
	public boolean create(IngBatchDTO ingBatch) throws SQLException, ClassNotFoundException {		
		if(read(ingBatch.getIngBatchID()) == null) {
			String query = "Call opretRaavarebatch(?, ?, ?, ?)";
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = connector.getStatement(query);
			preparedStatement.setInt(1, ingBatch.getIngBatchID());
			preparedStatement.setInt(2, ingBatch.getIngredientID());
			preparedStatement.setDouble(3, ingBatch.getAmount());
			preparedStatement.setString(4, ingBatch.getSupplier());
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
	public IngBatchDTO read(int ingBatchID) throws ClassNotFoundException, SQLException {
		IngBatchDTO ingBatch = null;
		ResultSet results = null;

		String query = "Select * from raavare_batch WHERE rb_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, ingBatchID);
		results = preparedStatement.executeQuery();

		if(results.next()) {
			ingBatch = new IngBatchDTO(results.getInt("rb_id"), results.getInt("raavare_id"), results.getDouble("maengde"), results.getString("raavare_navn"), results.getString("leverandoer"));
			preparedStatement.close();
			return ingBatch;
		}
		preparedStatement.close();
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
