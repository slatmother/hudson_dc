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

import execution.groovy.dsl.DSLManager;
import execution.groovy.dsl.container.DSLContainer;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 01.07.13</p>
 *
 * @version 1.0
 */
public class Validator {
    private static final Logger logger = Logger.getRootLogger();

    public static void main(String[] args) {
        File dqlFolder = new File("./DC");

        File[] dcArr = dqlFolder.listFiles();
        boolean dqlResult = true;
        ArrayList<String> failedDC = new ArrayList<String>();

        if (dcArr != null) {
            for (File dc : dcArr) {
                DSLContainer dcContainer = (DSLContainer) DSLManager.getDCMappingInst(dc);
                boolean dcResult = DSLManager.validate(dcContainer);

                if (!dcResult) {
                    failedDC.add(dc.getName());
                }
                dqlResult &= dcResult;
            }
        }

        logger.info("DQL DC validation result is " + dqlResult);
        if (!dqlResult) {
            logger.info("Failed dc: " + failedDC);
        }
    }
}
