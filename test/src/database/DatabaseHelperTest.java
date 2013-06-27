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

import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 08.05.13</p>
 *
 * @version 1.0
 */
public class DatabaseHelperTest {
    public static final Logger log = Logger.getRootLogger();
    public static final String TEST_QUERY = "Select 1 from dual";

    @Test
    public void test() {
        try {
            DatabaseHelper cq_dao = DatabaseHelperFactory.getClearQuestHelper();
            assertTrue(cq_dao.getConnection().isValid(10));

            DatabaseHelper project_dao = DatabaseHelperFactory.getCustomProjectHelper();
            assertTrue(project_dao.getConnection().isValid(10));

            ResultSet cq_set = cq_dao.executeStatementWithoutCommit(TEST_QUERY);
            while (cq_set.next()) {
                assertTrue("1".equals(cq_set.getString("1")));
            }
            assertTrue(cq_dao.closeConnection());

            ResultSet pr_set = project_dao.executeStatementWithoutCommit(TEST_QUERY);
            while (pr_set.next()) {
                assertTrue("1".equals(pr_set.getString("1")));
            }

            assertTrue(project_dao.closeConnection());
        } catch (SQLException e) {
            log.error(e);
            assertTrue(false);
        }
    }
}
