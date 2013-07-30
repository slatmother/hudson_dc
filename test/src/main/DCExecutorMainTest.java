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

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import database.DBHelper;
import database.DBHelperFactory;
import execution.groovy.dsl.DSLManager;
import execution.groovy.dsl.container.DSLContainer;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import transaction.NestedTx;
import transaction.SQLTx;
import util.Checker;
import util.Configuration;
import util.Utils;

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
 * <p>Date: 13.06.13</p>
 *
 * @version 1.0
 */
public class DCExecutorMainTest {
    private static final Logger logger = Logger.getRootLogger();
    private static final FilenameFilter dcFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".dc");
        }
    };

    @Before
    public void before() {

    }

    @Test
    public void test() throws DfException, SQLException {
        try {
            String user = Configuration.getConfig_properties().getProperty("documentum.user");
            String passwd = Configuration.getConfig_properties().getProperty("documentum.password");
            String docbase = Configuration.getConfig_properties().getProperty("documentum.docbase");

            IDfSession session = Utils.getSession(user, passwd, docbase);
            NestedTx tx = NestedTx.beginTx(session);

            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            SQLTx sqlTxHelper = SQLTx.beginTransaction(dbHelper.getConnection());

            boolean result = true;

            try {
                DSLManager dslManager = new DSLManager();
                Map<DSLContainer, Object> dcContainerMap = new LinkedHashMap<DSLContainer, Object>();

                File dqlDir = new File("./dc/dql");
                logger.info("Current dir path is " + dqlDir.getAbsolutePath());
                if (dqlDir.isDirectory()) {
                    for (File scriptFile : dqlDir.listFiles(dcFilter)) {
                        logger.info(scriptFile.getName());

                        Checker.checkFileExistsOrIsFile(scriptFile);
                        dcContainerMap.put((DSLContainer) dslManager.getDCMappingInst(scriptFile), session);
                    }
                }

                File sqlDir = new File("./dc/sql");
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
                    tx.okToCommit();
                    sqlTxHelper.okToCommit();
                }
//                else {
//                    for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
//                        dslManager.rollback(entry.getKey());
//                    }
//                }
            } finally {
                tx.commitOrAbort();
                sqlTxHelper.commitOrAbort();
            }
        } catch (DfException dfe) {
            logger.error(dfe);
            throw dfe;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }
}
