package datalag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.mysql.jdbc.Statement;

public class receptComponentDAO implements BaseDAO<ReceptComponentDTO> {

	@Override
	public boolean create(ReceptComponentDTO receptComponent) throws SQLException, ClassNotFoundException {
		if(read(receptComponent.getReceptID()) == null) {

			String query = "Call opretRekomponent(?, ?, ?, ?)";
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
			preparedStatement.setInt(1, receptComponent.getReceptID());
			preparedStatement.setInt(2, receptComponent.getIngredientID());
			preparedStatement.setDouble(3, receptComponent.getNomNetto());
			preparedStatement.setDouble(4, receptComponent.getTolerance());
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
	public ReceptComponentDTO delete(int ID) {
		// TODO Auto-generated method stub
		return null;
	}

}
