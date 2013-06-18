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

import dao.DatabaseHelper;
import dao.DatabaseHelperFactory;
import junit.framework.TestCase;
import org.apache.log4j.Logger;

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
public class SQLTransactionHelperTest extends TestCase {
    private static final Logger logger = Logger.getRootLogger();

    public static final String INSERT_QUERY = "insert into test_table values(2, 'test')";
    public static final String CHECK_QUERY = "select count(*) as c from test_table";

    public void test() {
        SQLTransactionHelper helper = null;
        try {
            DatabaseHelper project_dao = DatabaseHelperFactory.getProjectDAO();
            Connection connection = project_dao.getConnection();

            assertTrue(connection.getMetaData().supportsSavepoints());

            helper = SQLTransactionHelper.beginTransaction(connection);

            project_dao.executeUpdateWithoutCommit(INSERT_QUERY);

            helper.okToCommit();
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        } finally {
            assertNotNull(helper);
            try {
                helper.commitOrAbort();

                boolean isClosed = helper.closeConnection();
                assertTrue(isClosed);
            } catch (SQLException e) {
                logger.error(e);
                e.printStackTrace();
            }
        }
    }
}
