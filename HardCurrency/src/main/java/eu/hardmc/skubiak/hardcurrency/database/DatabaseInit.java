package eu.hardmc.skubiak.hardcurrency.database;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;

public class DatabaseInit {
	
	Database db;
	
	public DatabaseInit(HardCurrency plugin) {
		//conditions to enable mysql
		if (plugin.getConfig().getString("database.type", "SQLITE").toUpperCase().equals("MYSQL")) {
			db = new MySQL(plugin);
		} else {
			db = new SQLite(plugin);
		}
	}
	
	public void setDatabase(Database db) {
		this.db = db;
	}
	
	public Database getDatabase() {
		return db;
	}
}
