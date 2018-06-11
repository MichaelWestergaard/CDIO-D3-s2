package datalag;

import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

//import datalag.BaseDAO.NotImplementedException;

public interface BaseDAO<T> {
		
	boolean create(T object) throws SQLException;
	T read(int ID);
	boolean update(T object) throws SQLException;
	List<T> list() throws SQLException;
	T delete(int ID);
	
	
//	public class NotImplementedException extends Exception {
//	}



}
