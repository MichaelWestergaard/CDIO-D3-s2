package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductBatchComponentDAO implements BaseDAO<ProductBatchComponentDTO> {

	@Override
	public boolean create(ProductBatchComponentDTO pbcDTO) throws SQLException, ClassNotFoundException, NotImplementedException {
		if(read(pbcDTO.getProductBatchID()) == null)  {

			String query = "Call afvejning(?, ?, ?, ?, ?)"; //Ved ikke, om det er det rigtige sql call ??? 
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = connector.getStatement(query);
			preparedStatement.setInt(1, pbcDTO.getProductBatchID());
			preparedStatement.setInt(2, pbcDTO.getIngredientBatchID());
			preparedStatement.setDouble(3, pbcDTO.getTara());
			preparedStatement.setDouble(4, pbcDTO.getNetto());
			preparedStatement.setInt(5, pbcDTO.getOperatorID());
			if(connector.execute(preparedStatement)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;			
		}
	}

	public ProductBatchComponentDTO read(int pbID, int raavareBatchID) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "Select * from produkt_batch_komponent WHERE pb_id = ? and rb_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, pbID);
		preparedStatement.setInt(2, raavareBatchID);
		ResultSet results = connector.doQuery(preparedStatement);

		if(results.next()) {
			ProductBatchComponentDTO productBatchComponent = new ProductBatchComponentDTO(results.getInt("pb_id"), results.getInt("rb_id"), results.getInt("raavare_id"), results.getString("rb_raavare_navn"), results.getInt("opr_id"), results.getString("initialer"), results.getDouble("netto"), results.getDouble("tara"));
			preparedStatement.close();
			return productBatchComponent;
		}
		preparedStatement.close();
		return null;
	}

	@Override
	public boolean update(ProductBatchComponentDTO pbcDTO) throws ClassNotFoundException {
		throw new ClassNotFoundException("Denne metode er ikke lavet");
	}

	@Override
	public List<ProductBatchComponentDTO> list() throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		List<ProductBatchComponentDTO> productBatchComponents = new ArrayList<ProductBatchComponentDTO>();

		String query = "SELECT * FROM produkt_batch_komponent";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		ResultSet results = connector.doQuery(preparedStatement);

		while(results.next()) {
			ProductBatchComponentDTO productBatchComponent = new ProductBatchComponentDTO(results.getInt("pb_id"), results.getInt("rb_id"), results.getInt("raavare_id"), results.getString("rb_raavare_navn"), results.getInt("opr_id"), results.getString("initialer"), results.getDouble("netto"), results.getDouble("tara"));
			productBatchComponents.add(productBatchComponent);
		}
		preparedStatement.close();
		return productBatchComponents;
	}

	@Override
	public ProductBatchComponentDTO delete(int pbcID) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}

	@Override
	public ProductBatchComponentDTO read(int ID) throws NotImplementedException {
		throw new NotImplementedException("ProductBatchComponentDTO.read(int) er ikke lavet! Brug read(int pbID, int raavareBatchID)");
	}

}
