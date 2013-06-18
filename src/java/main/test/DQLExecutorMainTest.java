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
package main.test;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import execution.groovy.DSLManager;
import org.apache.log4j.Logger;
import transaction.NestedTx;
import util.Checker;
import util.Configuration;
import util.Utils;

import java.io.File;
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
public class DQLExecutorMainTest {
    private static final Logger logger = Logger.getRootLogger();

    public static void main(String[] args) {
        try {
            String user = Configuration.getConfig_properties().getProperty("documentum.user");
            String passwd = Configuration.getConfig_properties().getProperty("documentum.password");
            String docbase = Configuration.getConfig_properties().getProperty("documentum.docbase");

            IDfSession session = Utils.getSession(user, passwd, docbase);
            NestedTx tx = NestedTx.beginTx(session);

            boolean result = true;

            try {
                HashMap<String, String> dqlMap = new HashMap<String, String>();
                dqlMap.put("DQL_DC", "dql");

                for (String dc : dqlMap.keySet()) {
                    File scriptFile = new File("D:\\SVNProjects\\hudson_dc_execution\\out\\test\\hudson_dc_execution\\" + dc + ".groovy");

//                    File scriptFile = new File(dc + ".groovy");
                    Checker.checkFileExistsOrIsFile(scriptFile);

                    result &= (Boolean) DSLManager.executeDCScript(session, scriptFile);
                }

                if (result) {
                    tx.okToCommit();
                }
            } catch (Exception ex) {
                logger.error(ex);
            } finally {
                tx.commitOrAbort();
            }

        } catch (DfException dfe) {
            dfe.printStackTrace();
        }
    }
}
