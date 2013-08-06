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
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
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
public class DCExecutorMainTest {
    private static final Logger logger = Logger.getRootLogger();
    private static final FilenameFilter dcFilter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
            return name.endsWith(".dc");
        }
    };
    private HashMap<String, String> dcMap = new HashMap<String, String>();

    @Before
    public void before() {
        dcMap.put("TEST_DQL_1", "dql");
        dcMap.put("TEST_DQL_2", "dql");
        dcMap.put("TEST_SQL_1", "sql");
        dcMap.put("TEST_SQL_2", "sql");
    }

    @Test
    public void testMain() throws DfException, SQLException {
//        try {
//            IDfSession session = Utils.getSessionFromConfig();
//            JdbcTemplate dbHelper = JdbcTemplateFactory.getProjectDBTemplate();
//
//            NestedTx dqlTx = NestedTx.beginTx(session);
//
//            boolean result = true;
//            Map<DSLContainer, Object> dcContainerMap = new LinkedHashMap<DSLContainer, Object>();
//
//            try {
//                for (String dc : dcMap.keySet()) {
//                    String headline = dcMap.get(dc);
//
//                    File scriptFile = new File(dc + ".dc");
//                    logger.info(scriptFile.getName());
//
//                    Checker.checkFileExistsOrIsFile(scriptFile);
//                    if (Utils.isNotNull(headline) && headline.contains("dql")) {
//                        dcContainerMap.put((DSLContainer) DSLManager.getDCMappingInst(scriptFile), session);
//
//                    } else if (Utils.isNotNull(headline) && headline.contains("sql")) {
//                        dcContainerMap.put((DSLContainer) DSLManager.getDCMappingInst(scriptFile), dbHelper);
//                    }
//                }
//
//                for (Map.Entry<DSLContainer, Object> entry : dcContainerMap.entrySet()) {
//                    result &= (Boolean) DSLManager.executeDC(entry.getValue(), entry.getKey());
//                }
//
//                logger.info("Total execution result is " + result);
//                if (result) {
//                    dqlTx.okToCommit();
//                }
//            } finally {
//                dqlTx.commitOrAbort();
//            }
//        } catch (DfException dfe) {
//            logger.error(dfe);
//            throw dfe;
//        } catch (SQLException e) {
//            logger.error(e);
//            throw e;
//        }
    }
}
