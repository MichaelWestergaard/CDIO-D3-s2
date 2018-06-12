package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReceptComponentDAO implements BaseDAO<ReceptComponentDTO> {

	@Override
	public boolean create(ReceptComponentDTO receptComponent) throws SQLException, ClassNotFoundException {
		if(read(receptComponent.getReceptID()) == null)  {
			String query = "Call opretRekomponent(?, ?, ?, ?)";
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = connector.getStatement(query);
			preparedStatement.setInt(1, receptComponent.getReceptID());
			preparedStatement.setInt(2, receptComponent.getIngredientID());
			preparedStatement.setDouble(3, receptComponent.getNomNetto());
			preparedStatement.setDouble(4, receptComponent.getTolerance());
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
	public ReceptComponentDTO read(int receptID) throws SQLException, ClassNotFoundException {
		ResultSet results = null;

		String query = "Select * from recept_komponent WHERE recept_id = ? and raavare_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, receptID );
		//preparedStatement.setInt(2, ingredientID);		DETTE SKAL FIXES!! HUSK HUSK HUSK HUSK HUSK
		results = preparedStatement.executeQuery();

		if(results.next()) {
			preparedStatement.close();
			return new ReceptComponentDTO(results.getInt("recept_id"), results.getInt("raavare_id"), results.getString("raavare_navn"), results.getDouble("nom_netto"), results.getDouble("tolerance"));
		}
		preparedStatement.close();
		return null;
	}

	@Override
	public boolean update(ReceptComponentDTO receptComponent) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReceptComponentDTO> list() throws SQLException, ClassNotFoundException {
		List<ReceptComponentDTO> receptComponents = new ArrayList<ReceptComponentDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM recept_komponent";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);

		while(results.next()) {
			receptComponents.add(new ReceptComponentDTO(results.getInt("recept_id"), results.getInt("raavare_id"), results.getString("raavare_navn"), results.getDouble("nom_netto"), results.getDouble("tolerance")));
		}
		preparedStatement.close();
		return receptComponents;
	}

	@Override
	public ReceptComponentDTO delete(int receptID) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
