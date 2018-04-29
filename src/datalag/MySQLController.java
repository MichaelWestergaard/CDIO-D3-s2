package datalag;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

public class MySQLController {
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/testvaegt";
	private static final String USER = "root";
	private static final String PASSWORD = "";
	
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
		
	public MySQLController() throws ClassNotFoundException {
		Class.forName(DRIVER_CLASS);
	}
	
	private Connection startConnection() throws SQLException {
		Connection connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
		return connection;
	}
	
	public Connection getConnection() throws SQLException {
		return startConnection();
	}
		
	public UserDTO getUser(int userID) throws SQLException {
		UserDTO user = null;
		ResultSet results = null;
		
		String query = "SELECT * FROM admin WHERE userID = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, userID);
		results = preparedStatement.executeQuery();
		
		if(results.next()) {
			List<String> roles = Arrays.asList(results.getString("Roller").split(",")); 
			user = new UserDTO(results.getInt("opr_id"), results.getString("brugernavn"), results.getString("opr_fornavn"), results.getString("opr_efternavn"), results.getString("cpr"), "mangler view", roles, results.getInt("status"));
			preparedStatement.close();
			return user;
		}

		preparedStatement.close();
		return null;
	}
	
	public List<UserDTO> getUsers() throws SQLException {
		List<UserDTO> users = new ArrayList<UserDTO>();
		ResultSet results = null;
		
		String query = "SELECT * FROM admin";
		statement = (Statement) getConnection().createStatement();
		results = statement.executeQuery(query);
		
		while(results.next()) {
			List<String> roles = Arrays.asList(results.getString("Roller").split(","));
			UserDTO user = new UserDTO(results.getInt("opr_id"), results.getString("brugernavn"), results.getString("opr_fornavn"), results.getString("opr_efternavn"), results.getString("cpr"), "mangler view", roles, results.getInt("status"));
			users.add(user);
		}
		statement.close();
		return users;
	}
	
	public void createUser(int userID, String userName, String firstName, String lastName, String cpr, String password, List<String> role, int active) throws SQLException {
		if(getUser(userID) == null) {
			UserDTO user = new UserDTO(userID, userName, firstName, lastName, cpr, password, role, active);
			
			String query = "call opretBruger(?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, user.getUserID());
			preparedStatement.setString(2, user.getUserName());
			preparedStatement.setString(3, user.getName());
			preparedStatement.setString(4, user.getLastName());
			preparedStatement.setString(5, user.getCpr());
			preparedStatement.setString(6, user.getPassword());
			preparedStatement.setString(7, String.join(",", user.getRole()));
			preparedStatement.setInt(8, user.getActive());
			preparedStatement.execute();
			preparedStatement.close();
		}		
	}
	
	public boolean updateUser(int userID, String userName, String firstName, String lastName, String cpr, String password, List<String> role, int active) throws SQLException {
		if(getUser(userID) != null) {
			UserDTO user = getUser(userID);
			user.setActive(active);
			user.setCpr(cpr);
			user.setRole(role);
			user.setPassword(password);
			user.setName(firstName);
			user.setLastName(lastName);
			
			String query = "call opdaterBruger(?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, user.getUserID());
			preparedStatement.setString(2, user.getUserName());
			preparedStatement.setString(3, user.getName());
			preparedStatement.setString(4, user.getLastName());
			preparedStatement.setString(5, user.getCpr());
			preparedStatement.setString(6, user.getPassword());
			preparedStatement.setString(7, String.join(",", user.getRole()));
			preparedStatement.setInt(8, user.getActive());
			if(preparedStatement.execute()) {
				preparedStatement.close();
				return true;
			} else {
				preparedStatement.close();
				return false;
			}
		}
		return false;
	}
	
	public boolean deleteUser(int userID) throws SQLException {
		if(getUser(userID) != null) {
			String query = "call sletBruger(?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, userID);
			if(preparedStatement.execute()) {
				preparedStatement.close();
				return true;
			} else {
				preparedStatement.close();
				return false;
			}
		}
		return false;
	}
	
}