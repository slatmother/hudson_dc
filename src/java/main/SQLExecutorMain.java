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

import database.DatabaseHelper;
import database.DatabaseHelperFactory;
import execution.groovy.DSLManager;
import org.apache.log4j.Logger;
import transaction.SQLTransactionHelper;
import util.Checker;

import java.io.File;
import java.io.FilenameFilter;
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
public class SQLExecutorMain {
    private static final Logger logger = Logger.getRootLogger();
    private static final FilenameFilter dcFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".groovy");
        }
    };

    public static void main(String[] args) {
        SQLTransactionHelper txHelper = null;

        try {
            DatabaseHelper dbHelper = DatabaseHelperFactory.getCustomProjectHelper();

            txHelper = SQLTransactionHelper.beginTransaction(dbHelper.getConnection());
            boolean result = true;

            DSLManager dslManager = new DSLManager();
            File sqlDir = new File("sql");
            logger.info("Current dir path is " + sqlDir.getAbsolutePath());
            if (sqlDir.isDirectory()) {
                for (File scriptFile : sqlDir.listFiles(dcFilter)) {
                    logger.info(scriptFile.getName());

                    Checker.checkFileExistsOrIsFile(scriptFile);

//                    result &= (Boolean) dslManager.executeDCScript(dbHelper, scriptFile);
                }
            }

            if (result) {
                txHelper.okToCommit();
            }
        } catch (Exception ex) {
            logger.error(ex);
        } finally {
            try {
                if (txHelper != null) {
                    txHelper.commitOrAbort();
                    txHelper.closeConnection();
                }
            } catch (SQLException sqlex) {
                logger.error(sqlex);
            }
        }
    }
}
