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
package execution.groovy;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import execution.groovy.GroovyDSLExecutor;
import org.apache.log4j.Logger;
import org.junit.Test;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 11.06.13</p>
 *
 * @version 1.0
 */
public class GroovyDSLExecutorTest {
    public static final Logger log = Logger.getRootLogger();

    public static final String TEST_QUERY_BEFORE = "";
    public static final String TEST_QUERY_DC = "";
    public static final String TEST_QUERY_AFTER = "";

    private Map<String, Object> beforeMap = new HashMap<String, Object>();
    private Map<String, Object> dcMap = new HashMap<String, Object>();
    private Map<String, Object> afterMap = new HashMap<String, Object>();


    @Test
    public void testDQLBefore() {
        try {
            IDfSession session = Utils.getSession("dmowner", "Dctm6", "dbuio");
            assertTrue(session.isConnected());

            GroovyDSLExecutor executor = new GroovyDSLExecutor(session);

            beforeMap.put("dmowner", 16);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            mapList.add(beforeMap);

            Boolean result = (Boolean) executor.executeDQL(TEST_QUERY_BEFORE, mapList);
            assertTrue(result);

        } catch (DfException e) {
            log.error("Error run test.", e);
        }
    }

    @Test
    public void testDQLDC() {
        try {
            IDfSession session = Utils.getSession("dmowner", "Dctm6", "dbuio");
            assertTrue(session.isConnected());

            GroovyDSLExecutor executor = new GroovyDSLExecutor(session);

            dcMap.put("dmowner", 16);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            mapList.add(dcMap);

            Boolean result = (Boolean) executor.executeDQL(TEST_QUERY_DC, mapList);
            assertTrue(result);

        } catch (DfException e) {
            log.error("Error run test.", e);
        }
    }

    @Test
    public void testDQLAfter() {
        try {
            IDfSession session = Utils.getSession("dmowner", "Dctm6", "dbuio");
            assertTrue(session.isConnected());

            GroovyDSLExecutor executor = new GroovyDSLExecutor(session);

            afterMap.put("dmowner", 16);

            List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            mapList.add(afterMap);

            Boolean result = (Boolean) executor.executeDQL(TEST_QUERY_AFTER, mapList);
            assertTrue(result);

        } catch (DfException e) {
            log.error("Error run test.", e);
        }
    }
}
