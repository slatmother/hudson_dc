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
package util;

import database.DatabaseHelper;
import database.DatabaseHelperFactory;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 25.06.13</p>
 *
 * @version 1.0
 */
public class SQLUtilsTest {
    Logger logger = Logger.getRootLogger();

    @Test
    public void constructRollbackQueryTest() {
        DatabaseHelper dbHelper = null;
        try {
            dbHelper = DatabaseHelperFactory.getCustomProjectHelper();
            String query = "CREATE OR REPLACE FORCE VIEW ddv_department_branch AS (\n" +
                    "SELECT\n" +
                    "  ddt_department.r_object_id AS r_object_id,\n" +
                    "  ddt_department.dss_name AS dss_department,\n" +
                    "  ddt_department.dss_index AS dss_index,\n" +
                    "  ddt_department.dsdt_begin AS dsdt_department_begin,\n" +
                    "  ddt_department.dsdt_end AS dsdt_department_end,\n" +
                    "  ddt_branch.r_object_id AS dsid_branch,\n" +
                    "  ddt_branch.dss_name AS dss_branch,\n" +
                    "  ddt_branch.dss_okpo AS dss_branch_okpo,\n" +
                    "  ddt_branch.dss_ogrn AS dss_branch_ogrn,\n" +
                    "  ddt_branch.dss_inn AS dss_branch_inn,\n" +
                    "  ddt_branch.dss_kpp AS dss_branch_kpp,\n" +
                    "  ddt_branch.dsdt_begin AS dsdt_branch_begin,\n" +
                    "  ddt_branch.dsdt_end AS dsdt_branch_end,\n" +
                    "  ddt_branch.dsid_directory_group AS dsid_directory_group,\n" +
                    "  NVL(ddt_parent_department.r_object_id,ddt_branch.r_object_id) AS dsid_parent_department,\n" +
                    "  NVL(ddt_parent_department.dss_name,ddt_branch.dss_name) AS dss_parent_department\n" +
                    "FROM\n" +
                    "  ddt_department_sp ddt_department JOIN ddt_branch_sp ddt_branch\n" +
                    "\t  ON ddt_department.dsid_directory_group = ddt_branch.dsid_directory_group\n" +
                    " LEFT JOIN ddt_periodical_relation_sp relation\n" +
                    "\t  ON ddt_department.r_object_id = relation.dsid_child\n" +
                    " LEFT JOIN ddt_department_sp ddt_parent_department\n" +
                    "\t  ON  relation.dsid_parent  = ddt_parent_department.r_object_id\n" +
                    ")\n";
            String rollbackQuery = SQLUtils.constructRollbackQuery(dbHelper, query, "view");

            assertTrue(Utils.isNotNull(rollbackQuery));

            logger.info(rollbackQuery);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (dbHelper != null) {
                try {
                    dbHelper.closeConnection();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }
}
