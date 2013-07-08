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

import database.DBHelper;
import database.DBHelperFactory;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.05.13</p>
 *
 * @version 1.0
 */
public class SQLTxTest {
    private static final Logger logger = Logger.getRootLogger();

    public static final String INSERT_QUERY = "insert into test_table values(2, 'test')";
    public static final String INSERT_QUERY_WRONG = "insert into test_table values(2, 'test', 5)";

    public static final String CHECK_QUERY = "select test_id from test_table where test_id = '2' and test_name = 'test'";
    public static final String DELETE_QUERY = "delete from test_table where test_id = '2'";

    @Test
    public void testSuccessCommit() {
        SQLTx txhelper1 = null;
        try {
            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            Connection connection = dbHelper.getConnection();

            assertTrue(connection.getMetaData().supportsSavepoints());

            txhelper1 = SQLTx.beginTransaction(connection);

            dbHelper.executeUpdateWithoutCommit(INSERT_QUERY);

            txhelper1.okToCommit();
        } catch (SQLException e) {
            logger.error(e);
            assertTrue(false);
        } finally {
            assertNotNull(txhelper1);
            try {
                txhelper1.commitOrAbort();

                boolean isClosed = txhelper1.closeConnection();
                assertTrue(isClosed);
            } catch (SQLException e) {
                logger.error(e);
                assertTrue(false);
            }
        }

        SQLTx txhelper2 = null;
        try {
            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            Connection connection = dbHelper.getConnection();

            assertTrue(connection.getMetaData().supportsSavepoints());

            txhelper2 = SQLTx.beginTransaction(connection);

            ResultSet set = dbHelper.executeStatementWithoutCommit(CHECK_QUERY);
            while (set.next()) {
                assertTrue(Integer.valueOf(1).equals(set.getRow()));
                assertTrue(Integer.toString(2).equals(set.getObject("TEST_ID").toString()));
            }

            dbHelper.executeUpdateWithoutCommit(DELETE_QUERY);

            txhelper2.okToCommit();
        } catch (SQLException e) {
            logger.error(e);
            assertTrue(false);
        } finally {
            assertNotNull(txhelper2);
            try {
                txhelper2.commitOrAbort();

                boolean isClosed = txhelper2.closeConnection();
                assertTrue(isClosed);
            } catch (SQLException e) {
                logger.error(e);
                assertTrue(false);
            }
        }

    }

    @Test
    public void testFailedCommit() {
        SQLTx txhelper1 = null;
        try {
            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            Connection connection = dbHelper.getConnection();

            assertTrue(connection.getMetaData().supportsSavepoints());

            txhelper1 = SQLTx.beginTransaction(connection);

            dbHelper.executeUpdateWithoutCommit(INSERT_QUERY_WRONG);

            txhelper1.okToCommit();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            assertNotNull(txhelper1);
            try {
                txhelper1.commitOrAbort();

                boolean isClosed = txhelper1.closeConnection();
                assertTrue(isClosed);
            } catch (SQLException e) {
                logger.error(e);
                assertTrue(false);
            }
        }

        SQLTx txhelper2 = null;
        try {
            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            Connection connection = dbHelper.getConnection();

            assertTrue(connection.getMetaData().supportsSavepoints());

            txhelper2 = SQLTx.beginTransaction(connection);

            ResultSet set = dbHelper.executeStatementWithoutCommit(CHECK_QUERY);
            while (set.next()) {
                assertTrue(Integer.valueOf(0).equals(set.getRow()));
            }

            txhelper2.okToCommit();
        } catch (SQLException e) {
            logger.error(e);
            assertTrue(false);
        } finally {
            assertNotNull(txhelper2);
            try {
                txhelper2.commitOrAbort();

                boolean isClosed = txhelper2.closeConnection();
                assertTrue(isClosed);
            } catch (SQLException e) {
                logger.error(e);
                assertTrue(false);
            }
        }

    }
}
