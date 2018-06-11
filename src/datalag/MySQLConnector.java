package datalag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLConnector {
	private static MySQLConnector connector;
	private Connection conn;
	
	private MySQLConnector() {
		//initialize connection!
		
	}
	
	public static MySQLConnector getInstance() {
		if (connector== null) { // and check connection!
			connector = new MySQLConnector();
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
