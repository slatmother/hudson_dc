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

import constants.IConstants;
import execution.groovy.dsl.container.DSLContainer;

import java.util.Map;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 06.08.13</p>
 *
 * @version 1.0
 */
public class TypeService {
    public static Map<DSLContainer, Object> performModification(String type, Map<DSLContainer, Object> dcMapping) {
        if (IConstants.MainArgsTypes.Type.ALL.equalsIgnoreCase(type)) {
            return dcMapping;
        }

        for (Map.Entry entry : dcMapping.entrySet()) {
            if (!entry.getValue().equals(type)) {
                dcMapping.remove(entry.getKey());
            }
        }

        return dcMapping;
    }
}
