package com.helper;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.springframework.beans.factory.FactoryBean;

import javax.sql.DataSource;

public class CustomDatabaseConnection  implements FactoryBean<DatabaseDataSourceConnection>{
    private DataSource dataSource;
    private String username;

    private String password;

    private String schema;

    @Override
    public DatabaseDataSourceConnection getObject() throws Exception {
        return new DatabaseDataSourceConnection(
                this.dataSource, this.schema, this.username, this.password);
    }

    @Override
    public Class<?> getObjectType() {
        return DatabaseDataSourceConnection.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
