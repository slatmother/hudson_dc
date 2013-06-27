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
import database.DatabaseHelper;
import org.apache.log4j.Logger;
import util.Utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
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

    /**
     * @param session
     * @param query
     * @param conditionsMap
     * @return
     * @throws DfException
     */
    public static boolean runScriptWithOneRow(IDfSession session, String query, Map<String, Object> conditionsMap) throws DfException {
        Map<String, Object> map = Utils.getFirstRow(session, query);

        Set<Map.Entry<String, Object>> actualEntrySet = map.entrySet();

        boolean result = true;

        for (Map.Entry entry : conditionsMap.entrySet()) {
            result &= actualEntrySet.contains(entry);
        }

        return result;
    }

    /**
     * @param session
     * @param query
     * @param conditionsMapList
     * @return
     * @throws DfException
     */
    public static boolean runScript(IDfSession session, String query, List<Map<String, Object>> conditionsMapList) throws DfException {
        logger.info("Query to execute is: " + query);

        List<Map<String, Object>> rowsList = Utils.getAllRows(session, query);
        boolean result = true;

        if (conditionsMapList.size() == rowsList.size()) {
            for (int i = 0; i < rowsList.size(); i++) {
                Map<String, Object> executedMap = rowsList.get(i);
                Set<Map.Entry<String, Object>> actualEntrySet = executedMap.entrySet();

                List<String> execKeys = new ArrayList<String>(executedMap.keySet());
                List<Object> execVals = new ArrayList<Object>(executedMap.values());
                logger.info("Executed keys: " + execKeys);
                logger.info("Executed vals: " + execVals);

                Map<String, Object> conditionsMap = conditionsMapList.get(i);
                List<String> condKeys = new ArrayList<String>(conditionsMap.keySet());
                List<Object> condVals = new ArrayList<Object>(conditionsMap.values());

                logger.info("Conditions keys: " + condKeys);
                logger.info("Conditions vals: " + condVals);

                result &= execKeys.equals(condKeys);
                result &= execVals.equals(condVals);

//                for (Map.Entry<String, Object> entry : conditionsMap.entrySet()) {
//                    logger.info(entry);
//                    result &= actualEntrySet.contains(entry);
//                }
            }
        } else {
            result = false;
        }

        return result;
    }

    public static boolean runScript(IDfSession session, String query, Integer affectedRows) throws DfException {
        logger.info("Query to execute is: " + query);
        logger.info("Expected num of affected rows is " + affectedRows);

        List<Map<String, Object>> rowsList = Utils.getAllRows(session, query);

        Map<String, Object> map = rowsList.get(0);
        boolean result;

        if (map.containsKey("objects_updated")) {
            logger.info("Objects updated " + map.get("objects_updated"));
            result = affectedRows.equals(map.get("objects_updated"));
        } else if (map.containsKey("objects_deleted")) {
            logger.info("Objects deleted " + map.get("objects_deleted"));
            result = affectedRows.equals(map.get("objects_deleted"));
        } else {
            logger.info("Is objects created condition? " + map.containsKey("object_created"));
            logger.info("Objects created " + rowsList.size());
            result = map.containsKey("object_created") && affectedRows.equals(rowsList.size());
        }

        logger.info("Result of executing is " + result);
        return result;
    }

    /**
     * @param dbHelper
     * @param query
     * @param conditionsMapList
     * @return
     * @throws SQLException
     */
    public static boolean runScript(DatabaseHelper dbHelper, String query, List<Map<String, Object>> conditionsMapList) throws SQLException {
        logger.info("Query to execute is: " + query);

        ResultSet set = dbHelper.executeStatementWithoutCommit(query);
        boolean result = true;

        try {
            ResultSetMetaData mdata = set.getMetaData();
            int columnAmount = mdata.getColumnCount();

            while (set.next()) {
                int rowNum = set.getRow();

                if (conditionsMapList.get(0).isEmpty()) {
                    if (!(rowNum == 0)) {
                        result = false;
                    }
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
            Utils.closeResources(set);
        }

        return result;
    }

    /**
     * @param dbHelper
     * @param query
     * @param affectedRows
     * @return
     * @throws SQLException
     */
    public static boolean runScript(DatabaseHelper dbHelper, String query, Integer affected) throws SQLException {
        logger.info("Query to execute is: " + query);

        int resultInt = dbHelper.executeUpdateWithoutCommit(query);

//        if (minVal != null) {
//            if (maxVal != null) {
//                if (maxVal.equals(minVal)) {
//                    return resultInt == minVal;
//                } else {
//                    return (minVal < resultInt) && (resultInt < maxVal);
//                }
//            } else {
//                return minVal < resultInt;
//            }
//        } else {
//            return false;
//        }
        return resultInt == affected;
    }
}
