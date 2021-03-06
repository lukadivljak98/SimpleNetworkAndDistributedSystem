package net.etfbl.rest;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import model.Linija;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Service {
	
	public JedisPool pool = new JedisPool("localhost");
	public ArrayList<Linija> list = new ArrayList<>();
	private static String instanceName = "Lines";
	private Gson json = new Gson();
	
	public Service() {
		addLinesToList();
	}
	
	public ArrayList<Linija> getLinije(){
		return this.list;
	}
	
	public Linija getByName(String name) {
		for(Linija l : list) {
			if(l.getNaziv().equals(name))
				return l;
		}
		return null;
	}
	
	public boolean add(Linija l) {
		return list.add(l);
	}
	
	public boolean update(String linija, String stanicaProlazak) {
		int index = list.indexOf(new Linija(linija));
		if(index >= 0) {
			list.get(index).setStanicaProlazak(stanicaProlazak);
			return true;
		}
		else
			return false;
	}
	
	public boolean remove(String linija) {
		int index = list.indexOf(new Linija(linija));
		if(index >= 0) {
			list.remove(index);
			return true;
		}
		else 
			return false;
	}
	
	public void addLinesToList() {
		List<String> response;
		try (Jedis jedis = pool.getResource()) {
			response = jedis.lrange(instanceName, 0, -1);
			for(String s : response) {
				Linija line = json.fromJson(s, Linija.class);
				list.add(line);
			}
		}
	}

}
