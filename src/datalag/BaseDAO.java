package datalag;

import java.sql.SQLException;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

//import datalag.BaseDAO.NotImplementedException;

public interface BaseDAO<T> {
		
	boolean create(T object) throws SQLException, ClassNotFoundException;
	T read(int ID) throws SQLException, ClassNotFoundException;
	boolean update(T object) throws SQLException, ClassNotFoundException;
	List<T> list() throws SQLException, ClassNotFoundException;
	T delete(int ID) throws ClassNotFoundException;
	
	
//	public class NotImplementedException extends Exception {
//	}



}
