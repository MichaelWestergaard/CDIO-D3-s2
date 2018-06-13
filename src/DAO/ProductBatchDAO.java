package DAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import DTO.ProductBatchDTO;
import datalag.MySQLConnector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductBatchDAO implements BaseDAO<ProductBatchDTO> {

	@Override
	public boolean create(ProductBatchDTO pb) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		if(read(pb.getProductBatchID()) == null) {
			String query = "Call opretProduktBatch(?, ?, ?)";
			PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
			preparedStatement.setInt(1, pb.getProductBatchID());
			preparedStatement.setInt(2, pb.getStatus());
			preparedStatement.setInt(3, pb.getReceptID());
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
	public ProductBatchDTO read(int pbID) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		ProductBatchDTO productBatch = null;
		ResultSet results = null;

		String query = "Select * from produktbatch WHERE pb_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, pbID);
		results = connector.doQuery(preparedStatement);

		if(results.next()) {
			String endDate = "";
			String startDate = "";

			if(results.getTimestamp("slutdato") != null) {
				endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(results.getTimestamp("slutdato"));
			}

			if(results.getTimestamp("startdato") != null) {
				startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(results.getTimestamp("startdato"));
			}

			productBatch = new ProductBatchDTO(results.getInt("pb_id"), results.getInt("status"), results.getInt("recept_id"), startDate, endDate);
			preparedStatement.close();
			return productBatch;
		}
		preparedStatement.close();
		return null;
	}

	@Override
	public List<ProductBatchDTO> list() throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		List<ProductBatchDTO> productBatches = new ArrayList<ProductBatchDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM produktbatch";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		results = connector.doQuery(preparedStatement);

		while(results.next()) {
			String endDate = "";
			String startDate = "";

			if(results.getTimestamp("slutdato") != null) {
				endDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(results.getTimestamp("slutdato"));
			}

			if(results.getTimestamp("startdato") != null) {
				startDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(results.getTimestamp("startdato"));
			}

			ProductBatchDTO productBatch = new ProductBatchDTO(results.getInt("pb_id"), results.getInt("status"), results.getInt("recept_id"), startDate, endDate);
			productBatches.add(productBatch);
		}
		preparedStatement.close();
		return productBatches;
	}

	public boolean finishProductBatch(int pbID) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "call afslutProduktion(?)";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, pbID);
		if(connector.execute(preparedStatement)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean update(ProductBatchDTO pb) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}
	
	@Override
	public ProductBatchDTO delete(int pbID) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}
	
}
