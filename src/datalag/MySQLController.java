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
	private static final String URL = "jdbc:mysql://mysql25.unoeuro.com/michaelwestergaard_dk_db?useSSL=false&serverTimezone=CEST";
	private static final String USER = "michaelwest_dk";
	private static final String PASSWORD = "68wukovuzovi";
	
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
		
		String query = "SELECT * FROM admin WHERE opr_id = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, userID);
		results = preparedStatement.executeQuery();
		
		if(results.next()) {
			List<String> roles = Arrays.asList(results.getString("Roller").split(",")); 
			user = new UserDTO(results.getInt("opr_id"), results.getString("brugernavn"), results.getString("opr_fornavn"), results.getString("opr_efternavn"), results.getString("cpr"), results.getString("Password"), roles, results.getInt("status"));
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
			UserDTO user = new UserDTO(results.getInt("opr_id"), results.getString("brugernavn"), results.getString("opr_fornavn"), results.getString("opr_efternavn"), results.getString("cpr"), results.getString("Password"), roles, results.getInt("status"));
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
	
	public boolean updateUser(int userID, String userName, String firstName, String lastName, String cpr, String password, String role, int active) throws SQLException {
			String query = "call opdaterBruger(?, ?, ?, ?, ?, ?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, userID);
			preparedStatement.setString(2, userName);
			preparedStatement.setString(3, firstName);
			preparedStatement.setString(4, lastName);
			preparedStatement.setString(5, cpr);
			preparedStatement.setString(6, password);
			preparedStatement.setString(7, role);
			preparedStatement.setInt(8, active);
			preparedStatement.execute();
			preparedStatement.close();
			return true;
	}
	
	public void deleteUser(int userID) throws SQLException {
		String query = "call sletBruger(?)";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, userID);
		preparedStatement.execute();
		preparedStatement.close();
	}
	
	//Mangler storedProcedure 'nulstilkode', og eventuelt også en ændring i viewet.
	public boolean resetPassword(int userID, String password) throws SQLException {
		String query = "call nulstilKode(?, ?)";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, userID);
		preparedStatement.setString(2, password);
		preparedStatement.execute();
		preparedStatement.close();
		return true;
	}
	
	
	public IngredientDTO getIngredient(int ingredientID) throws SQLException {
		IngredientDTO ingredient = null;
		ResultSet results = null;
		
		String query = "Selet * from raavare WHERE raavare_id = =";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, ingredientID);
		
		if(results.next()) {
			ingredient = new IngredientDTO(results.getInt("raavare_id"), results.getString("raavare_navn"), results.getString("leverandoer"));
			preparedStatement.close();
			return ingredient;
		}
		preparedStatement.close();
		return null;
	}
	
	public void createIngredient(int ingredientID, String ingredientName, String supplier) throws SQLException {
		if(getIngredient(ingredientID) == null) {
			IngredientDTO ingredient = new IngredientDTO(ingredientID, ingredientName, supplier);
			
			String query = "Call opretRaavare(?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, ingredient.getIngredientID());
			preparedStatement.setString(2, ingredient.getIngredientName());
			preparedStatement.setString(3, ingredient.getSupplier());
			preparedStatement.execute();
			preparedStatement.close();
					
		}
	}
	
	
	
	
	
	
	
}