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
import execution.groovy.dsl.DSLManager;
import execution.groovy.dsl.container.DSLContainer;
import org.apache.log4j.Logger;
import transaction.NestedTx;
import util.Checker;
import util.Configuration;
import util.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 07.05.13</p>
 *
 * @version 1.0
 */
public class DQLExecutorMain {
    private static final Logger logger = Logger.getRootLogger();
    private static final FilenameFilter dcFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".dc");
        }
    };

    public static void main(String[] args) throws Exception {
        try {
            String user = Configuration.getConfig_properties().getProperty("documentum.user");
            String passwd = Configuration.getConfig_properties().getProperty("documentum.password");
            String docbase = Configuration.getConfig_properties().getProperty("documentum.docbase");

            IDfSession session = Utils.getSession(user, passwd, docbase);
            NestedTx tx = NestedTx.beginTx(session);

            boolean result = true;

            try {
                DSLManager dslManager = new DSLManager();
                Map<DSLContainer, Object> dcContainerMap = new LinkedHashMap<DSLContainer, Object>();

                File dqlDir = new File("./DC/DQL");
                logger.info("Current dir path is " + dqlDir.getAbsolutePath());
                if (dqlDir.isDirectory()) {
                    for (File scriptFile : dqlDir.listFiles(dcFilter)) {
                        logger.info(scriptFile.getName());

                        Checker.checkFileExistsOrIsFile(scriptFile);
                        dcContainerMap.put((DSLContainer) dslManager.getDCMappingInst(scriptFile), session);
                    }
                }

                for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
                    result &= (Boolean) dslManager.executeDC(entry.getValue(), entry.getKey());
                }

                logger.info("Total execution result is " + result);
                if (result) {
                    tx.okToCommit();
                } else {
                    for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
                        dslManager.rollback(entry.getKey());
                    }
                }
            } finally {
                tx.commitOrAbort();
            }
        } catch (Exception ex) {
            logger.error(ex);
            throw ex;
        }
    }
}
