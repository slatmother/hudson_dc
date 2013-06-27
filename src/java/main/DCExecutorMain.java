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
import locator.BuildDCLocator;
import org.apache.log4j.Logger;
import transaction.NestedTx;
import transaction.SQLTransactionHelper;
import util.Checker;
import util.Configuration;
import util.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.06.13</p>
 *
 * @version 1.0
 */
public class DCExecutorMain {
    private static final Logger logger = Logger.getRootLogger();

    public static void main(String[] args) {
        BuildDCLocator dcLocator = new BuildDCLocator();

        try {
            String projectName = Configuration.getConfig_properties().getProperty("cq.project.name");

            String user = Configuration.getConfig_properties().getProperty("documentum.user");
            String passwd = Configuration.getConfig_properties().getProperty("documentum.password");
            String docbase = Configuration.getConfig_properties().getProperty("documentum.docbase");

            IDfSession session = Utils.getSession(user, passwd, docbase);
            NestedTx dqlTx = NestedTx.beginTx(session);

            DatabaseHelper dbHelper = DatabaseHelperFactory.getCustomProjectHelper();
            SQLTransactionHelper sqlTxHelper = SQLTransactionHelper.beginTransaction(dbHelper.getConnection());

            boolean result = true;

            try {
                HashMap<String, String> dcMap = dcLocator.getAllBuildDefChanges(projectName);
                DSLManager dslManager = new DSLManager();

                for (String dc : dcMap.keySet()) {
                    String headline = dcMap.get(dc);

                    File scriptFile = new File(dc + ".groovy");
                    Checker.checkFileExistsOrIsFile(scriptFile);

                    if (Utils.isNotNull(headline) && headline.contains("dql")) {
//                        result &= (Boolean) dslManager.executeDCScript(session, scriptFile);

                    } else if (Utils.isNotNull(headline) && headline.contains("sql")) {
//                        result &= (Boolean) dslManager.executeDCScript(dbHelper, scriptFile);
                    }
                }

                if (result) {
                    dqlTx.okToCommit();
                    sqlTxHelper.okToCommit();
                }
            } catch (Exception ex) {
                logger.error(ex);
            } finally {
                dqlTx.commitOrAbort();

                sqlTxHelper.commitOrAbort();
                sqlTxHelper.closeConnection();
            }
        } catch (DfException dfe) {
            logger.error(dfe);
        } catch (SQLException e) {
            logger.error(e);
        }
    }
}
