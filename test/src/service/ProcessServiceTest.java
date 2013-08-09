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

import execution.groovy.dsl.container.DSLContainer;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

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
public class ProcessServiceTest {
    private static final Map<DSLContainer, Object> map = new LinkedHashMap<DSLContainer, Object>();


    @DataPoints
    public static Object[][] testData = new Object[][]{
//            {"run"},
            {"validate"},
    };

    @Before
    public void setUp() throws Exception {
        LocationService.performModification("custom", map);
    }

    @Theory
    public void testExecute(final Object... data) throws Exception {
        boolean result = ProcessService.execute((String) data[0], map);
        assertTrue(result);
    }
}
