package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

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
	public List<IngBatchDTO> list() throws SQLException, ClassNotFoundException {
		List<IngBatchDTO> ingBatches = new ArrayList<IngBatchDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM raavare_batch";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);

		while(results.next()) {
			IngBatchDTO ingBatch = new IngBatchDTO(results.getInt("rb_id"), results.getInt("raavare_id"), results.getDouble("maengde"), results.getString("raavare_navn"), results.getString("leverandoer"));
			ingBatches.add(ingBatch);
		}
		preparedStatement.close();
		return ingBatches;
	}
	
	public List<Integer> getIngredientBatchesByIngredient(int ingredientID) throws SQLException, ClassNotFoundException {
		List<Integer> ingBatches = new ArrayList<Integer>();
		ResultSet results = null;

		String query = "SELECT rb_id FROM raavarebatch WHERE raavare_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, ingredientID);
		results = preparedStatement.executeQuery();

		while(results.next()) {
			ingBatches.add(results.getInt("rb_id"));
		}

		preparedStatement.close();
		return ingBatches;
	}

	public boolean updateAmount(int ingBatchID, double weighedAmount) throws SQLException, ClassNotFoundException {
		if(read(ingBatchID) == null) {
			return false;
		}
		if(read(ingBatchID).getAmount() < weighedAmount) {
			return false;
		}

		String query = "call opdaterMaengde(?, ?)";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, ingBatchID);
		preparedStatement.setDouble(2, weighedAmount);
		preparedStatement.execute();
		preparedStatement.close();
		return true;
	}
	
	@Override
	public boolean update(IngBatchDTO ingBatch) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IngBatchDTO delete(int ID) {
		// TODO Auto-generated method stub
		return null;
	}
}
