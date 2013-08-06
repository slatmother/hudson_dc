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
package util;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 21.05.13</p>
 *
 * @version 1.0
 */
public class UtilsTest {
    Logger logger = Logger.getRootLogger();
    public static final String TEST_QUERY_1 = "select user_os_name from dm_user where user_name = 'dmowner'";
    public static final String TEST_QUERY_2 = "select count(*) as c from dm_user where user_name = 'dmowner'";

    @Test
    public void test() {
//        IDfSession session = null;
//        IDfSessionManager manager = null;
//        try {
//            session = Utils.getSession("dmowner", "Dctm6", "dbuio");
//            assertTrue(session.isConnected());
//
//            manager = session.getSessionManager();
//
//            Map<String, Object> map = Utils.getFirstRow(session, TEST_QUERY_1);
//            assertEquals("dmowner", map.get("user_os_name"));
//        } catch (DfException e) {
//            logger.error(e);
//        } finally {
//            if (manager != null) {
//                manager.release(session);
//                assertFalse(session.isConnected());
//            }
//        }
    }

    @Test
    public void doubleIntegerReturnValueTest() {
//        IDfSession session = null;
//        IDfSessionManager manager = null;
//        try {
//            session = Utils.getSession("dmowner", "Dctm6", "dbuio");
//            assertTrue(session.isConnected());
//
//            manager = session.getSessionManager();
//
//            Map<String, Object> map = Utils.getFirstRow(session, TEST_QUERY_2);
//            assertEquals(1, map.get("c"));
//        } catch (DfException e) {
//            logger.error(e);
//        } finally {
//            if (manager != null) {
//                manager.release(session);
//                assertFalse(session.isConnected());
//            }
//        }
    }
}
