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

import dao.DatabaseHelper;
import dao.DatabaseHelperFactory;
import org.apache.log4j.Logger;
import org.junit.Test;
import transaction.SQLTransactionHelper;

import java.math.BigDecimal;
import java.sql.ResultSet;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.06.13</p>
 *
 * @version 1.0
 */
public class SQLTest {
    private static final Logger logger = Logger.getRootLogger();

    @Test
    public void test() {
        try {
            DatabaseHelper projectDatabaseDAO = DatabaseHelperFactory.getProjectDAO();

            SQLTransactionHelper helper = SQLTransactionHelper.beginTransaction(projectDatabaseDAO.getConnection());
            ResultSet set = projectDatabaseDAO.executeStatementWithoutCommit("select test_id from test_table");

            while(set.next()) {
                System.out.println(set.getRow() + " " + set.getObject(1));
            }

            helper.okToCommit();
            helper.commitOrAbort();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Test
    public void testDecimal() {
        Object big = new BigDecimal(24.5);
        System.out.println(big.toString());
        Object doub = 24.5;
        System.out.println(doub.toString());
        System.out.println(big.toString().equals(doub.toString()));

    }

}
