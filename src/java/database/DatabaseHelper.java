/*
* $Id
*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/
package database;

import oracle.jdbc.pool.OracleDataSource;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 07.05.13</p>
 *
 * @version 1.0
 */
public class DatabaseHelper {
    private static final Logger logger = Logger.getRootLogger();
    private Connection connection;
    private OracleDataSource dataSource;

    public DatabaseHelper(OracleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || !connection.isValid(0)) {
            connection = dataSource.getConnection();
        }
        return connection;
    }

    public Connection establishNewConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public ResultSet executeStatementWithoutCommit(String query) throws SQLException {
        Connection con = getConnection();
        Statement st = con.createStatement();
        return st.executeQuery(query);
    }

    public int executeUpdateWithoutCommit(String query) throws SQLException {
        Statement st = null;
        int returnValue = 0;
        try {
            Connection con = getConnection();
            st = con.createStatement();
            returnValue = st.executeUpdate(query);
        } finally {
            if (st != null) {
                st.close();
            }
        }

        return returnValue;
    }

    public boolean closeConnection() throws SQLException {
        Connection conn = getConnection();
        if (conn.isValid(0)) {
            conn.close();
        }
        return !conn.isValid(0);
    }
}