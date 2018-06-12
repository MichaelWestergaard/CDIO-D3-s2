package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductBatchComponentDAO implements BaseDAO<ProductBatchComponentDTO> {

	@Override
	public boolean create(ProductBatchComponentDTO pbcDTO) throws SQLException, ClassNotFoundException {
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

	@Override
	public ProductBatchComponentDTO read(int pbID) throws SQLException, ClassNotFoundException {
		ProductBatchComponentDTO productBatchComponent = null;
		ResultSet results = null;

		String query = "Select * from produkt_batch_komponent WHERE pb_id = ? and rb_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, pbID);
		//preparedStatement.setInt(2, raavareBatchID);			DETTE SKAL FIXES!!! HUSK HUSK HUSK HUSK HUSK HUSK
		results = preparedStatement.executeQuery();

		if(results.next()) {
			productBatchComponent = new ProductBatchComponentDTO(results.getInt("pb_id"), results.getInt("rb_id"), results.getInt("raavare_id"), results.getString("rb_raavare_navn"), results.getInt("opr_id"), results.getString("initialer"), results.getDouble("netto"), results.getDouble("tara"));
			preparedStatement.close();
			return productBatchComponent;
		}
		preparedStatement.close();
		return null;
	}

	@Override
	public boolean update(ProductBatchComponentDTO pbcDTO) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ProductBatchComponentDTO> list() throws SQLException, ClassNotFoundException {
		List<ProductBatchComponentDTO> productBatchComponents = new ArrayList<ProductBatchComponentDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM produkt_batch_komponent";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);

		while(results.next()) {
			ProductBatchComponentDTO productBatchComponent = new ProductBatchComponentDTO(results.getInt("pb_id"), results.getInt("rb_id"), results.getInt("raavare_id"), results.getString("rb_raavare_navn"), results.getInt("opr_id"), results.getString("initialer"), results.getDouble("netto"), results.getDouble("tara"));
			productBatchComponents.add(productBatchComponent);
		}
		preparedStatement.close();
		return productBatchComponents;
	}

	@Override
	public ProductBatchComponentDTO delete(int pbcID) {
		// TODO Auto-generated method stub
		return null;
	}

}