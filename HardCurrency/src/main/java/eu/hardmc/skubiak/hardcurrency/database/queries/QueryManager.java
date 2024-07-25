package eu.hardmc.skubiak.hardcurrency.database.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import eu.hardmc.skubiak.hardcurrency.database.Database;

public class QueryManager {

    private Database database;

    
    public QueryManager(Database database) {
        this.database = database;
    }
    
    //public
    public void execute(String query, Object... values) {
    	try {
    		throwsExecute(query, values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public QueryResult executeQuery(String query, Object... values) {
    	try {
    		return throwsExecuteQuery(query, values);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
    }

    public void throwsExecute(String query, Object... values) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
        	database.ensureConnection();
            connection = database.getConnection();
            statement = connection.prepareStatement(query);
            setValues(statement, values);
            statement.execute();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
    

    public QueryResult throwsExecuteQuery(String query, Object... values) throws SQLException {
    	database.ensureConnection();
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        setValues(statement, values);
        ResultSet resultSet = statement.executeQuery();
        return new QueryResult(resultSet, statement);
    }
    
    public Database getDatabase() {
    	return database;
    }
    
    //private
    private void setValues(PreparedStatement statement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
    }
}

