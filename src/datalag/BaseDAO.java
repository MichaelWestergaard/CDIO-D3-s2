package datalag;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

//import datalag.BaseDAO.NotImplementedException;

public interface BaseDAO<T> {
	
	Statement statement = null;
	PreparedStatement preparedStatement = null;
	
	boolean create(T element) throws SQLException;
	T read(String id);
	T update(T element);
	T delele(String id);
	
	
//	public class NotImplementedException extends Exception {
//	}



}
