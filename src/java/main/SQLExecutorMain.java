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
import locator.BuildDCLocator;
import org.apache.log4j.Logger;
import transaction.SQLTransactionHelper;
import util.Checker;
import util.Configuration;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;

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

    public static void main(String[] args) {
        BuildDCLocator dcLocator = new BuildDCLocator();
        SQLTransactionHelper helper = null;

        try {
            String projectName = Configuration.getConfig_properties().getProperty("cq.project.name");

            HashMap<String, String> sqlMap = dcLocator.getLastBuildSQLDefChanges(projectName);
            DatabaseHelper dao = DatabaseHelperFactory.getProjectDAO();

            helper = SQLTransactionHelper.beginTransaction(dao.getConnection());
            boolean result = true;

            for (String dc : sqlMap.keySet()) {
                File scriptFile = new File(dc + ".groovy");
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
