package com.dt.autumn.utils.databaseUtils;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.dt.autumn.reporting.assertions.CustomAssert;
import com.dt.autumn.reporting.extentReport.Logger;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.MapListHandler;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The Class to manage the database related functionality like managing the database connection,
 * Executing the specified SQL queries etc.
 */
public class SQLDBUtil {

    private static SQLDBUtil sqldbUtil;
    private static ConcurrentMap<String, Connection> connectionMap = new ConcurrentHashMap<>();
    private Connection connection;
    private Statement statement;

    private SQLDBUtil() {
    }

    /**
     * Class constructor creates a new object of SQLDBUtil if
     * that object already doesn't exist, otherwise returns the existing object.
     *
     * @return The synchronized object of SQLDBUtil class.
     */
    public static synchronized SQLDBUtil getInstance() {
        if (sqldbUtil == null) {
            synchronized (SQLDBUtil.class) {
                if (sqldbUtil == null) {
                    sqldbUtil = new SQLDBUtil();
                }
            }
        }
        return sqldbUtil;
    }

    /**
     * Creates the database connection with the database specified in
     * the connection URL.
     *
     * @param dbConnectionURL The string URL specifying the database details.
     * @return The object of database connection.
     */
    public synchronized Connection getConnection(String dbConnectionURL) {
        if (connectionMap.containsKey(dbConnectionURL)) {
            synchronized (SQLDBUtil.class) {
                if (connectionMap.containsKey(dbConnectionURL)) {
                    Connection connection = connectionMap.get(dbConnectionURL);

                    try {
                        if (connection.isValid(30)) {
                            return connection;
                        } else {
                            connectionMap.get(dbConnectionURL).close();
                            connectionMap.remove(dbConnectionURL);
                        }
                    } catch (SQLException e) {
                        try {
                            connectionMap.get(dbConnectionURL).close();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            }
        }

        Connection connection = null;
        String[] arr = dbConnectionURL.split(":");
        String dbDriver = arr[1];

        switch (dbDriver.toUpperCase()) {
            case "MYSQL":
                DbUtils.loadDriver("com.mysql.jdbc.Driver");
                break;
            case "ORACLE":
                DbUtils.loadDriver("oracle.jdbc.driver.OracleDriver");
                break;
            case "MICROSOFT":
                DbUtils.loadDriver("com.microsoft.jdbc.sqlserver.SQLServerDriver");
                break;
            default:
                throw new RuntimeException("Incorrect database driver: " + dbDriver);
        }
        try {
            Properties properties = new Properties();
            properties.put("connectTimeout", "10000");
            connection = DriverManager.getConnection(dbConnectionURL, properties);
            connectionMap.put(dbConnectionURL, connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the existing database connection from the database specified in
     * the database connection URL.
     *
     * @param dbConnectionURL The string URL specifying the database details.
     */
    public synchronized void closeConnection(String dbConnectionURL) {
        if (connectionMap.containsKey(dbConnectionURL)) {
            Connection connection = connectionMap.get(dbConnectionURL);
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectionMap.remove(dbConnectionURL);
            }
        }
    }

    /**
     * Closes all the currently opened database connections.
     */
    public synchronized void closeAllConnections() {
        Set<String> keys = connectionMap.keySet();
        for (String key : keys) {
            Connection connection = connectionMap.get(key);
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                connectionMap.remove(key);
            }
        }
    }

    /**
     * Executes the specified sql select query with respect the database provided in the dbConnectionURL.
     *
     * @param dbConnectionURL The string URL specifying the database details.
     * @param sqlQuery        The SQL select query to execute.
     * @return The resultset returned after execution of the specified SQL query.
     */
    public synchronized List<Map<String, Object>> executeSelectQuery(String dbConnectionURL, String sqlQuery) {
        Logger.logInfoInLogger("sqlQuery is " + sqlQuery);
        ResultSet resultSet;
        try {
            connection = getConnection(dbConnectionURL);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlQuery);
            MapListHandler mapListHandler = new MapListHandler();
            List<Map<String, Object>> result = mapListHandler.handle(resultSet);
            Logger.logInfoInLogger("Result output size is :- "+result.size());
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(statement);
        }
    }

    /**
     * Executes the specified sql update query with respect the database provided in the dbConnectionURL.
     *
     * @param dbConnectionURL The string URL specifying the database details.
     * @param sqlQuery        The SQL update query to execute.
     */
    public synchronized void executeUpdateQuery(String dbConnectionURL, String sqlQuery) {
        Logger.logInfoInLogger("Sql Query is " + sqlQuery);
        try {
            connection = getConnection(dbConnectionURL);
            statement = connection.createStatement();
            statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DbUtils.closeQuietly(statement);
        }
    }

    public List<Map<String, Object>> isEntryPresent(String dbConnectionURL, String sqlQuery) {
        List<Map<String, Object>> result = executeSelectQuery(dbConnectionURL, sqlQuery);
        CustomAssert.assertEquals(result.size() > 0, true, "No entry found in DB");
        return result;
    }

    public void isEntryAbsent(String dbConnectionURL, String sqlQuery) {
        List<Map<String, Object>> result = executeSelectQuery(dbConnectionURL, sqlQuery);
        CustomAssert.assertEquals(result.size() == 0, true, "Entry found in DB");
    }


}
