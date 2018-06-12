package datalag;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductBatchDAO implements BaseDAO<ProductBatchDTO> {

	@Override
	public boolean create(ProductBatchDTO pb) throws SQLException, ClassNotFoundException {
		if(read(pb.getProductBatchID()) == null) {
			String query = "Call opretProduktBatch(?, ?, ?)";
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = connector.getStatement(query);
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
		ProductBatchDTO productBatch = null;
		ResultSet results = null;

		String query = "Select * from produktbatch WHERE pb_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, pbID);
		results = preparedStatement.executeQuery();

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
		List<ProductBatchDTO> productBatches = new ArrayList<ProductBatchDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM produktbatch";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);

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
		String query = "call afslutProduktion(?)";
		MySQLConnector connector = MySQLConnector.getInstance();
		PreparedStatement preparedStatement = connector.getStatement(query);
		preparedStatement.setInt(1, pbID);
		preparedStatement.execute();
		preparedStatement.close();
		return true;
	}
	
	@Override
	public boolean update(ProductBatchDTO pb) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public ProductBatchDTO delete(int pbID) {
		// TODO Auto-generated method stub
		return null;
	}
	
}