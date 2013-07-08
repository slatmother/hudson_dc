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

import constants.IConstants;
import database.DBHelper;
import database.DBHelperFactory;
import transaction.SQLTx;
import util.querytemplate.QueryTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 08.07.13</p>
 *
 * @version 1.0
 */
public class RollbackDDLSt {
    private QueryTemplate userSourceTemplate = QueryTemplate.queryTemplate(
            "select 'create or replace '||text as TEXT, line " +
                    "from user_source " +
                    "where name = '${name}' " +
                    "and line = 1 " +
                    "and type = '${type}' " +
                    "UNION " +
                    "select text, line " +
                    "from user_source us2 " +
                    "where us2.name = '${name}' " +
                    "and  us2.line > 1 " +
                    "and type = '${type}' " +
                    "order by line;",
            false);
    private QueryTemplate dropTemplate = QueryTemplate.queryTemplate(
            "drop ${type} ${name}",
            false
    );

    private String defType;
    private String defName;
    private String rollbackQuery;

    public RollbackDDLSt(String defType, String defName) {
        this.defType = defType;
        this.defName = defName;
    }

    public static RollbackDDLSt construct(String ddlType, String defType, String defName) throws SQLException {
        boolean needDBSearch = checkParams(ddlType, defType);
        RollbackDDLSt statement = new RollbackDDLSt(defType, defName);

        if (needDBSearch) {
            statement.constructRollbackQuery();
        }

        return statement;
    }

    private static boolean checkParams(String ddlType, String defType) {
        return !("COMMENT".equalsIgnoreCase(ddlType) ||
                "TRUNCATE".equalsIgnoreCase(ddlType)) ||
                !"TABLE".equalsIgnoreCase(defType) ||
                !"SEQUENCE".equalsIgnoreCase(defType);
    }

    protected void constructRollbackQuery() throws SQLException {
        DBHelper dbHelper = DBHelperFactory.getCustomProjectHelper();
        SQLTx sqlTxHelper = SQLTx.beginTransaction(dbHelper.getConnection());

        try {
            if (IConstants.QueryTypes.VIEW.toUpperCase().equals(defType)) {
                String sqlGetQuery = "select text from all_views where view_name = '" + defName.toUpperCase() + "'";

                ResultSet set = dbHelper.executeStatementWithoutCommit(sqlGetQuery);
                if (set.first()) {
                    rollbackQuery = "CREATE OR REPLACE FORCE VIEW " + defName + " " + set.getString(1);
                } else {
                    rollbackQuery = "drop view " + defName;
                }
            }

            if ("TRIGGER".equalsIgnoreCase(defType) ||
                    "PROCEDURE".equalsIgnoreCase(defType) ||
                    "FUNCTION".equalsIgnoreCase(defType)) {
                String sqlGetQuery = userSourceTemplate.substitute(
                        "name", defName,
                        "type", defType
                ).replaceAll("[\\n|\\t|\\r]", " ");

                ResultSet set = dbHelper.executeStatementWithoutCommit(sqlGetQuery);
                StringBuilder rollbackBuilder = new StringBuilder();
                while (set.next()) {
                    rollbackBuilder.
                            append(set.getString("TEXT")).
                            append(" ");
                }

                if (rollbackBuilder.length() == 0) {
                    rollbackBuilder.append(
                            dropTemplate.substitute(
                                    "type", defType,
                                    "name", defName
                            )
                    );
                }

                rollbackQuery = rollbackBuilder.toString();
            }
            sqlTxHelper.okToCommit();
        } finally {
            sqlTxHelper.commitOrAbort();
        }
    }

    public String getRollbackQuery() {
        return rollbackQuery;
    }
}
