package datalag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO implements BaseDAO<UserDTO> {
	
	@Override
	public boolean create(UserDTO user) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "call opretBruger(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, user.getUserID());
		preparedStatement.setString(2, user.getUserName());
		preparedStatement.setString(3, user.getName());
		preparedStatement.setString(4, user.getLastName());
		preparedStatement.setString(5, user.getCpr());
		preparedStatement.setString(6, user.getPassword());
		preparedStatement.setString(7, user.getInitial());
		preparedStatement.setString(8, String.join(",", user.getRole()));
		preparedStatement.setInt(9, user.getActive());
		if(connector.execute(preparedStatement)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public UserDTO read(int ID) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "SELECT * FROM admin WHERE opr_id = ?";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, ID);
		ResultSet result = connector.doQuery(preparedStatement);

		if(result.next()) {
			List<String> roles = Arrays.asList(result.getString("Roller").split(","));
			return new UserDTO(result.getInt("opr_id"), result.getString("brugernavn"), result.getString("opr_fornavn"), result.getString("opr_efternavn"), result.getString("cpr"), result.getString("Password"), result.getString("initialer"), roles, result.getInt("status"));
		}
		return null;
	}

	@Override
	public boolean update(UserDTO user) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "call opdaterBruger(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, user.getUserID());
		preparedStatement.setString(2, user.getUserName());
		preparedStatement.setString(3, user.getName());
		preparedStatement.setString(4, user.getLastName());
		preparedStatement.setString(5, user.getCpr());
		preparedStatement.setString(6, user.getPassword());
		preparedStatement.setString(7, user.getInitial());
		preparedStatement.setString(8, String.join(",", user.getRole()));
		preparedStatement.setInt(9, user.getActive());
		if(connector.execute(preparedStatement)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<UserDTO> list() throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		List<UserDTO> users = new ArrayList<UserDTO>();

		String query = "SELECT * FROM admin";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		ResultSet results = connector.doQuery(preparedStatement);

		while(results.next()) {
			List<String> roles = Arrays.asList(results.getString("Roller").split(","));
			UserDTO user = new UserDTO(results.getInt("opr_id"), results.getString("brugernavn"), results.getString("opr_fornavn"), results.getString("opr_efternavn"), results.getString("cpr"), results.getString("Password"), results.getString("initialer"), roles, results.getInt("status"));
			users.add(user);
		}
		preparedStatement.close();
		return users;
	}
	
	public boolean changeStatus(UserDTO user) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "call opdaterStatus(?, ?)";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, user.getUserID());
		preparedStatement.setInt(2, user.getActive());
		if(connector.execute(preparedStatement)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean resetPassword(UserDTO user) throws SQLException, ClassNotFoundException {
		MySQLConnector connector = MySQLConnector.getInstance();
		
		String query = "call nulstilKode(?, ?)";
		PreparedStatement preparedStatement = (PreparedStatement) connector.getStatement(query);
		preparedStatement.setInt(1, user.getUserID());
		preparedStatement.setString(2, user.getPassword());
		if(connector.execute(preparedStatement)) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public UserDTO delete(int ID) throws NotImplementedException {
		throw new NotImplementedException("Denne metode er ikke lavet");
	}
}