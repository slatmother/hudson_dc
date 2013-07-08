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
package parser;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeThat;


/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 08.07.13</p>
 *
 * @version 1.0
 */
public class RollbackDDLStTest {
    private static final Logger logger = Logger.getRootLogger();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testTable() throws Exception {
        {
            RollbackDDLSt st = RollbackDDLSt.construct("CREATE", "TABLE", "DDV_HISTORY");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }
        {
            RollbackDDLSt st = RollbackDDLSt.construct("ALTER", "TABLE", "DDV_HISTORY");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }
        {
            RollbackDDLSt st = RollbackDDLSt.construct("COMMENT", "TABLE", "DDV_HISTORY");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("TRUNCATE", "TABLE", "DDV_HISTORY");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("DROP", "TABLE", "DDV_HISTORY");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }
    }

    @Test
    public void testSq() throws Exception {
        {
            RollbackDDLSt st = RollbackDDLSt.construct("CREATE", "SEQUENCE", "ddseq_document_barcode");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("ALTER", "SEQUENCE", "ddseq_document_barcode");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("COMMENT", "SEQUENCE", "ddseq_document_barcode");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("TRUNCATE", "SEQUENCE", "ddseq_document_barcode");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("DROP", "SEQUENCE", "ddseq_document_barcode");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
        }
    }

    @Test
    public void testView() throws Exception {
        {
            RollbackDDLSt st = RollbackDDLSt.construct("create", "view", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), notNullValue(String.class));
            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("ALTER", "VIEW", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), notNullValue(String.class));
            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("COMMENT", "view", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
//            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("TRUNCATE", "VIEW", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
//            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("DROP", "view", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), notNullValue(String.class));
            assumeThat(st.getRollbackQuery().length(), not(0));
        }
    }

    @Test
    public void testTrigger() throws Exception {
        {
            RollbackDDLSt st = RollbackDDLSt.construct("create", "Trigger", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), notNullValue(String.class));
            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("ALTER", "TRIGGER", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), notNullValue(String.class));
            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("COMMENT", "trigger", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
            //            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("TRUNCATE", "TRIGGER", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), nullValue(String.class));
            //            assumeThat(st.getRollbackQuery().length(), not(0));
        }

        {
            RollbackDDLSt st = RollbackDDLSt.construct("DROP", "trigger", "ddv_register_transfer");
            assumeThat(st.getRollbackQuery(), notNullValue(String.class));
            assumeThat(st.getRollbackQuery().length(), not(0));
        }
    }

    @Test
    public void testProcedure() throws Exception {
        RollbackDDLSt st = RollbackDDLSt.construct("CREATE", "TABLE", "DDV_HISTORY");
        assumeThat(st.getRollbackQuery(), nullValue(String.class));
    }

    @Test
    public void testFunction() throws Exception {
        RollbackDDLSt st = RollbackDDLSt.construct("CREATE", "TABLE", "DDV_HISTORY");
        assumeThat(st.getRollbackQuery(), nullValue(String.class));
    }
//
//    @Test
//    public void testTable() throws Exception {
//        RollbackDDLSt st = RollbackDDLSt.construct("CREATE", "TABLE", "DDV_HISTORY");
//        assumeThat(st.getRollbackQuery(), nullValue(String.class));
//    }
}
