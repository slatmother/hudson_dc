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
package execution;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import dao.DatabaseHelper;
import org.apache.log4j.Logger;
import util.Utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 15.05.13</p>
 *
 * @version 1.0
 */
public class DCScriptRunner {
    private static final Logger logger = Logger.getRootLogger();

    public static boolean runScriptWithOneRow(IDfSession session, String query, Map<String, Object> conditionsMap) throws DfException {
        Map<String, Object> map = Utils.getFirstRow(session, query);

        Set<Map.Entry<String, Object>> actualEntrySet = map.entrySet();

        boolean result = true;

        for (Map.Entry entry : conditionsMap.entrySet()) {
            result &= actualEntrySet.contains(entry);
        }

        return result;
    }

    public static boolean runDQLScript(IDfSession session, String query, List<Map<String, Object>> conditionsMapList) throws DfException {
        logger.info(query);

        List<Map<String, Object>> rowsList = Utils.getAllRows(session, query);
        boolean result = true;

        if (conditionsMapList.size() == rowsList.size()) {
            for (int i = 0; i < rowsList.size(); i++) {
                Map<String, Object> executedMap = rowsList.get(i);
                Set<Map.Entry<String, Object>> actualEntrySet = executedMap.entrySet();

                Map<String, Object> conditionsMap = conditionsMapList.get(i);
                result = actualEntrySet.containsAll(conditionsMap.entrySet());
            }
        } else {
            result = false;
        }

        return result;
    }

    public static boolean runDQLScript(IDfSession session, String query, Integer affectedRows) throws DfException {
        Map<String, Object> map = Utils.getFirstRow(session, query);

        return affectedRows.equals(map.get("objects_updated")) ||
                affectedRows.equals(map.get("objects_created")) ||
                affectedRows.equals(map.get("objects_deleted"));
    }

    public static boolean runSQLScript(DatabaseHelper dao, String query, List<Map<String, Object>> conditionsMapList) throws SQLException {
        logger.info(query);

        ResultSet set = dao.executeStatementWithoutCommit(query);
        boolean result = true;

        try {
            ResultSetMetaData mdata = set.getMetaData();
            int columnAmount = mdata.getColumnCount();

            while (set.next()) {
                int rowNum = set.getRow();

                if (rowNum == 0) {
                    break;
                }

                Map<String, Object> map = conditionsMapList.get(rowNum - 1);

                if (columnAmount != map.size()) {
                    continue;
                }

                for (int i = 1; i <= columnAmount; i++) {
                    logger.info(rowNum + " " + set.getObject(i).toString());
                    logger.info("result is: " + map.get(mdata.getColumnName(i)).toString().equals(set.getObject(i).toString()));
                    logger.info("Map value is " + map.get(mdata.getColumnName(i)).toString());

                    result &= map.containsKey(mdata.getColumnName(i))
                            && map.get(mdata.getColumnName(i)).toString().equals(set.getObject(i).toString());
                }
            }
        } finally {
            closeResources(set);
        }

        return result;
    }

    public static boolean runSQLScript(DatabaseHelper dao, String query, Integer affectedRows) {
        boolean result;
        try {
            int resultInt = dao.executeUpdateWithoutCommit(query);
            result = (resultInt == affectedRows);
        } catch (SQLException e) {
            logger.error(e);
            result = false;
        }

        return result;
    }

    private static void closeResources(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            Statement st = resultSet.getStatement();

            if (!st.isClosed()) {
                st.close();
            }
        }
    }
}
