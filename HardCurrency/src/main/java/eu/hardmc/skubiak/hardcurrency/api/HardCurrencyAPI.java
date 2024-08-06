package eu.hardmc.skubiak.hardcurrency.api;

import java.util.List;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;
import eu.hardmc.skubiak.hardcurrency.account.Currency;

public class HardCurrencyAPI {
	
	public static HardCurrency plugin;
	
	//Currency Manager
	public static void addCurrency(String name) {
		plugin.getCurrencyManager().addCurrency(name);
	}
	
	public static void removeCurrency(Currency curreny) {
		plugin.getCurrencyManager().removeCurrency(curreny);
	}
	
	public static void removeCurrency(int id) {
		plugin.getCurrencyManager().removeCurrency(id);
	}
	
	public static void removeCurrency(String name) {
		plugin.getCurrencyManager().removeCurrency(name);
	}
	
	public static void refreshCurrencies() {
		plugin.getCurrencyManager().refreshCurrencies();
	}
	
	public static List<String> getAllCurrencies() {
		return plugin.getCurrencyManager().getAllCurrencies();
	}
	
	//Player
}
