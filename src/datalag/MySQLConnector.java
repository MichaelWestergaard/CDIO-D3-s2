package datalag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQLConnector {
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
//	private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://mysql25.unoeuro.com/michaelwestergaard_dk_db?useSSL=false&zeroDateTimeBehavior=convertToNull";
//	private static final String URL = "jdbc:mysql://mysql25.unoeuro.com/michaelwestergaard_dk_db?useSSL=false&serverTimezone=UTC";//&zeroDateTimeBehavior=convertToNull
	private static final String USER = "michaelwest_dk";
	private static final String PASSWORD = "68wukovuzovi";

	private Statement statement = null;
	private PreparedStatement preparedStatement = null;

	public MySQLConnector() throws ClassNotFoundException {
		Class.forName(DRIVER_CLASS);
	}

	private static Connection startConnection() throws SQLException {
		Connection connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
		return connection;
	}

	public static Connection getConnection() throws SQLException {
		return startConnection();
	}
	
	
	
	private static MySQLConnector connector;
	private Connection conn;
	
	
	
	public static MySQLConnector getInstance() {
		try {
			if (connector== null && getConnection() == null) { // and check connection!
				connector = new MySQLConnector();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connector;
	}
	
	PreparedStatement getStatement(){
		//TODO 
	}
	
	ResultSet doQuery(PreparedStatement statement) {
		return null;
		//TODO
	}
	
	void doUpdate(Statement stmnt) {
		
	}
	

	
	
	
}
