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

import org.junit.Test;
import parser.sql.SQLStatement;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 05.07.13</p>
 *
 * @version 1.0
 */
public class SQLStatementTest {
    @Test
    public void testProcedure() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "create or replace procedure\n" +
                "ased.create_journal_trigger(object_attr_table_name varchar2, journal_name varchar2)\n" +
                "is\n" +
                "begin\n" +
                "      EXECUTE IMMEDIATE\n" +
                "            'create or replace trigger '||object_attr_table_name||'t\n" +
                "            after insert or update or delete on '||object_attr_table_name||\n" +
                "            ' for each row\n" +
                "            declare\n" +
                "               oid varchar2(16);\n" +
                "               delete_flag char(1) :=''N'';\n" +
                "            begin\n" +
                "               oid := :new.r_object_id;\n" +
                "               if oid is null\n" +
                "                    then\n" +
                "                    oid := :old.r_object_id;\n" +
                "                    delete_flag := ''Y'';\n" +
                "               end if;\n" +
                "               delete from '||journal_name||' where objectId = oid;\n" +
                "               insert into '||journal_name||'(objectId, modification_date,is_deleted)\n" +
                "               values (oid, sys_extract_utc(systimestamp),delete_flag);\n" +
                "            end;';\n" +
                "end;";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("CREATE_JOURNAL_TRIGGER", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("PROCEDURE", (String) defType.get(statement));
    }

    @Test
    public void testProcedureWithoutComma() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "create or replace procedure\n" +
                "create_journal_trigger(object_attr_table_name varchar2, journal_name varchar2)\n" +
                "is\n" +
                "begin\n" +
                "      EXECUTE IMMEDIATE\n" +
                "            'create or replace trigger '||object_attr_table_name||'t\n" +
                "            after insert or update or delete on '||object_attr_table_name||\n" +
                "            ' for each row\n" +
                "            declare\n" +
                "               oid varchar2(16);\n" +
                "               delete_flag char(1) :=''N'';\n" +
                "            begin\n" +
                "               oid := :new.r_object_id;\n" +
                "               if oid is null\n" +
                "                    then\n" +
                "                    oid := :old.r_object_id;\n" +
                "                    delete_flag := ''Y'';\n" +
                "               end if;\n" +
                "               delete from '||journal_name||' where objectId = oid;\n" +
                "               insert into '||journal_name||'(objectId, modification_date,is_deleted)\n" +
                "               values (oid, sys_extract_utc(systimestamp),delete_flag);\n" +
                "            end;';\n" +
                "end;";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("CREATE_JOURNAL_TRIGGER", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("PROCEDURE", (String) defType.get(statement));
    }

    @Test
    public void testFunction() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "create or replace function ased.table_exists(table_to_check varchar2)\n" +
                "return boolean is\n" +
                "    tbl_cnt PLS_INTEGER;   \n" +
                "begin\n" +
                "    select count(*)\n" +
                "    into tbl_cnt\n" +
                "    from user_tables where table_name=(upper(table_to_check));\n" +
                "    return tbl_cnt > 0;\n" +
                "end;";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("TABLE_EXISTS", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("FUNCTION", (String) defType.get(statement));
    }

    @Test
    public void testFunctionWithoutComma() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "create or replace function table_exists(table_to_check varchar2)\n" +
                "return boolean is\n" +
                "    tbl_cnt PLS_INTEGER;   \n" +
                "begin\n" +
                "    select count(*)\n" +
                "    into tbl_cnt\n" +
                "    from user_tables where table_name=(upper(table_to_check));\n" +
                "    return tbl_cnt > 0;\n" +
                "end;";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("TABLE_EXISTS", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("FUNCTION", (String) defType.get(statement));
    }

    @Test
    public void testComment() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "COMMENT ON TABLE ased.sync_journals_list IS 'Documentum type to journal table mapping'";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("SYNC_JOURNALS_LIST", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("COMMENT", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("TABLE", (String) defType.get(statement));
    }

    @Test
    public void testCommentWithoutComma() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "COMMENT ON TABLE sync_journals_list IS 'Documentum type to journal table mapping'";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("SYNC_JOURNALS_LIST", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("COMMENT", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("TABLE", (String) defType.get(statement));
    }


    @Test
    public void testTable() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "CREATE TABLE ased.sync_journals_list (\n" +
                "    object_type VARCHAR(32) not null,\n" +
                "    journal_name VARCHAR(50) not null,\n" +
                "    CONSTRAINT journal_name_uc UNIQUE (journal_name),\n" +
                "    CONSTRAINT object_type_uc UNIQUE (object_type)\n" +
                ")";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("SYNC_JOURNALS_LIST", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("TABLE", (String) defType.get(statement));
    }

    @Test
    public void testTableWithoutComma() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "CREATE TABLE sync_journals_list (\n" +
                "    object_type VARCHAR(32) not null,\n" +
                "    journal_name VARCHAR(50) not null,\n" +
                "    CONSTRAINT journal_name_uc UNIQUE (journal_name),\n" +
                "    CONSTRAINT object_type_uc UNIQUE (object_type)\n" +
                ")";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("SYNC_JOURNALS_LIST", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("TABLE", (String) defType.get(statement));
    }

    @Test
    public void testTrigger() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "create or replace trigger ased.sync_journals_list_ad_trg\n" +
                "    after delete on ased.sync_journals_list\n" +
                "   for each row\n" +
                "declare\n" +
                "job_name varchar2(60);\n" +
                "begin\n" +
                "    job_name := 'deleting_journal_job_'||:old.object_type;\n" +
                "    dbms_job.submit( job_name , 'ased.drop_journal('''||:old.object_type||''', '''||:old.journal_name||''');' );\n" +
                "end sync_journals_list_ad_trg;";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("SYNC_JOURNALS_LIST_AD_TRG", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("TRIGGER", (String) defType.get(statement));
    }

    @Test
    public void testTriggerWithoutComma() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "create or replace trigger sync_journals_list_ad_trg\n" +
                "    after delete on ased.sync_journals_list\n" +
                "   for each row\n" +
                "declare\n" +
                "job_name varchar2(60);\n" +
                "begin\n" +
                "    job_name := 'deleting_journal_job_'||:old.object_type;\n" +
                "    dbms_job.submit( job_name , 'ased.drop_journal('''||:old.object_type||''', '''||:old.journal_name||''');' );\n" +
                "end sync_journals_list_ad_trg;";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("SYNC_JOURNALS_LIST_AD_TRG", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("TRIGGER", (String) defType.get(statement));
    }

    @Test
    public void testSequence() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "CREATE SEQUENCE ddseq_document_barcode\n" +
                "INCREMENT BY 1\n" +
                "START WITH 1\n" +
                "MAXVALUE 999999999999999999\n" +
                "MINVALUE 1\n" +
                "NOCYCLE\n" +
                "NOCACHE ORDER";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("DDSEQ_DOCUMENT_BARCODE", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("SEQUENCE", (String) defType.get(statement));
    }

    @Test
    public void testView() throws NoSuchFieldException, IllegalAccessException, SQLException {
        String query = "CREATE OR REPLACE VIEW DDV_HISTORY AS (\n" +
                "SELECT\n" +
                "ddt_extended_audit.string_1 AS dss_event,\n" +
                "ddt_extended_audit.string_3 AS dss_status,\n" +
                "ddt_extended_audit.id_1 AS dsid_document,\n" +
                "ddt_extended_audit.string_2 AS dss_note,\n" +
                "ddt_extended_audit_r.drs_new_value AS dss_performer_employee,\n" +
                "ddt_extended_audit.time_1 AS dsdt_send,\n" +
                "ddt_extended_audit.time_2 AS dsdt_acquire,\n" +
                "ddt_extended_audit.time_3 AS dsdt_finish\n" +
                "FROM\n" +
                "ddt_extended_audit_s ddt_extended_audit\n" +
                "INNER JOIN\n" +
                "ddt_extended_audit_r ON ddt_extended_audit.r_object_id = ddt_extended_audit_r.r_object_id\n" +
                "WHERE ddt_extended_audit_r.drs_attribute_name = 'performer_employee'\n" +
                ")";

        query = query.replaceAll("[\\n|\\t|\\r]", " ");

        SQLStatement statement = SQLStatement.resolve(query);
        assumeTrue(statement.isDDLQuery());

        Field defName = statement.getClass().getDeclaredField("defName");
        defName.setAccessible(true);
        assertEquals("DDV_HISTORY", (String) defName.get(statement));

        Field ddlType = statement.getClass().getDeclaredField("ddlType");
        ddlType.setAccessible(true);
        assertEquals("CREATE", (String) ddlType.get(statement));

        Field defType = statement.getClass().getDeclaredField("defType");
        defType.setAccessible(true);
        assertEquals("VIEW", (String) defType.get(statement));
    }
}
