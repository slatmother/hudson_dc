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

import groovy.container.DSLContainer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

public class ProcessServiceTest {

    @BeforeMethod
    public void setUp() throws Exception {


    }

    @Test
    public void testName() throws Exception {


    }


    private static final Map<DSLContainer, Object> map = new LinkedHashMap<DSLContainer, Object>();

    public static Object[][] testData = new Object[][]{
//            {"run"},
            {"validate"},
    };


    public void testExecute(final Object... data) throws Exception {
        boolean result = ProcessService.execute((String) data[0], map);
        assertTrue(result);
    }
}
