package umu.tds.appchat.persistencia;

import java.util.Hashtable;

public class PoolDAO {

	private Hashtable<Integer, Object> pool;
	private static PoolDAO unicaInstancia;
	
	private PoolDAO(){
		pool = new Hashtable<Integer,Object>();
	}
	
	public static PoolDAO getUnicaInstancia() {
		if (unicaInstancia == null)
			unicaInstancia = new PoolDAO();
		return unicaInstancia;
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
	
	public void removeObject(int id) {
		pool.remove(id);
	}

}
