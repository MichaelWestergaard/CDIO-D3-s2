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

//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.Statement;

public class MySQLController {
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
//	private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://mysql25.unoeuro.com/michaelwestergaard_dk_db?useSSL=false";
//	private static final String URL = "jdbc:mysql://mysql25.unoeuro.com/michaelwestergaard_dk_db?useSSL=false&serverTimezone=UTC";
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
	
	public boolean createUser(int userID, String userName, String firstName, String lastName, String cpr, String password, List<String> role, int active) throws SQLException {
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
			return true;
		} else {
			return false;
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
	
	public boolean changeStatus(int userID, int status) throws SQLException {
		String query = "call opdaterStatus(?, ?)";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, userID);
		preparedStatement.setInt(2, status);
		preparedStatement.execute();
		preparedStatement.close();
		return true;
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
		
		String query = "Select * from raavare WHERE raavare_id = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, ingredientID);
		results = preparedStatement.executeQuery();
		
		if(results.next()) {
			ingredient = new IngredientDTO(results.getInt("raavare_id"), results.getString("raavare_navn"), results.getString("leverandoer"));
			preparedStatement.close();
			return ingredient;
		}
		preparedStatement.close();
		return null;
	}
	
	public boolean createIngredient(int ingredientID, String ingredientName, String supplier) throws SQLException {
		if(getIngredient(ingredientID) == null) {
			IngredientDTO ingredient = new IngredientDTO(ingredientID, ingredientName, supplier);
			
			String query = "Call opretRaavare(?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, ingredient.getIngredientID());
			preparedStatement.setString(2, ingredient.getIngredientName());
			preparedStatement.setString(3, ingredient.getSupplier());
			preparedStatement.execute();
			preparedStatement.close();
			return true;
		} else {
			return false;
		}
	}
	
	public List<ProductBatchDTO> getProductBatches() throws SQLException {
		List<ProductBatchDTO> productBatches = new ArrayList<ProductBatchDTO>();
		ResultSet results = null;
		
		String query = "SELECT * FROM produktbatch";
		statement = (Statement) getConnection().createStatement();
		results = statement.executeQuery(query);
		
		while(results.next()) {
			ProductBatchDTO productBatch = new ProductBatchDTO(results.getInt("pb_id"), results.getInt("status"), results.getInt("recept_id"));
			productBatches.add(productBatch);
		}
		statement.close();
		return productBatches;
	
	}
	
	public List<ReceptDTO> getRecepter() throws SQLException {
		List<ReceptDTO> recepter = new ArrayList<ReceptDTO>();
		ResultSet results = null;
		
		String query = "SELECT * FROM recept";
		statement = (Statement) getConnection().createStatement();
		results = statement.executeQuery(query);
		
		while(results.next()) {
			ReceptDTO recept = new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn"));
			recepter.add(recept);
		}
		statement.close();
		return recepter;
	
	}
	
	public List<IngredientDTO> getIngredients() throws SQLException {
		List<IngredientDTO> ingredients = new ArrayList<IngredientDTO>();
		ResultSet results = null;
		
		String query = "SELECT * FROM raavare";
		statement = (Statement) getConnection().createStatement();
		results = statement.executeQuery(query);
		
		while(results.next()) {
			IngredientDTO ingredient = new IngredientDTO(results.getInt("raavare_id"), results.getString("raavare_navn"), results.getString("leverandoer"));
			ingredients.add(ingredient);
		}
		statement.close();
		return ingredients;
	
	}
	
	public List<ReceptComponentDTO> getReceptComponents() throws SQLException {
		List<ReceptComponentDTO> receptComponents = new ArrayList<ReceptComponentDTO>();
		ResultSet results = null;
		
		String query = "SELECT * FROM receptkomponent";
		statement = (Statement) getConnection().createStatement();
		results = statement.executeQuery(query);
		
		while(results.next()) {
			ReceptComponentDTO receptComponent = new ReceptComponentDTO(results.getInt("recept_id"), results.getInt("raavare_id"), results.getDouble("nom_netto"), results.getDouble("tolerance"));
			receptComponents.add(receptComponent);
		}
		statement.close();
		return receptComponents;
	}

	public ReceptComponentDTO getReceptComponent(int receptID, int ingredientID) throws SQLException {
		ReceptComponentDTO receptComponent = null;
		ResultSet results = null;
		
		String query = "Select * from receptkomponent WHERE recept_id = ? and raavare_id = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, receptID );
		preparedStatement.setInt(2, ingredientID);
		results = preparedStatement.executeQuery();
		
		if(results.next()) {
			receptComponent = new ReceptComponentDTO(results.getInt("recept_id"), results.getInt("raavare_id"), results.getDouble("nomNetto"), results.getDouble("tolerance"));
			preparedStatement.close();
			return receptComponent;
		}
		preparedStatement.close();
		return null;
	}
	
	public boolean createReceptComponent(int receptID, int ingredientID, double nomNetto, double tolerance) throws SQLException {
		if(getIngredient(ingredientID) == null && getRecept(receptID) == null)  {
			ReceptComponentDTO receptComponent = new ReceptComponentDTO(receptID, ingredientID, nomNetto, tolerance);
			
			String query = "Call opretRaavare(?, ?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
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

	public List<IngBatchDTO> getIngBatches() throws SQLException {
		List<IngBatchDTO> ingBatches = new ArrayList<IngBatchDTO>();
		ResultSet results = null;

		String query = "SELECT * FROM raavarebatch";
		statement = (Statement) getConnection().createStatement();
		results = statement.executeQuery(query);
		
		while(results.next()) {
			IngBatchDTO ingBatch = new IngBatchDTO(results.getInt("rb_id"), results.getInt("raavare_id"), results.getDouble("maengde"));
			ingBatches.add(ingBatch);
		}
		statement.close();
		return ingBatches;

	
	}
	
	public ReceptDTO getRecept(int receptID) throws SQLException {
		ReceptDTO recept = null;
		ResultSet results = null;
		
		String query = "Select * from recept WHERE recept_id = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, receptID);
		results = preparedStatement.executeQuery();
		
		if(results.next()) {
			recept = new ReceptDTO(results.getInt("recept_id"), results.getString("recept_navn"));
			preparedStatement.close();
			return recept;
		}
		preparedStatement.close();
		return null;
	}
	
	public boolean createRecept(int receptID, String receptName) throws SQLException {
		if(getRecept(receptID) == null) {
			ReceptDTO recept = new ReceptDTO(receptID, receptName);
			
			String query = "Call opretRecept(?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, recept.getReceptID());
			preparedStatement.setString(2, recept.getReceptName());
			preparedStatement.execute();
			preparedStatement.close();
			return true;
		} else {
			return false;	
		}
	}
	
	public ProductBatchDTO getProductBatch(int productBatchID) throws SQLException {
		ProductBatchDTO productBatch = null;
		ResultSet results = null;
		
		String query = "Select * from productbatch WHERE pb_id = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, productBatchID);
		results = preparedStatement.executeQuery();
		
		if(results.next()) {
			productBatch = new ProductBatchDTO(results.getInt("pb_id"), results.getInt("status"), results.getInt("recept_id"));
			preparedStatement.close();
			return productBatch;
		}
		preparedStatement.close();
		return null;
	}
	
	public boolean createProductBatch(int productBatchID, int status, int receptID) throws SQLException {
		if(getProductBatch(productBatchID) == null) {
			ProductBatchDTO productBatch = new ProductBatchDTO(productBatchID, status, receptID);
			
			String query = "Call opretProduktBatch(?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, productBatch.getProductBatchID());
			preparedStatement.setInt(2, productBatch.getStatus());
			preparedStatement.setInt(3, productBatch.getReceptID());
			preparedStatement.execute();
			preparedStatement.close();
			return true;
		} else {
			return false;	
		}
	}
	
	public IngBatchDTO getIngBatch(int ingBatchID) throws SQLException {
		IngBatchDTO ingBatch = null;
		ResultSet results = null;
		
		String query = "Select * from raavarebatch WHERE rb_id = ?";
		preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
		preparedStatement.setInt(1, ingBatchID);
		results = preparedStatement.executeQuery();
		
		if(results.next()) {
			ingBatch = new IngBatchDTO(results.getInt("rb_id"), results.getInt("raavare_id"), results.getDouble("maengde"));
			preparedStatement.close();
			return ingBatch;
		}
		preparedStatement.close();
		return null;
	}
	
	public boolean createIngBatch(int ingBatchID, int ingredientID, double amount) throws SQLException {
		if(getIngBatch(ingBatchID) == null) {
			IngBatchDTO ingBatch = new IngBatchDTO(ingBatchID, ingredientID, amount);
			
			String query = "Call opretProduktBatch(?, ?, ?)";
			preparedStatement = (PreparedStatement) getConnection().prepareStatement(query);
			preparedStatement.setInt(1, ingBatch.getIngBatchID());
			preparedStatement.setInt(2, ingBatch.getIngredientID());
			preparedStatement.setDouble(3, ingBatch.getAmount());
			preparedStatement.execute();
			preparedStatement.close();
			return true;
		} else {
			return false;	
		}
	}
}