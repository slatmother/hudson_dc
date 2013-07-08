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

import com.documentum.fc.common.DfException;
import database.DBHelper;
import database.DBHelperFactory;
import execution.groovy.dsl.DSLManager;
import execution.groovy.dsl.container.DSLContainer;
import org.apache.log4j.Logger;
import org.junit.Test;
import transaction.SQLTx;
import util.Checker;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 27.06.13</p>
 *
 * @version 1.0
 */
public class SQLExecutorMainTest {
    private static final Logger logger = Logger.getRootLogger();
    private static final FilenameFilter dcFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".dc");
        }
    };

    @Test
    public void test() throws DfException, SQLException {
        try {
            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            SQLTx sqlTxHelper = SQLTx.beginTransaction(dbHelper.getConnection());

            boolean result = true;

            try {
                DSLManager dslManager = new DSLManager();
                List<DSLContainer> dcMappingList = new ArrayList<DSLContainer>();

                File sqlDir = new File("./dc/sql");
                logger.info("Current dir path is " + sqlDir.getAbsolutePath());
                if (sqlDir.isDirectory()) {
                    for (File scriptFile : sqlDir.listFiles(dcFilter)) {
                        logger.info(scriptFile.getName());

                        Checker.checkFileExistsOrIsFile(scriptFile);
                        dcMappingList.add((DSLContainer) dslManager.getDCMappingInst(scriptFile));
                    }
                }

                for (DSLContainer container : dcMappingList) {
                    result &= (Boolean) dslManager.executeDC(dbHelper, container);
                }

                if (result) {
                    sqlTxHelper.okToCommit();
                } else {
                    for (DSLContainer container : dcMappingList) {
                        dslManager.rollback(container);
                    }
                }
            } finally {
                sqlTxHelper.commitOrAbort();
                sqlTxHelper.closeConnection();
            }
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }
}
