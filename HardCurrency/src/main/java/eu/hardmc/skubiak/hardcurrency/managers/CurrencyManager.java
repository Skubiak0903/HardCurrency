package eu.hardmc.skubiak.hardcurrency.managers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.scheduler.BukkitScheduler;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;
import eu.hardmc.skubiak.hardcurrency.account.Currency;
import eu.hardmc.skubiak.hardcurrency.database.queries.QueryManager;
import eu.hardmc.skubiak.hardcurrency.database.queries.QueryResult;

public class CurrencyManager {
	private HardCurrency plugin;
	
	public static ConcurrentHashMap<Integer, String> currencies = Currency.currencies;
	
	public CurrencyManager(HardCurrency plugin) {
		this.plugin = plugin;
		int autoRefreshTime = Math.abs(plugin.getConfig().getInt("currencies.autoRefresh", 60));
		if (autoRefreshTime != 0) autoRefresh(60);
	}
	
	private void autoRefresh(int seconds) {
		refreshCurrencies();
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
            	refreshCurrencies();
            }
        }, 0L, seconds * 20L);
	}
	
    public synchronized void refreshCurrencies() {
        currencies.clear();
        // Add currencies from database
        QueryManager query = plugin.getQueryManager();
        
        try (QueryResult queryResult = query.executeQuery(HardCurrency.getQuery().selectAllCurrencies())) {
            ResultSet resultSet = queryResult.getResultSet();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                
                currencies.put(id, name);
            }
        } catch (SQLException e) {
        	plugin.getLogger().warning("An error occured when refreshing Currenices!");
			e.printStackTrace();
		} 

    }
	
	public Currency addCurrency(String name) {
		//database add new currency record
		if (currencies.contains(name)) return Currency.getCurrencyFromName(name);
		QueryManager query = plugin.getQueryManager();
		query.execute(HardCurrency.getQuery().addCurrency(), name);
		refreshCurrencies();
        return Currency.getCurrencyFromName(name);
	}
	
	public void removeCurrency(int id) {
		//database remove record of currency
		QueryManager query = plugin.getQueryManager();
		query.execute(HardCurrency.getQuery().removeCurrencyUsingId(), id);
		refreshCurrencies();
	}
	
	public void removeCurrency(String name) {
		//database remove record of currency
		QueryManager query = plugin.getQueryManager();
		query.execute(HardCurrency.getQuery().removeCurrencyUsingName(), name);
		refreshCurrencies();
	}
	
	public List<String> getAllCurrencies() {
		List<String> currenciesList = new ArrayList<String>();
        for (Entry<Integer, String> entry : currencies.entrySet()) {
        	currenciesList.add(entry.getValue());
        }
		return currenciesList;
	}
}
