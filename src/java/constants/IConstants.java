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

    interface MainArgsTypes {
        public static final String[] DEFAULT_ARGS = {"cq", "all", "run"};

        interface Location {
            public static final String CQ = "cq";
            public static final String CUSTOM = "custom";
        }

        interface Type {
            public static final String DQL = "dql";
            public static final String SQL = "sql";
            public static final String ALL = "all";
        }

        interface Operation {
            public static final String RUN = "run";
            public static final String VALIDATE = "validate";
        }
    }
}
