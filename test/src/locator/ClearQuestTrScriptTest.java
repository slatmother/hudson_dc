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

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 08.05.13</p>
 *
 * @version 1.0
 */
//TODO: исправить этот тест. Gradle не понимает русские символы
public class ClearQuestTrScriptTest {
    private static final Logger logger = Logger.getRootLogger();
    private String projectName;


    @Before
    public void setUp() throws Exception {
        projectName = "НН:Архив ДБУиО Documentum";
    }

    @Test
    public void test() {
//        ClearQuestTrScript locator = new ClearQuestTrScript();
//        try {
//            HashMap<String, String> dcMap = locator.getAllBuildDefChanges(projectName);
//            int size = dcMap.size();
//
//            logger.info(dcMap.keySet());
//            logger.info(dcMap.values());
//
//            assumeTrue("Map size " + size, size > 0);
//        } catch (Exception e) {
//            logger.error(e);
//            assertTrue(false);
//        }
    }
}
