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
package constants;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 28.06.13</p>
 *
 * @version 1.0
 */
public interface IConstants {

    interface QueryTypes {
        public static final String VIEW = "view";
    }

    interface AffectedTypes {
        public static final int UNDEFINED = 0;
        public static final int INFINITY_RANGE = 1;
        public static final int VALUE = 2;
        public static final int RANGE = 3;
    }

    interface DQLQueryReturnVals {
        public static final String CREATED = "object_created";
        public static final String UPDATED = "objects_updated";
        public static final String DELETED = "objects_deleted";
    }

    interface SQLRegexp {
        public static final String CREATE_OR_REPLACE_REGEXP = "(CREATE OR REPLACE (FORCE VIEW|VIEW))";

        public static final String VIEW_REGEXP = "(CREATE OR REPLACE (FORCE VIEW|VIEW))";
        public static final String TRIGGER_REGEXP = "(CREATE OR REPLACE (FORCE TRIGGER|TRIGGER)))";
        public static final String PROCEDURE_REGEXP = "(CREATE OR REPLACE (FORCE PROCEDURE|PROCEDURE)))";
        public static final String FUNCTION_REGEXP = "(CREATE OR REPLACE (FORCE FUNCTION|FUNCTION)))";
    }
}
