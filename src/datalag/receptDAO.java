package datalag;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

import datalag.ReceptDTO;


public abstract class receptDAO implements BaseDAO<ReceptDTO> {

	@Override
	public boolean create(ReceptDTO recept) throws SQLException, ClassNotFoundException {
		
		if(read(recept.getReceptID()) == null) {

			String query = "Call opretRecept(?, ?)";
			PreparedStatement preparedStatement = null;
			
			try {
				preparedStatement = MySQLConnector.getInstance().getStatement(query);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, recept.getReceptID());
			preparedStatement.setString(2, recept.getReceptName());
			preparedStatement.execute();
			preparedStatement.close();
			return true;
		} else {
			return false;	
		}
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
	public List<ReceptDTO> list() throws SQLException, ClassNotFoundException {
	List<ReceptDTO> recepter = new ArrayList<ReceptDTO>();
	ResultSet results = null;

	String query = "SELECT * FROM recept";
	PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
	results = preparedStatement.executeQuery(query);

	while(results.next()) {
		ReceptDTO recept = new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn"));
		recepter.add(recept);
	}
	preparedStatement.close();
	return recepter;

}

	
}