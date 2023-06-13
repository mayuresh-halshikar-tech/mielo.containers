package org.mielo.containers;

public class DatabaseConfigurationHolder {
    private static DatabaseConfiguration databaseConfiguration;

    public static void setDatabaseConfiguration(DatabaseConfiguration configuration) {
        databaseConfiguration = configuration;
    }

    public static DatabaseConfiguration getDatabaseConfiguration() {
        return databaseConfiguration;
    }
}
