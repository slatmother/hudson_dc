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
package transaction.script;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import constants.IConstants;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import util.Utils;

import java.sql.SQLException;
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
public class ProjectTrScript {
    private static final Logger logger = Logger.getRootLogger();

    /**
     * @param session
     * @param query
     * @param conditionsMapList
     * @return
     * @throws DfException
     */
    public static boolean query(IDfSession session, String query, List<Map<String, Object>> conditionsMapList) throws DfException {
        logger.info("Running script. Query to execute is: " + query);

        List<Map<String, Object>> rowsList = Utils.getAllRows(session, query);
        boolean result = true;

        if (conditionsMapList.size() == rowsList.size()) {
            for (int i = 0; i < rowsList.size(); i++) {
                Map<String, Object> executedMap = rowsList.get(i);
                Set<Map.Entry<String, Object>> actualEntrySet = executedMap.entrySet();

                result &= actualEntrySet.containsAll(conditionsMapList.get(i).entrySet());
            }
        } else {
            result = false;
        }

        logger.info("Script execution result is " + result);

        return result;
    }

    public static boolean query(IDfSession session, String query, Integer minAffectedValue, Integer maxAffectedValue) throws DfException {
        logger.info("Query to execute is: " + query);

        List<Map<String, Object>> rowsList = Utils.getAllRows(session, query);
        Map<String, Object> map = rowsList.get(0);
        boolean result;

        int affectedCase = Utils.checkAffectedParams(minAffectedValue, maxAffectedValue);
        String dqlQueryReturnVal = Utils.checkDQLQueryReturnedType(map);

        switch (affectedCase) {
            case IConstants.AffectedTypes.INFINITY_RANGE:
                result = IConstants.DQLQueryReturnVals.CREATED.equals(dqlQueryReturnVal) ?
                        minAffectedValue <= rowsList.size() &&
                                Utils.isNotNullId((String) map.get(dqlQueryReturnVal))
                        :
                        minAffectedValue <= (Integer) (map.get(dqlQueryReturnVal));
                break;
            case IConstants.AffectedTypes.VALUE:
                result = IConstants.DQLQueryReturnVals.CREATED.equals(dqlQueryReturnVal) ?
                        minAffectedValue.equals(rowsList.size()) &&
                                Utils.isNotNullId((String) map.get(dqlQueryReturnVal))
                        :
                        minAffectedValue.equals(map.get(dqlQueryReturnVal));
                break;
            case IConstants.AffectedTypes.RANGE:
                result = IConstants.DQLQueryReturnVals.CREATED.equals(dqlQueryReturnVal) ?
                        minAffectedValue <= rowsList.size() &&
                                maxAffectedValue >= rowsList.size() &&
                                Utils.isNotNullId((String) map.get(dqlQueryReturnVal))
                        :
                        minAffectedValue <= (Integer) (map.get(dqlQueryReturnVal)) &&
                                maxAffectedValue >= (Integer) (map.get(dqlQueryReturnVal));
                break;
            default:
                return false;
        }

        logger.info("Result of execution is " + result);
        return result;
    }

    /**
     * @param template
     * @param query
     * @param conditionsMapList
     * @return
     * @throws SQLException
     */
    public static boolean query(JdbcTemplate template, String query, List<Map<String, Object>> conditionsMapList) throws SQLException {
        logger.info("Query to execute is: " + query);

        SqlRowSet set = template.queryForRowSet(query);
        boolean result = true;

        SqlRowSetMetaData mdata = set.getMetaData();
        int columnAmount = mdata.getColumnCount();
        logger.info("Columns: " + columnAmount);
        logger.info("Map size: " + conditionsMapList.size());

        if (set.first()) {
            set.last();
            int rowNum = set.getRow();
            result = (rowNum == conditionsMapList.size());
            set.beforeFirst();
        } else {
            if (!conditionsMapList.get(0).isEmpty()) {
                result = false;
            }
        }

        logger.info("Two maps comparison result is " + result);

        if (result) {
            while (set.next()) {
                int rowNum = set.getRow();

                Map<String, Object> map = conditionsMapList.get(rowNum - 1);

                for (int i = 1; i <= columnAmount; i++) {
                    result &= map.containsKey(mdata.getColumnName(i)) &&
                            map.get(mdata.getColumnName(i)).toString().equals(set.getObject(i).toString());
                }
            }
        }
        return result;
    }

    /**
     * @param template
     * @param query
     * @return
     * @throws SQLException
     */
    public static boolean query(JdbcTemplate template, String query, Integer minAffectedValue, Integer maxAffectedValue) throws SQLException {
        logger.info("Query to execute is: " + query);

        int resultInt = template.update(query);
        int affectedCase = Utils.checkAffectedParams(minAffectedValue, maxAffectedValue);

        switch (affectedCase) {
            case IConstants.AffectedTypes.INFINITY_RANGE:
                return resultInt >= minAffectedValue;
            case IConstants.AffectedTypes.VALUE:
                return resultInt == minAffectedValue;
            case IConstants.AffectedTypes.RANGE:
                return resultInt >= minAffectedValue && resultInt <= maxAffectedValue;
            default:
                return false;
        }
    }
}
