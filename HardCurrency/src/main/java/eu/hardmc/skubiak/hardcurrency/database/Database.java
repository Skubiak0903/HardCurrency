package eu.hardmc.skubiak.hardcurrency.database;

import java.sql.Connection;

public interface Database {
    void connect();
    void disconnect();
    void reconnect();
    void ensureConnection();
    boolean isConnected();
    Connection getConnection();
    String getDatabaseType();
}

