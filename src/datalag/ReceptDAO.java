package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReceptDAO implements BaseDAO<ReceptDTO> {

	@Override
	public boolean create(ReceptDTO recept) throws SQLException, ClassNotFoundException {
		if(read(recept.getReceptID()) == null) {
			String query = "Call opretRecept(?, ?)";
			MySQLConnector connector = MySQLConnector.getInstance();
			PreparedStatement preparedStatement = connector.getStatement(query);
			preparedStatement.setInt(1, recept.getReceptID());
			preparedStatement.setString(2, recept.getReceptName());
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
	public ReceptDTO read(int receptID) throws SQLException, ClassNotFoundException {
		ReceptDTO recept = null;
		ResultSet results = null;

		String query = "Select * from recept WHERE recept_id = ?";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		preparedStatement.setInt(1, receptID);
		results = preparedStatement.executeQuery();

		if(results.next()) {
			preparedStatement.close();
			return new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn"));
		}
		preparedStatement.close();
		return null;
	}

	@Override
	public boolean update(ReceptDTO recept) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ReceptDTO> list() throws SQLException, ClassNotFoundException {
		List<ReceptDTO> recepter = new ArrayList<ReceptDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM recept";
		PreparedStatement preparedStatement = MySQLConnector.getInstance().getStatement(query);
		results = preparedStatement.executeQuery(query);

		while(results.next()) {
			recepter.add(new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn")));
		}
		preparedStatement.close();
		return recepter;
	}

	@Override
	public ReceptDTO delete(int receptID) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
