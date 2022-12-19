package com.inov8.microbank.common.util;

import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.inov8.microbank.disbursement.util.BatchUtil;

public class DBManager {
    public static Connection getConnection() throws Exception {
        Connection connection;
        Properties prop = new Properties();

        prop.put("user", BatchUtil.getDbUsername());
        prop.put("password", BatchUtil.getDbPassword());
        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        OracleDataSource ods = new OracleDataSource();
        ods.setConnectionProperties(prop);
        ods.setURL(BatchUtil.getDbURL());

        connection = ods.getConnection();
        if (connection != null) {
            connection.setAutoCommit(false);
        }

        return connection;
    }
     
/*     public static Connection getDSConnection() throws Exception {
         String datasource = ConfigurationProperties.DATASOURCE;
         InitialContext ic = new InitialContext();
         DataSource ds = (DataSource)ic.lookup("jdbc/"+datasource);
         Connection connection = ds.getConnection();
         connection.setAutoCommit(false);
         return connection;
     }     */
}

