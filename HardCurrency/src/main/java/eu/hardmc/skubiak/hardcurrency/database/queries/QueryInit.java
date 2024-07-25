package eu.hardmc.skubiak.hardcurrency.database.queries;

public class QueryInit {
    public static Query createQuery(String dbType) {
        switch (dbType.toLowerCase()) {
            case "mysql":
                return new MySQLQuery();
            case "sqlite":
                return new SQLiteQuery();
            default:
                throw new UnsupportedOperationException("Unsupported database type: " + dbType);
        }
    }
}

