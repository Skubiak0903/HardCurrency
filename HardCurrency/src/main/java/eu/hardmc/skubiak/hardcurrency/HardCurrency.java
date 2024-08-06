package eu.hardmc.skubiak.hardcurrency;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import eu.hardmc.skubiak.hardcurrency.api.HardCurrencyAPI;
import eu.hardmc.skubiak.hardcurrency.cmds.HardCurrencyCMD;
import eu.hardmc.skubiak.hardcurrency.cmds.HardCurrencyTabCompleter;
import eu.hardmc.skubiak.hardcurrency.database.Database;
import eu.hardmc.skubiak.hardcurrency.database.DatabaseInit;
import eu.hardmc.skubiak.hardcurrency.database.queries.Query;
import eu.hardmc.skubiak.hardcurrency.database.queries.QueryInit;
import eu.hardmc.skubiak.hardcurrency.database.queries.QueryManager;
import eu.hardmc.skubiak.hardcurrency.hooks.PlaceholderAPIHook;
import eu.hardmc.skubiak.hardcurrency.managers.CurrencyManager;

public class HardCurrency extends JavaPlugin {
	Database db;
	QueryManager qm;
	CurrencyManager cm;
	
	static Query query;
	
	public void onEnable() {
		long startTime = System.nanoTime();  // Start timer
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
		
		db = new DatabaseInit(this).getDatabase();
		db.connect();
		
		qm = new QueryManager(db);
		
		query = QueryInit.createQuery(db.getDatabaseType());
		
		cm = new CurrencyManager(this);
		
		HardCurrencyAPI.plugin = this;
		
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) { // 
            PlaceholderAPIHook.registerHook(); 
        }
		
        this.getCommand("hardcurrency").setExecutor(new HardCurrencyCMD(this));
        this.getCommand("hardcurrency").setTabCompleter(new HardCurrencyTabCompleter(this));
        this.getCommand("hardcurrency").setAliases(Arrays.asList("currency","hc"));
		
		//this.getServer().getPluginManager().registerEvents(new Test(this), this);
		
		long endTime = System.nanoTime();    // End timer
		long duration = (endTime - startTime) / 1_000_000;  // Convert to milliseconds
		this.getLogger().info("Successfully enabled in " + duration + "ms!");
	}
	
	public void onDisable() {
		db.disconnect();
		this.getLogger().info(ChatColor.RED + "Disabled HardCurrency...");
	}
	
	public Database getDatabase() {
		return db;
	}
	
	public QueryManager getQueryManager() {
		return qm;
	}
	
	public static Query getQuery() {
		return query;
	}
	
	public CurrencyManager getCurrencyManager() {
		return cm;
	}

}
