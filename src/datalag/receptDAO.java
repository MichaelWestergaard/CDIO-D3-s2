package datalag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import datalag.ReceptDTO;


public class receptDAO implements BaseDAO<ReceptDTO> {

	@Override
	public boolean create(int ID, String[] parameters) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ReceptDTO read(int receptID) {
		ReceptDTO recept = null;
		ResultSet results = null;

		String query = "Select * from recept WHERE recept_id = ?";
		PreparedStatement preparedStatement = null;
		try {
		preparedStatement = MySQLConnector.getInstance().getStatement(query);
	
		
		//preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, receptID);
		results = preparedStatement.executeQuery();

		if(results.next()) {
			recept = new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn"));
			preparedStatement.close();
			return recept;
		}
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ReceptDTO update(ReceptDTO element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReceptDTO delele(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
