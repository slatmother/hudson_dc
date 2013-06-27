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
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 25.06.13</p>
 *
 * @version 1.0
 */
public class SQLUtils {
    private static final Logger logger = Logger.getRootLogger();

    public static String constructRollbackQuery(DatabaseHelper dbHelper, String query, String queryType) throws SQLException {
        String rollbackQuery = "";
        if ("view".equals(queryType)) {
            Pattern viewPattern = Pattern.compile("(?<=VIEW\\s)(\\w*)(?=\\sAS)");

            Matcher m = viewPattern.matcher(query);
            while (m.find()) {
                String s = m.group(0);

                logger.info("Found match is " + s);

                ResultSet set = dbHelper.executeStatementWithoutCommit("select text from all_views where view_name = '" + s.toUpperCase() + "'");

                if (set.getRow() != 0) {
                    while (set.next()) {
                        rollbackQuery = set.getString(1);
                    }
                } else {
                   rollbackQuery = "drop view " + s;
                }
            }
        }
        return rollbackQuery;
    }

    public static String checkQueryType(String query) {
        if (query.contains("CREATE OR REPLACE VIEW")) {
            return "view";
        }

        return "";
    }
}
