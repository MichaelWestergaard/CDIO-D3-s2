package DAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import DTO.IngBatchDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import datalag.MySQLConnector;

public class IngBatchDAO implements BaseDAO<IngBatchDTO> {

	@Override
	public boolean create(IngBatchDTO ingBatch) throws SQLException, ClassNotFoundException {		
		MySQLConnector connector = MySQLConnector.getInstance();
		
		if(read(ingBatch.getIngBatchID()) == null) {
			String query = "Call opretRaavarebatch(?, ?, ?, ?)";
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
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "Select * from raavare_batch WHERE rb_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, ingBatchID);
		ResultSet results = connector.doQuery(preparedStatement);

		if(results.next()) {
			IngBatchDTO ingBatch = new IngBatchDTO(results.getInt("rb_id"), results.getInt("raavare_id"), results.getDouble("maengde"), results.getString("raavare_navn"), results.getString("leverandoer"));
			preparedStatement.close();
			return ingBatch;
		}
		preparedStatement.close();
		return null;		
	}

	@Override
	public List<IngBatchDTO> list() throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		List<IngBatchDTO> ingBatches = new ArrayList<IngBatchDTO>();

		String query = "SELECT * FROM raavare_batch";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		ResultSet results = connector.doQuery(preparedStatement);

		while(results.next()) {
			IngBatchDTO ingBatch = new IngBatchDTO(results.getInt("rb_id"), results.getInt("raavare_id"), results.getDouble("maengde"), results.getString("raavare_navn"), results.getString("leverandoer"));
			ingBatches.add(ingBatch);
		}
		preparedStatement.close();
		return ingBatches;
	}
		
	public List<Integer> list(int ingredientID) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		List<Integer> productBatches = new ArrayList<Integer>();

		String query = "SELECT rb_id FROM raavare_batch WHERE raavare_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, ingredientID);
		ResultSet results = connector.doQuery(preparedStatement);

		while(results.next()) {
			productBatches.add(results.getInt("rb_id"));
		}
	
		preparedStatement.close();
		return productBatches;
	}
	
	public List<Integer> getIngredientBatchesByIngredient(int ingredientID) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		List<Integer> ingBatches = new ArrayList<Integer>();

		String query = "SELECT rb_id FROM raavarebatch WHERE raavare_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, ingredientID);
		ResultSet results = connector.doQuery(preparedStatement);

		while(results.next()) {
			ingBatches.add(results.getInt("rb_id"));
		}

		preparedStatement.close();
		return ingBatches;
	}

	public boolean updateAmount(int ingBatchID, double weighedAmount) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		if(read(ingBatchID) == null) {
			return false;
		}
		if(read(ingBatchID).getAmount() < weighedAmount) {
			return false;
		}

		String query = "call opdaterMaengde(?, ?)";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, ingBatchID);
		preparedStatement.setDouble(2, weighedAmount);
		preparedStatement.execute();
		preparedStatement.close();
		return true;
	}
	
	@Override
	public boolean update(IngBatchDTO ingBatch) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}

	@Override
	public IngBatchDTO delete(int ID) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}
}
