package datalag;

import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

//import datalag.BaseDAO.NotImplementedException;

public interface BaseDAO<T> {
		
	boolean create(int ID, String[] parameters) throws SQLException;
	T read(String id);
	T update(T element);
	T delele(String id);
	
	
//	public class NotImplementedException extends Exception {
//	}



}
