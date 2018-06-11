package datalag;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

//import datalag.BaseDAO.NotImplementedException;

public interface BaseDAO<T> {
		
	boolean create(int ID, String[] parameters) throws SQLException;
	T read(int ID);
	boolean update(int ID, String[] parameters) throws SQLException;
	T delete(int ID);
	
	
//	public class NotImplementedException extends Exception {
//	}



}
