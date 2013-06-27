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
import database.DatabaseHelper;
import database.DatabaseHelperFactory;
import execution.groovy.DSLManager;
import org.apache.log4j.Logger;
import org.junit.Test;
import transaction.NestedTx;
import transaction.SQLTransactionHelper;
import util.Checker;
import util.Configuration;
import util.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;

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
            return name.endsWith(".groovy");
        }
    };

    @Test
    public void test() throws DfException, SQLException {
        try {
            String user = Configuration.getConfig_properties().getProperty("documentum.user");
            String passwd = Configuration.getConfig_properties().getProperty("documentum.password");
            String docbase = Configuration.getConfig_properties().getProperty("documentum.docbase");

            IDfSession session = Utils.getSession(user, passwd, docbase);
            NestedTx tx = NestedTx.beginTx(session);

            DatabaseHelper dbHelper = DatabaseHelperFactory.getCustomProjectHelper();
            SQLTransactionHelper sqlTxHelper = SQLTransactionHelper.beginTransaction(dbHelper.getConnection());

            boolean result = true;

            try {
                DSLManager dslManager = new DSLManager();
                File dqlDir = new File("./dc/dql");
                logger.info("Current dir path is " + dqlDir.getAbsolutePath());
                if (dqlDir.isDirectory()) {
                    for (File scriptFile : dqlDir.listFiles(dcFilter)) {
                        logger.info(scriptFile.getName());

                        Checker.checkFileExistsOrIsFile(scriptFile);

//                        result &= (Boolean) dslManager.executeDCScript(session, scriptFile);
                    }
                }

                File sqlDir = new File("./dc/sql");
                logger.info("Current dir path is " + sqlDir.getAbsolutePath());
                if (sqlDir.isDirectory()) {
                    for (File scriptFile : sqlDir.listFiles(dcFilter)) {
                        logger.info(scriptFile.getName());

                        Checker.checkFileExistsOrIsFile(scriptFile);

//                        result &= (Boolean) dslManager.executeDCScript(dbHelper, scriptFile);
                    }
                }

                if (result) {
                    tx.okToCommit();
                    sqlTxHelper.okToCommit();
                }
            } finally {
                tx.commitOrAbort();
                sqlTxHelper.commitOrAbort();
                sqlTxHelper.closeConnection();
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
