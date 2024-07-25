package eu.hardmc.skubiak.hardcurrency.database.queries;

public interface Query {
	// Currency Managment
	String selectAllCurrencies();
	String addCurrency();
	String removeCurrencyUsingId();
	String removeCurrencyUsingName();
	
	
	//Player Currency Amount Managment
	String insertCurrencyToPlayer();
	String setPlayerCurrency();
	String getPlayerCurrencyAmount();
}
