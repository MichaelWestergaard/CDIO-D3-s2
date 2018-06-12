package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
		MySQLConnector connector = MySQLConnector.getInstance();
		
		ReceptDTO recept = null;
		ResultSet results = null;

		String query = "Select * from recept WHERE recept_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, receptID);
		results = connector.doQuery(preparedStatement);

		if(results.next()) {
			preparedStatement.close();
			return new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn"));
		}
		preparedStatement.close();
		return null;
	}

	@Override
	public boolean update(ReceptDTO recept) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}

	@Override
	public List<ReceptDTO> list() throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		List<ReceptDTO> recepter = new ArrayList<ReceptDTO>();

		String query = "SELECT * FROM recept";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		ResultSet results = connector.doQuery(preparedStatement);

		while(results.next()) {
			recepter.add(new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn")));

		}
		preparedStatement.close();
		return recepter;
	}

	@Override
	public ReceptDTO delete(int receptID) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}

}