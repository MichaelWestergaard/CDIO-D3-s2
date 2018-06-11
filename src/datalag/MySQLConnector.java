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
	
	private static MySQLConnector connector;
	private Connection connection;
	
	private MySQLConnector() throws ClassNotFoundException, SQLException {
		//initialize connection!
		Class.forName(DRIVER_CLASS);
		connection = (Connection) DriverManager.getConnection(URL, USER, PASSWORD);
	}
	
	public static MySQLConnector getInstance() throws ClassNotFoundException, SQLException {
		if (connector == null || connector.getConnection() == null) {
			connector = new MySQLConnector();
		}
		return connector;
	}
	
	private Connection getConnection() {
		return connection;
	}

	public PreparedStatement getStatement(String sql) throws SQLException{
		return connection.prepareStatement(sql);
		//TODO 
	}
	
	ResultSet doQuery(PreparedStatement statement) {
		return null;
		//TODO
	}
	
	void doUpdate(Statement stmnt) {
		
	}

}