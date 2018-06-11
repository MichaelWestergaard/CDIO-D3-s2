package datalag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

public class receptComponentDAO implements BaseDAO<ReceptComponentDTO> {

	@Override
	public boolean create(int receptID, int ingredientID, double nomNetto, double tolerance) throws SQLException, ClassNotFoundException {
		if(read(receptID, ingredientID) == null)  {

			String query = "Call opretRekomponent(?, ?, ?, ?)";
			PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
			preparedStatement.setInt(1, receptID);
			preparedStatement.setInt(2, ingredientID);
			preparedStatement.setDouble(3, nomNetto);
			preparedStatement.setDouble(4, tolerance);
			preparedStatement.execute();
			preparedStatement.close();
			return true;
		} else {
			return false;			
		}
	}

	@Override
	public ReceptComponentDTO read(int ID) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(ReceptComponentDTO object) throws SQLException, ClassNotFoundException {
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
			ReceptComponentDTO receptComponent = new ReceptComponentDTO(results.getInt("recept_id"), results.getInt("raavare_id"), results.getString("raavare_navn"), results.getDouble("nom_netto"), results.getDouble("tolerance"));
			receptComponents.add(receptComponent);
		}
		preparedStatement.close();
		return receptComponents;
	}
	
	@Override
	public List<ReceptComponentDTO> list(int receptID) throws SQLException, ClassNotFoundException {
		List<ReceptComponentDTO> receptComponents = new ArrayList<ReceptComponentDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM recept_komponent where recept_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);
		preparedStatement.setInt(1, receptID);
		results = preparedStatement.executeQuery();

		while(results.next()) {
			ReceptComponentDTO receptComponent = new ReceptComponentDTO(results.getInt("recept_id"), results.getInt("raavare_id"), results.getString("raavare_navn"), results.getDouble("nom_netto"), results.getDouble("tolerance"));
			receptComponents.add(receptComponent);
		}
		preparedStatement.close();
		return receptComponents;
	}
	
	@Override
	public List<ReceptComponentDTO> list(int receptID, int ingredientID) throws SQLException, ClassNotFoundException {
		ReceptComponentDTO receptComponent = null;
		ResultSet results = null;

		String query = "Select * from recept_komponent WHERE recept_id = ? and raavare_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);
		preparedStatement.setInt(1, receptID );
		preparedStatement.setInt(2, ingredientID);
		results = preparedStatement.executeQuery();

		if(results.next()) {
			receptComponent = new ReceptComponentDTO(results.getInt("recept_id"), results.getInt("raavare_id"), results.getString("raavare_navn"), results.getDouble("nom_netto"), results.getDouble("tolerance"));
			preparedStatement.close();
			return (List<ReceptComponentDTO>) receptComponent;
		}
		preparedStatement.close();
		return null;
	}
	
	@Override
	public ReceptComponentDTO delete(int ID) {
		// TODO Auto-generated method stub
		return null;
	}

}
