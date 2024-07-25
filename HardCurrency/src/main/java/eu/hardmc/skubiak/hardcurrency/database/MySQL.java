package eu.hardmc.skubiak.hardcurrency.database;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import eu.hardmc.skubiak.hardcurrency.HardCurrency;

public class MySQL implements Database {
	
	private HardCurrency plugin;
    
    private Connection connection;
    
    private String host;
    private int port;
    private String username;
    private String database;
    private String password;
    private String connSettings;

    public MySQL(HardCurrency plugin) {
    	this.plugin = plugin;
    	this.host = plugin.getConfig().getString("database.mysql.host", "localhost");
    	this.port = plugin.getConfig().getInt("database.mysql.port", 3306);
    	this.username = plugin.getConfig().getString("database.mysql.username", "root");
    	this.database = plugin.getConfig().getString("database.mysql.database", "database");
    	this.password = plugin.getConfig().getString("database.mysql.password", "");
    	this.connSettings = plugin.getConfig().getString("database.mysql.connSettings", "characterEncoding=UTF-8&useSSL=false");
    }

    @Override
    public void connect() {
        try {
        	String encodedPassword = URLEncoder.encode(this.password, "UTF-8");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException e1) {
                	plugin.getLogger().severe("Cannot find MySQL JDBC Driver! Plugin shutting down.");
                	plugin.getPluginLoader().disablePlugin(plugin);
                }
            }
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host +":" + this.port + "/" + this.database + "?user=" + this.username + "&password=" + encodedPassword + "&" + connSettings);
            plugin.getLogger().info("Successfully connected to MySQL database.");
            initialize();
        } catch (SQLException | UnsupportedEncodingException e) {
            plugin.getLogger().severe("An error occurred while connecting to the MySQL database! Plugin shutting down");
            e.printStackTrace();
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }
    
    private void initialize() {
        // Query to check if tables exists
        String query = "SELECT 1 FROM HardCurrency_Currencies, HardCurrency_Players";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
        	statement.executeQuery();
        } catch (SQLException e) {
        	// Ensure if table doesn't exists
        	if (e.getMessage().contains("Table") && e.getMessage().contains("doesn't exist")) {
            	plugin.getLogger().info("Missing tables! Creating new tables.");
                try (Statement statement = connection.createStatement()) {
                	createTables(statement);
                	plugin.getLogger().info("Created missing tables.");
                } catch (SQLException e2) {
                	plugin.getLogger().severe("An error occurred while creating tables in the MySQL database!");
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
        createTableSQL = "CREATE TABLE IF NOT EXISTS `HardCurrency_Currencies` ("
							+ "`id` INT AUTO_INCREMENT PRIMARY KEY,"
							+ "`name` tinytext NOT NULL"
							+ ")";
        statement.execute(createTableSQL);

        // Creating table HardCurrency_Players
        createTableSQL = "CREATE TABLE IF NOT EXISTS HardCurrency_Players ("
			        		+ "id INT AUTO_INCREMENT PRIMARY KEY,"
			        		+ "uuid VARCHAR(36) NOT NULL,"
			        		+ "currency_id INT NOT NULL,"
			        		+ "currency_amount FLOAT NOT NULL,"
			        		+ "INDEX (uuid),"
			        		+ "INDEX (currency_id)"
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
            plugin.getLogger().severe("An error occurred while closing MySQL connection!");
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
    	return "MySQL";
    }
}