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

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import constants.IConstants;
import groovy.container.DSLContainer;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 06.08.13</p>
 *
 * @version 1.0
 */
public class LocationServiceTest {
    private String location;

    @Before
    public void setUp() throws Exception {
        location = IConstants.MainArgsTypes.Location.CUSTOM;
    }

    @Test
    public void testLocation() throws Exception {
        Map<DSLContainer, Object> dcMap = new HashMap<DSLContainer, Object>();
        LocationService.performModification(location, dcMap);
        assertTrue(dcMap.size() == 5);

        Multiset items = HashMultiset.create(dcMap.values());
        assertTrue(items.count("dql") == 2);
        assertTrue(items.count("sql") == 3);
    }
}
