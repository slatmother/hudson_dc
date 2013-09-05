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

import com.documentum.fc.common.DfException;
import constants.IConstants;
import groovy.execution.DSLManager;
import groovy.container.DSLContainer;
import transaction.script.ClearQuestTrScript;
import org.apache.log4j.Logger;
import util.Checker;
import util.Configuration;
import util.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
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
public class LocationService {
    private static final Logger logger = Logger.getRootLogger();

    /**
     *
     * @param location
     * @param dcMap
     * @return
     * @throws SQLException
     * @throws DfException
     */
    public static Map<DSLContainer, Object> performModification(String location, Map<DSLContainer, Object> dcMap) throws SQLException, DfException {
        if (IConstants.MainArgsTypes.Location.CQ.equalsIgnoreCase(location)) {
            return getDCFromClearQuest(dcMap);
        } else if (IConstants.MainArgsTypes.Location.CUSTOM.equalsIgnoreCase(location)) {
            return getDCFromCustomFolder(dcMap);
        } else {
            return Collections.emptyMap();
        }
    }

    /*
     *  TODO: hardcode to IConstants
     * @param dcMap
     * @return
     * @throws DfException
     * @throws SQLException
     */
    private static Map<DSLContainer, Object> getDCFromClearQuest(Map<DSLContainer, Object> dcMap) throws DfException, SQLException {
        ClearQuestTrScript dcLocator = new ClearQuestTrScript();

        String projectName = Configuration.getConfig_properties().getProperty("cq.project.name");
        HashMap<String, String> dcNameMap = dcLocator.getAllBuildDefChanges(projectName);

        for (String dc : dcNameMap.keySet()) {
            String headline = dcNameMap.get(dc);

            File scriptFile = new File("./dc/" + dc + ".dc");
            logger.info(scriptFile.getName());

            Checker.checkFileExistsOrIsFile(scriptFile);
            if (Utils.isNotNull(headline) && headline.contains("dql")) {
                dcMap.put((DSLContainer) DSLManager.getDCMappingInst(scriptFile), "dql");

            } else if (Utils.isNotNull(headline) && headline.contains("sql")) {
                dcMap.put((DSLContainer) DSLManager.getDCMappingInst(scriptFile), "sql");
            }
        }

        return dcMap;
    }

    /*
     *  TODO: hardcode to IConstants
     * @param dcMap
     * @return
     * @throws DfException
     * @throws SQLException
     */
    private static Map<DSLContainer, Object> getDCFromCustomFolder(Map<DSLContainer, Object> dcMap) throws DfException, SQLException {
        logger.info("Start filling dc map from custom folder");

        final FilenameFilter dcFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".dc");
            }
        };

        File customDCDir = new File("./dc/custom");
        logger.info("Custom folder has path " + customDCDir.getAbsolutePath() + " Exists? "  + customDCDir.exists());

        if (customDCDir.isDirectory()) {
            for (File scriptFile : customDCDir.listFiles(dcFilter)) {
                logger.info("DC file name: " + scriptFile.getName());

                Checker.checkFileExistsOrIsFile(scriptFile);
                DSLContainer container = (DSLContainer) DSLManager.getDCMappingInst(scriptFile);
                dcMap.put(container, container.getDcQueryType());
            }
        }

        return dcMap;
    }
}
