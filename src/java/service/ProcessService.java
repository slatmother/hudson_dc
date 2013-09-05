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
package service;

import com.documentum.fc.client.IDfSession;
import com.documentum.fc.common.DfException;
import constants.IConstants;
import groovy.execution.DSLManager;
import groovy.container.DSLContainer;
import factory.JdbcTemplateFactory;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import transaction.holder.NestedTx;
import util.Utils;

import java.sql.SQLException;
import java.util.Map;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 06.08.13</p>
 *
 * @version 1.0
 */
public class ProcessService {
    private static final Logger logger = Logger.getRootLogger();

    public static boolean execute(String operation, Map<DSLContainer, Object> dcMap) throws DfException, SQLException {
        if (IConstants.MainArgsTypes.Operation.RUN.equalsIgnoreCase(operation)) {
            return run(dcMap);
        } else if (IConstants.MainArgsTypes.Operation.VALIDATE.equalsIgnoreCase(operation)) {
            return validate(dcMap);
        } else return false;
    }

    private static boolean validate(Map<DSLContainer, Object> dcMap) {
        boolean result = true;

        for (Map.Entry<DSLContainer, Object> entry : dcMap.entrySet()) {
            result &= DSLManager.validate(entry.getKey());
        }
        return result;
    }

    private static boolean run(Map<DSLContainer, Object> dcMap) throws DfException, SQLException {
        boolean result = true;
        NestedTx dqlTx = null;
        try {
            IDfSession session = null;

            if (dcMap.containsValue("dql")) {
                session = Utils.getSessionFromConfig();
                dqlTx = NestedTx.beginTx(session);
            }

            JdbcTemplate dbTemplate = null;
            if (dcMap.containsValue("sql")) {
                dbTemplate = JdbcTemplateFactory.getProjectDBTemplate();
            }

            for (Map.Entry<DSLContainer, Object> entry : dcMap.entrySet()) {
                result &= (Boolean) DSLManager.executeDC(entry.getKey(), entry.getValue().equals("dql") ? session : dbTemplate);
            }

            logger.info("Total execution result is " + result);

            if (result && dqlTx != null) {
                dqlTx.okToCommit();
            }
        } catch (DfException e) {
            logger.error(e);
            throw e;
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } finally {
            if (dqlTx != null) {
                dqlTx.commitOrAbort();
            }
        }

        return result;
    }
}
