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
package execution;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import org.apache.log4j.Logger;
import org.junit.Test;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 16.05.13</p>
 *
 * @version 1.0
 */
public class DCScriptRunnerTest {
    public static final Logger log = Logger.getRootLogger();

    public static final String QUERY_TEST_1 = "select user_os_name, user_privileges from dm_user where user_name = 'dmowner'";
    public static final String QUERY_TEST_2 = "select user_os_name, user_privileges from dm_user where user_name = '" + Math.random() * 100 + "'";
    public static final String QUERY_TEST_3 = "select title, owner_name  from dm_job where title = 'Docbase' enable(return_top 2)";
    public static final String QUERY_TEST_REPEATING = "select user_os_name, user_privileges from dm_user where user_name = '" + Math.random() * 100 + "'";


    @Test
    public void preconditionScriptTest() {
        try {
            IDfSession session = Utils.getSession("dmowner", "Dctm6", "dbuio");
            assertTrue(session.isConnected());

            HashMap<String, Object> mapQuery1 = new HashMap<String, Object>();
            mapQuery1.put("user_os_name", "dmowner");
            mapQuery1.put("user_privileges", 16);

            boolean preconditionScriptResultQuery1 = DCScriptRunner.runScriptWithOneRow(session, QUERY_TEST_1, mapQuery1);
            assertTrue("Precondition script failed with query_1", preconditionScriptResultQuery1);

            boolean preconditionScriptResultQuery2 = DCScriptRunner.runScriptWithOneRow(session, QUERY_TEST_2, mapQuery1);
            assertFalse("Precondition script failed with query_2", preconditionScriptResultQuery2);

        } catch (DfException e) {
            log.error(e);
            assertTrue(false);
        }
    }

    @Test
    public void preconditionScriptWithAllRowsSelectedTest() {
        try {
            IDfSession session = Utils.getSession("dmowner", "Dctm6", "dbuio");
            assertTrue(session.isConnected());

            HashMap<String, Object> mapQuery1 = new HashMap<String, Object>();
            mapQuery1.put("title", "Docbase");
            mapQuery1.put("owner_name", "dmowner");

            HashMap<String, Object> mapQuery2 = new HashMap<String, Object>();
            mapQuery1.put("title", "Docbase");
            mapQuery1.put("owner_name", "dmowner");

            List<Map<String, Object>> testList = new ArrayList<Map<String, Object>>();
            testList.add(mapQuery1);
            testList.add(mapQuery2);

            boolean preconditionScriptResultQuery1 = DCScriptRunner.runScript(session, QUERY_TEST_3, testList);
            assertTrue("Precondition script failed with query_1", preconditionScriptResultQuery1);

        } catch (DfException e) {
            log.error(e);
            assertTrue(false);
        }
    }
}
