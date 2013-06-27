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
package sql;

import database.DatabaseHelper;
import database.DatabaseHelperFactory;
import org.apache.log4j.Logger;
import org.junit.Test;
import transaction.SQLTransactionHelper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 24.06.13</p>
 *
 * @version 1.0
 */
public class SQLTest {
    private static final Logger logger = Logger.getRootLogger();

    @Test
    public void test() throws SQLException {
        DatabaseHelper dbHelper = DatabaseHelperFactory.getClearQuestHelper();
        SQLTransactionHelper sqlTxHelper = SQLTransactionHelper.beginTransaction(dbHelper.getConnection());

        try {
            ResultSet set = dbHelper.executeStatementWithoutCommit("select count(*) as c from dc");
            ResultSetMetaData metaData = set.getMetaData();
            logger.info("Columns " + metaData.getColumnCount());
            assertTrue(1 == metaData.getColumnCount());

            while (set.next()) {
                logger.info("Row " + set.getRow());
                assertTrue(1 == set.getRow());
                logger.info("Count is " + set.getObject(1).toString());
                assertTrue(Integer.valueOf(set.getObject(1).toString()) > 1000);
            }

            sqlTxHelper.okToCommit();
        } finally {
            sqlTxHelper.commitOrAbort();
            sqlTxHelper.closeConnection();
        }
    }
}
