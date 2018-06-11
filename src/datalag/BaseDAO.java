package datalag;

import datalag.BaseDAO.NotImplementedException;

public interface BaseDAO<T> {
	T create(T element);
	T read(String id);
	T update(T element);
	T delele(String id);
	
	
	public class NotImplementedException extends Exception {

	}



}
