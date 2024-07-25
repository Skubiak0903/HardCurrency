package eu.hardmc.skubiak.hardcurrency.database.queries;

import java.io.Closeable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResult implements Closeable {
    private final ResultSet resultSet;
    private final PreparedStatement statement;

    public QueryResult(ResultSet resultSet, PreparedStatement statement) {
        this.resultSet = resultSet;
        this.statement = statement;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    @Override
    public void close() {
        if (resultSet != null) {
            try {
				resultSet.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
        if (statement != null) {
            try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        }
    }
}
