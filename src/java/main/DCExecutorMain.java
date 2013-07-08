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
import locator.BuildDCLocator;
import org.apache.log4j.Logger;
import transaction.NestedTx;
import transaction.SQLTx;
import util.Checker;
import util.Configuration;
import util.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
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
public class DCExecutorMain {
    private static final Logger logger = Logger.getRootLogger();

    public static void main(String[] args) throws DfException, SQLException {
        BuildDCLocator dcLocator = new BuildDCLocator();

        try {
            String projectName = Configuration.getConfig_properties().getProperty("cq.project.name");

            String user = Configuration.getConfig_properties().getProperty("documentum.user");
            String passwd = Configuration.getConfig_properties().getProperty("documentum.password");
            String docbase = Configuration.getConfig_properties().getProperty("documentum.docbase");

            IDfSession session = Utils.getSession(user, passwd, docbase);
            NestedTx dqlTx = NestedTx.beginTx(session);

            DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
            SQLTx sqlTxHelper = SQLTx.beginTransaction(dbHelper.getConnection());

            boolean result = true;
            Map<DSLContainer, Object> dcContainerMap = new LinkedHashMap<DSLContainer, Object>();

            try {
                HashMap<String, String> dcMap = dcLocator.getAllBuildDefChanges(projectName);
                DSLManager dslManager = new DSLManager();

                for (String dc : dcMap.keySet()) {
                    String headline = dcMap.get(dc);

                    File scriptFile = new File(dc + ".dc");
                    logger.info(scriptFile.getName());

                    Checker.checkFileExistsOrIsFile(scriptFile);
                    if (Utils.isNotNull(headline) && headline.contains("dql")) {
                        dcContainerMap.put((DSLContainer) dslManager.getDCMappingInst(scriptFile), session);

                    } else if (Utils.isNotNull(headline) && headline.contains("sql")) {
                        dcContainerMap.put((DSLContainer) dslManager.getDCMappingInst(scriptFile), dbHelper);
                    }
                }

                for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
                    result &= (Boolean) dslManager.executeDC(entry.getValue(), entry.getKey());
                }

                logger.info("Total execution result is " + result);
                if (result) {
                    dqlTx.okToCommit();
                    sqlTxHelper.okToCommit();
                } else {
                    for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
                        dslManager.rollback(entry.getKey());
                    }
                }
            } finally {
                dqlTx.commitOrAbort();
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
