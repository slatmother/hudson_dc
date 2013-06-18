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
import core.TestConfiguration;
import org.apache.log4j.Logger;
import org.junit.Test;
import util.Utils;

import java.io.File;
import java.util.Properties;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
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
public class DSLManagerTest {
    public static final Logger log = Logger.getRootLogger();
    public static final String TEST_DC_PROPERTY_KEY = "dc.file.path";

    @Test
    public void test() {
        try {
            TestConfiguration configuration = new TestConfiguration();
            Properties testProp = configuration.readConfig("test.properties");
            assertFalse(testProp.isEmpty());

            String testDCPath = testProp.getProperty(TEST_DC_PROPERTY_KEY);
            assertNotNull(testDCPath);

            File scriptFile = new File(testDCPath);
            assertTrue(scriptFile.exists());
            assertTrue(scriptFile.isFile());

            IDfSession session = Utils.getSession("dmowner", "Dctm6", "dbuio");
            assertTrue(session.isConnected());

            boolean result = (Boolean) DSLManager.executeDCScript(session, scriptFile);
            assertTrue(result);

        } catch (Throwable e) {
            e.printStackTrace();
            log.error("Error run test.", e);
        }
    }
}
