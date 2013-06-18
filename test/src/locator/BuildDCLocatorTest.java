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
package locator;

import locator.BuildDCLocator;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 08.05.13</p>
 *
 * @version 1.0
 */
public class BuildDCLocatorTest {
    private static final Logger logger = Logger.getRootLogger();

    @Test
    public void test() {
        BuildDCLocator locator = new BuildDCLocator();
        try {
            HashMap<String, String> dcMap = locator.getAllBuildDefChanges("НН:Архив ДБУиО Documentum");
            int size = dcMap.size();
            assertTrue(size > 0);

            logger.info(dcMap.keySet());
            logger.info(dcMap.values());
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
