package eu.hardmc.skubiak.hardcurrency.account;

import java.util.concurrent.ConcurrentHashMap;

import eu.hardmc.skubiak.hardcurrency.utils.HashMapUtils;

public class Currency {
	
	//private HardCurrency plugin;
	
	public static ConcurrentHashMap<Integer, String> currencies = new ConcurrentHashMap<>();
	
	private int id;
	private String name;
	
	/*public Currency(HardCurrency plugin) {
		//this.plugin = plugin;
	}*/
	
	private Currency(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	//get currency instance with id and name
	public static Currency getCurrencyFromName(String name) {
		if (!currencies.containsValue(name)) {
			return null;
		}
		
		int id = (Integer) HashMapUtils.getKeyFromValue(currencies, name);
		if (id == 0) {
			return null;
		}
		
		return new Currency(id, name);
	}
	
	public static Currency getCurrencyFromId(int id) {
		if (!currencies.containsKey(id)) {
			return null;
		}
		return new Currency(id, currencies.get(id));
	}
	
	
	//getters
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
}
