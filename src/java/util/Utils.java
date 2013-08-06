package util;/*
* $Id
*
* (C) Copyright 1997 i-Teco, CJSK. All Rights reserved.
* i-Teco PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*
* Эксклюзивные права 1997 i-Teco, ЗАО.
* Данные исходные коды не могут использоваться и быть изменены
* без официального разрешения компании i-Teco.          
*/

import com.documentum.com.DfClientX;
import com.documentum.com.IDfClientX;
import com.documentum.fc.client.*;
import com.documentum.fc.common.*;
import com.sun.istack.NotNull;
import constants.IConstants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 07.05.13</p>
 *
 * @version 1.0
 */
public class Utils {
    protected final static IDfClientX clientX = new DfClientX();


    public static IDfSession getSession(String userName, String passwd, String docbaseName) throws DfException {
        IDfClient client = clientX.getLocalClient();

        IDfLoginInfo loginInfo = new DfLoginInfo();
        loginInfo.setUser(userName);
        loginInfo.setPassword(passwd);
        return client.newSession(docbaseName, loginInfo);
    }

    public static IDfSession getSessionFromConfig() throws DfException {
        @NotNull String user = Configuration.getConfig_properties().getProperty("documentum.user");
        @NotNull String passwd = Configuration.getConfig_properties().getProperty("documentum.password");
        @NotNull String docbase = Configuration.getConfig_properties().getProperty("documentum.docbase");

        IDfClient client = clientX.getLocalClient();

        IDfLoginInfo loginInfo = new DfLoginInfo();
        loginInfo.setUser(user);
        loginInfo.setPassword(passwd);
        return client.newSession(docbase, loginInfo);
    }


    /**
     * dql-запроса возвращает мэп <имя атрибута>-<значение> Все значения
     * типизированы в соответствии с типом атрибута. Для рипитингов будет
     * возвращем массив значений
     * TODO сделать возвращаемый результат типизированным как в Jackson ObjectMapper.readValue
     *
     * @param session сессия
     * @param query   запрос
     * @return null - ничего по запросу не получено
     * @throws DfException ошибки при работе с БД
     */
    public static Map<String, Object> getFirstRow(IDfSession session, String query)
            throws DfException {
        // проверяем входные параметры
//        Checker.checkSession("session", session, false);
//        if (query == null || query.length() == 0)
//            throw new ObjectRelationMappingException("Query param is empty or null");
        IDfQuery q = clientX.getQuery();
        q.setDQL(query);
        IDfCollection col = null;
        try {
            col = q.execute(session, 0);
            if (col.next()) {
                int n = col.getAttrCount();
                Map<String, Object> res = new HashMap<String, Object>(n);
                // идем по атрибутам
                for (int i = 0; i < n; i++) {
                    IDfAttr attr = col.getAttr(i);
                    res.put(attr.getName(), getTypedValue(col, attr.getName()));
                }
                return res;
            } else
                return Collections.emptyMap();
        } finally {
            if (col != null)
                col.close();
        }
    }

    /**
     * @param col      коллекция запроса
     * @param attrName имя атрибута
     * @return
     * @throws DfException ошибки при работе с БД
     * @date 10.10.2011 @author a.dunaev (a.dunaev@i-teco.ru) Возвращает объект
     * значения соответствующего ему типа. Для репитинга возвращает массив
     * значений. Для сингла только значение.
     */
    protected static Object getTypedValue(IDfCollection col, String attrName) throws DfException {
        Object res = null;
        // определяем сингл или репитинг нам дали
        if (!col.isAttrRepeating(attrName)) {
            // получаем сингл
            res = getTypedValue(col, attrName, 0);
        } else {
            int n = col.getValueCount(attrName);
            switch (col.getAttrDataType(attrName)) {
                case IDfType.DF_INTEGER:
//                    Integer[] intVals = new Integer[n];
                    ArrayList<Integer> intList = new ArrayList<Integer>(n);
                    for (int i = 0; i < n; i++)
//                        intVals[i] = col.getRepeatingInt(attrName, i);
                        intList.add(col.getRepeatingInt(attrName, i));
//                    res = intVals;
                    res = intList;
                    break;
                case IDfType.DF_DOUBLE:
//                    Integer[] doubVals = new Integer[n];
                    ArrayList<Integer> doubList = new ArrayList<Integer>(n);
                    for (int i = 0; i < n; i++)
//                        doubVals[i] = col.getRepeatingInt(attrName, i);
                        doubList.add(col.getRepeatingInt(attrName, i));

//                    res = doubVals;
                    res = doubList;
                    break;
                case IDfType.DF_BOOLEAN:
//                    Boolean[] boolVals = new Boolean[n];
                    ArrayList<Boolean> boolList = new ArrayList<Boolean>(n);
                    for (int i = 0; i < n; i++)
//                        boolVals[i] = col.getRepeatingBoolean(attrName, i);
                        boolList.add(col.getRepeatingBoolean(attrName, i));
//                    res = boolVals;
                    res = boolList;
                    break;
                case IDfType.DF_TIME:
//                    Date[] dateVals = new Date[n];
                    ArrayList<Date> dateList = new ArrayList<Date>(n);
                    for (int i = 0; i < n; i++)
//                        dateVals[i] = col.getRepeatingTime(attrName, i).getDate();
                        dateList.add(col.getRepeatingTime(attrName, i).getDate());
//                    res = dateVals;
                    res = dateList;
                    break;
                case IDfType.DF_STRING:
                default:
//                    String[] strVals = new String[n];
                    ArrayList<String> strList = new ArrayList<String>(n);
                    for (int i = 0; i < n; i++)
//                        strVals[i] = col.getRepeatingString(attrName, i);
                        strList.add(col.getRepeatingString(attrName, i));
//                    res = strVals;
                    res = strList;
                    break;
            }
        }
        return res;
    }

    /**
     * @param col      коллекция ответа на запрос
     * @param attrName имя атрибута
     * @param index    индекс репитинга
     * @return объект соответствующего значению типа
     * @throws DfException ошибки при работе с БД
     * @date 10.10.2011 @author a.dunaev (a.dunaev@i-teco.ru) Возвращает объект
     * значения соответствующего ему типа. Работает как с репитингами так
     * и с синглами. Для синглов передаем 0.
     */
    protected static Object getTypedValue(IDfCollection col, String attrName, int index) throws DfException {
        Object res = null;
        switch (col.getAttrDataType(attrName)) {
            case IDfType.DF_INTEGER:
                res = col.getRepeatingInt(attrName, index);
                break;
            case IDfType.DF_DOUBLE:
                res = col.getRepeatingInt(attrName, index);
                break;
            case IDfType.DF_BOOLEAN:
                res = col.getRepeatingBoolean(attrName, index);
                break;
            case IDfType.DF_TIME:
                res = col.getRepeatingTime(attrName, index).getDate();
                break;
            case IDfType.DF_STRING:
            default:
                res = col.getRepeatingString(attrName, index);
                break;
        }
        return res;
    }

    /**
     * @return
     * @throws DfException
     * @date 06.10.2011 @author a.dunaev (a.dunaev@i-teco.ru) Возвращает объект
     * значения соответствующего ему типа. Работает как с репитингами так
     * и с синглами. Для синглов передаем 0.
     */
    protected static Object getTypedValue(IDfPersistentObject obj, String attrName, int index) throws DfException {
        Object res = null;
        switch (obj.getAttrDataType(attrName)) {
            case IDfType.DF_INTEGER:
                res = obj.getRepeatingInt(attrName, index);
                break;
            case IDfType.DF_DOUBLE:
                res = obj.getRepeatingDouble(attrName, index);
                break;
            case IDfType.DF_BOOLEAN:
                res = obj.getRepeatingBoolean(attrName, index);
                break;
            case IDfType.DF_TIME:
                res = obj.getRepeatingTime(attrName, index).getDate();
                break;
            case IDfType.DF_STRING:
            default:
                res = obj.getRepeatingString(attrName, index);
                break;
        }
        return res;
    }

    /**
     * dql-запроса возвращает список мэпов вида <имя атрибута>-<значение>,
     * где каждый элемент списка это строка результата выполнения запроса.
     * Все значения типизированы в соответствии с типом атрибута. Для
     * рипитингов будет возвращем массив значений
     * TODO сделать возвращаемый результат типизированным как в Jackson ObjectMapper.readValue
     *
     * @param session сессия
     * @param query   запрос
     * @return Список строк. Если запрос не вернул не одной строки, List будет
     *         пустой.
     * @throws DfException ошибки при работе с БД
     */
    public static List<Map<String, Object>> getAllRows(IDfSession session, String query)
            throws DfException {
        // проверяем входные параметры
//        Checker.checkSession("session", session, false);
//        if (query == null || query.length() == 0)
//            throw new ObjectRelationMappingException("Query param is empty or null");
        IDfQuery q = clientX.getQuery();
        q.setDQL(query);
        IDfCollection col = null;
        try {
            col = q.execute(session, 0);
            LinkedList<Map<String, Object>> res = new LinkedList<Map<String, Object>>();
            while (col.next()) {
                int n = col.getAttrCount();
                Map<String, Object> row = new HashMap<String, Object>(n);
                // идем по атрибутам
                for (int i = 0; i < n; i++) {
                    IDfAttr attr = col.getAttr(i);
                    row.put(attr.getName(), getTypedValue(col, attr.getName()));
                }
                res.add(row);
            }
            return res;
        } finally {
            if (col != null)
                col.close();
        }
    }

    public static void closeResources(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            Statement st = resultSet.getStatement();

            if (!st.isClosed()) {
                st.close();
            }
        }
    }

    /**
     * Проверяет на null и на непустоту строку
     *
     * @param value - строковое значение
     * @return boolean возвращает true, если строка не null и не пуста, иначе false
     */
    public static boolean isNotNull(String value) {
        return (value != null) && (value.trim().length() > 0);
    }

    public static boolean isNull(String value) {
        return (value == null) || (value.trim().length() == 0);
    }

    public static int checkAffectedParams(Integer minValue, Integer maxValue) {
        if (maxValue == null) {
            return IConstants.AffectedTypes.INFINITY_RANGE;
        } else if (maxValue.equals(minValue)) {
            return IConstants.AffectedTypes.VALUE;
        } else if (maxValue > minValue) {
            return IConstants.AffectedTypes.RANGE;
        } else {
            return IConstants.AffectedTypes.UNDEFINED;
        }
    }

    public static String checkDQLQueryReturnedType(Map<String, Object> queryMap) {
        if (queryMap.containsKey(IConstants.DQLQueryReturnVals.UPDATED)) {
            return IConstants.DQLQueryReturnVals.UPDATED;
        } else if (queryMap.containsKey(IConstants.DQLQueryReturnVals.DELETED)) {
            return IConstants.DQLQueryReturnVals.DELETED;
        } else if (queryMap.containsKey(IConstants.DQLQueryReturnVals.CREATED)) {
            return IConstants.DQLQueryReturnVals.CREATED;
        } else {
            return "";
        }
    }

    public static boolean isNotNullId(String value) {
        return DfId.isObjectId(value);
    }
}
