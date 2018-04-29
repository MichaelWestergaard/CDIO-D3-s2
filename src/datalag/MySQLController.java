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
	
	private UserDAO userDAO = new UserDAO();
	
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
	
	public void getUsers() throws SQLException {
		ResultSet results = null;
		
		String query = "SELECT * FROM admin";
		statement = (Statement) getConnection().createStatement();
		results = statement.executeQuery(query);
		
		while(results.next()) {
			List<String> roles = Arrays.asList(results.getString("Roller").split(",")); 
			userDAO.createUser(results.getInt("userID"), results.getString("brugernavn"), results.getString("opr_fornavn"), results.getString("opr_efternavn"), results.getString("cpr"), "Skal ændres i view", roles, results.getInt("status"));
		}
		statement.close();
	}
	
	public void createUser(UserDTO user) throws SQLException {
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