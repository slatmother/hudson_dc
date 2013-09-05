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
package service;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 07.08.13</p>
 *
 * @version 1.0
 */
@RunWith(Theories.class)
public class TypeServiceTest {

    @DataPoints
    public static Object[][] testData = new Object[][]{
            {"all", 5},
            {"dql", 2},
            {"sql", 3}
    };

    @Theory
    public void testPerformModification(final Object... data) throws Exception {
//        Map<DSLContainer, Object> dcMap = new HashMap<DSLContainer, Object>();
//        dcMap.put(new DSLContainer(), "dql");
//        dcMap.put(new DSLContainer(), "dql");
//        dcMap.put(new DSLContainer(), "sql");
//        dcMap.put(new DSLContainer(), "sql");
//        dcMap.put(new DSLContainer(), "sql");
//
//        dcMap = TypeService.performModification((String) data[0], dcMap);
//        assertTrue(dcMap.size() == (Integer) data[1]);
    }
}
