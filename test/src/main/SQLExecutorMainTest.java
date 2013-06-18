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
package main;

import dao.DatabaseHelper;
import dao.DatabaseHelperFactory;
import execution.groovy.DSLManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import transaction.SQLTransactionHelper;
import util.Checker;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 14.06.13</p>
 *
 * @version 1.0
 */
public class SQLExecutorMainTest {
    private static final Logger logger = Logger.getRootLogger();

    @Test
    public void test() {
        SQLTransactionHelper helper = null;

        try {
            HashMap<String, String> sqlMap = new HashMap<String, String>();
            sqlMap.put("SQL_DC", "sql");

            DatabaseHelper dao = DatabaseHelperFactory.getProjectDAO();

            helper = SQLTransactionHelper.beginTransaction(dao.getConnection());

            boolean result = true;

            for (String dc : sqlMap.keySet()) {
                File scriptFile = new File("D:\\SVNProjects\\hudson_dc_execution\\out\\test\\hudson_dc_execution\\" +dc + ".groovy");
                Checker.checkFileExistsOrIsFile(scriptFile);

                result &= (Boolean) DSLManager.executeDCScript(dao, scriptFile);
            }

            if (result) {
                helper.okToCommit();
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                if (helper != null) {
                    helper.commitOrAbort();
                    helper.closeConnection();
                }
            } catch (SQLException sqlex) {
                logger.error(sqlex);
            }
        }
    }
}
