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
package transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.05.13</p>
 *
 * @version 1.0
 */
@Deprecated
public class SQLTx {
    private Connection connection;
    private boolean isOkToCommit = false;

    private SQLTx() {
    }

    public static SQLTx beginTransaction(Connection conn) throws SQLException {
        SQLTx helper = new SQLTx();

        if (conn != null && conn.isValid(0)) {
            helper.setConnection(conn);
            conn.setAutoCommit(false);
        } else {
            throw new IllegalArgumentException("Cannot begin transaction. Connection is null or not valid!");
        }
        return helper;
    }

    public void commitOrAbort() throws SQLException {
        if (!isOkToCommit) {
            connection.rollback();
        } else {
            connection.commit();
        }
        closeConnection();
    }

    public void okToCommit() {
        isOkToCommit = true;
    }


    protected void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean closeConnection() throws SQLException {
        if (connection.isValid(0)) {
            connection.close();
        }
        return !connection.isValid(0);
    }
}
