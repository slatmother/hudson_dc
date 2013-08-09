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
package main;

import com.documentum.fc.common.DfException;
import constants.IConstants;
import execution.groovy.dsl.container.DSLContainer;
import org.apache.log4j.Logger;
import service.LocationService;
import service.ProcessService;
import service.TypeService;
import util.Utils;

import java.sql.SQLException;
import java.util.LinkedHashMap;
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
public class Main {
    private static final Logger logger = Logger.getRootLogger();

    public static void main(String[] args) throws SQLException, DfException {
        String location = System.getProperty("location");
        if (Utils.isNull(location)) {
            location = IConstants.MainArgsTypes.Location.CQ;
        }
        logger.info("Location arg is " + location);

        String type = System.getProperty("type");
        if (Utils.isNull(type)) {
            type = IConstants.MainArgsTypes.Type.ALL;
        }
        logger.info("Type arg is " + type);


        String operation = System.getProperty("operation");
        if (Utils.isNull(operation)) {
            operation = IConstants.MainArgsTypes.Operation.RUN;
        }
        logger.info("Operation arg is " + operation);

        try {
            Map<DSLContainer, Object> dcMapping = new LinkedHashMap<DSLContainer, Object>();
            LocationService.performModification(location, dcMapping);
            dcMapping = TypeService.performModification(type, dcMapping);

            boolean result = ProcessService.execute(operation, dcMapping);
            logger.info("Result is " + result);
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        } catch (DfException e) {
            logger.error(e);
            throw e;
        }
    }
}
