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

import database.DBHelper;
import database.DBHelperFactory;
import execution.groovy.dsl.DSLManager;
import execution.groovy.dsl.container.DSLContainer;
import org.apache.log4j.Logger;
import transaction.SQLTx;
import util.Checker;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

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
            return name.endsWith(".dc");
        }
    };

    public static void main(String[] args) throws Exception {
        SQLTx txHelper = null;

        try {
            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            txHelper = SQLTx.beginTransaction(dbHelper.getConnection());
            boolean result = true;

            DSLManager dslManager = new DSLManager();
            Map<DSLContainer, Object> dcContainerMap = new LinkedHashMap<DSLContainer, Object>();

            File sqlDir = new File("./DC/SQL");
            logger.info("Current dir path is " + sqlDir.getAbsolutePath());
            if (sqlDir.isDirectory()) {
                for (File scriptFile : sqlDir.listFiles(dcFilter)) {
                    logger.info(scriptFile.getName());

                    Checker.checkFileExistsOrIsFile(scriptFile);
                    dcContainerMap.put((DSLContainer) dslManager.getDCMappingInst(scriptFile), dbHelper);
                }
            }

            for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
                result &= (Boolean) dslManager.executeDC(entry.getValue(), entry.getKey());
            }

            logger.info("Total execution result is " + result);
            if (result) {
                txHelper.okToCommit();
            } else {
//                for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
//                    dslManager.rollback(entry.getKey());
//                }
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        } finally {
            try {
                if (txHelper != null) {
                    txHelper.commitOrAbort();
                }
            } catch (SQLException sqlex) {
                logger.error("Connection close block execution failed. ", sqlex);
            }
        }
    }
}
