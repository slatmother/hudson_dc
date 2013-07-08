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
package parser.sql;

import parser.IStatement;
import parser.RollbackDDLSt;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 05.07.13</p>
 *
 * @version 1.0
 */
public class SQLStatement implements IStatement {
    private static final String QUERY_TYPE = "SQL";
    private static final List<String> ddlTypes = new ArrayList<String>();
    private String query;
    private String defName;
    private String rollbackQuery;
    private String ddlType;
    private String defType;


    private boolean isDDLQuery;

    protected SQLStatement(String query) {
        ddlTypes.add("CREATE");
        ddlTypes.add("ALTER");
        ddlTypes.add("DROP");
        ddlTypes.add("TRUNCATE");
        ddlTypes.add("COMMENT");
        ddlTypes.add("RENAME");
        this.query = query;
    }

    public static SQLStatement resolve(String query) throws SQLException {
        SQLStatement statement = new SQLStatement(query);
        statement.enquireMetaData();
        return statement;
    }

    protected void enquireMetaData() throws SQLException {
        parseQuery();
        if (isDDLQuery) {
            constructRollbackQuery();
        }
    }

    protected void parseQuery() {
//        Pattern viewPattern = Pattern.compile("(^\\w*)(?>.*)((?i)FUNCTION\\s|(?i)PROCEDURE\\s|(?i)SEQUENCE\\s|VIEW\\s|(?i)TRIGGER\\s)(\\w*(?(?=\\.)\\.\\w*))");

        Pattern ddlTypePattern = Pattern.compile("(^\\w*)");
        Matcher m1 = ddlTypePattern.matcher(query);
        while (m1.find()) {
            ddlType = m1.group(0).toUpperCase();
        }

        Pattern defTypePattern = Pattern.compile("((?i)TABLE|(?i)FUNCTION|(?i)PROCEDURE|(?i)SEQUENCE|VIEW|(?i)TRIGGER)(?=\\s[a-zA-Z])");
        Matcher m2 = defTypePattern.matcher(query);
        while (m2.find()) {
            defType = m2.group(0).toUpperCase();
        }

        Pattern defNamePattern = Pattern.compile("(?<=(?i)TABLE\\s|(?i)FUNCTION\\s|(?i)PROCEDURE\\s|(?i)SEQUENCE\\s|(?i)VIEW\\s|(?i)TRIGGER\\s)\\w*[\\.|.]?(\\w*)");
        Matcher m3 = defNamePattern.matcher(query);
        if (m3.find() && m3.groupCount() > 0) {
            defName = m3.group(0).toUpperCase();
            if (defName.contains(".")) {
                defName = defName.split("\\.")[1];
            }
        }

        if (ddlTypes.contains(ddlType)) {
            isDDLQuery = true;
        }
    }

    protected void constructRollbackQuery() throws SQLException {
        RollbackDDLSt rollbackDDLSt = RollbackDDLSt.construct(ddlType, defType, defName);
        rollbackQuery = rollbackDDLSt.getRollbackQuery();
    }

    public String getQuery() {
        return query;
    }

    public String getRollbackQuery() {
        return rollbackQuery;
    }

    @Override
    public String getQueryType() {
        return QUERY_TYPE;
    }


    public boolean isDDLQuery() {
        return isDDLQuery;
    }
}
