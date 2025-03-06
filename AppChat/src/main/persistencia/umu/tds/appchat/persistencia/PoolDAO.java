package umu.tds.appchat.persistencia;

import java.util.Hashtable;

public enum PoolDAO {

	INSTANCE;
	private Hashtable<Integer, Object> pool;
	
	private PoolDAO(){
		pool = new Hashtable<Integer,Object>();
	}
	
	public Object getObject(int id) {
		return pool.get(id);
	}
	
	public void addObject(int id, Object object) {
		pool.put(id, object);
	}
	
	public boolean contains(int id){
		return pool.containsKey(id);
	}

}
