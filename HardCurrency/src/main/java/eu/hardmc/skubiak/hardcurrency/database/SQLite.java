package eu.hardmc.skubiak.hardcurrency.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;

public class SQLite implements Database {

    private HardCurrency plugin;
    private String path;
    private Connection connection;

    public SQLite(HardCurrency plugin) {
        this.plugin = plugin;
        this.path = plugin.getConfig().getString("database.sqlite.path", "database.db");
    }

    @Override
    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            path = plugin.getDataFolder().getAbsolutePath() + "/" + path;
            File file = new File(path);
            if (!file.exists()) {
            	file.getParentFile().mkdirs();
                file.createNewFile();
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            plugin.getLogger().info("Successfully connected to SQLite database.");
            initialize();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            plugin.getLogger().severe("An error occurred while connecting to the SQLite database! Plugin shutting down");
            e.printStackTrace();
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }
    
    private void initialize() {
        // Query to check if tables exists
        String query = "SELECT 1 FROM HardCurrency_Currencies CROSS JOIN HardCurrency_Players;";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
        	statement.executeQuery();
        } catch (SQLException e) {
        	// Ensure if table doesn't exists
        	if (e.getMessage().contains("no such table")) {
            	plugin.getLogger().info("Missing tables! Creating new tables.");
                try (Statement statement = connection.createStatement()) {
                	createTables(statement);
                	plugin.getLogger().info("Created missing tables.");
                } catch (SQLException e2) {
                	plugin.getLogger().severe("An error occurred while creating tables in the SQLite database!");
                    e2.printStackTrace();
                }
            } else {
                // Other Sql error
                e.printStackTrace();
            }
        }
    }
    
    private void createTables(Statement statement) throws SQLException {
        // Creating table HardCurrency_Currencies
    	String createTableSQL;
        createTableSQL = "CREATE TABLE IF NOT EXISTS HardCurrency_Currencies ("
			                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
			                + "name TEXT NOT NULL"
			                + ")";
        statement.execute(createTableSQL);

        // Creating table HardCurrency_Players
        createTableSQL = "CREATE TABLE IF NOT EXISTS HardCurrency_Players ("
			                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
			                + "uuid VARCHAR(36) NOT NULL,"
			                + "currency_id INT NOT NULL,"
			                + "currency_amount FLOAT NOT NULL"
			                + ");";
        statement.execute(createTableSQL);
    }

    @Override
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().severe("An error occurred while closing SQLite connection!");
        }
    }
    
    @Override
    public void reconnect() {
    	disconnect();
    	connect();
    }
    
    @Override
    public void ensureConnection() {
        if (!isConnected()) {
            reconnect();
        }
    }
    
    @Override
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
    
    @Override
    public String getDatabaseType() {
    	return "SQLite";
    }
}

