package datalag;

import java.sql.SQLException;
import java.util.List;

public interface BaseDAO<T> {
		
	boolean create(T object) throws SQLException, ClassNotFoundException, NotImplementedException;
	T read(int ID) throws SQLException, ClassNotFoundException, NotImplementedException;
	boolean update(T object) throws SQLException, ClassNotFoundException, NotImplementedException;
	List<T> list() throws SQLException, ClassNotFoundException, NotImplementedException;
	T delete(int ID) throws NotImplementedException;
	
	
	public class NotImplementedException extends Exception {

		private static final long serialVersionUID = 3672121609440631944L;

		public NotImplementedException(String msg) {
			super(msg);
		}
	}

}