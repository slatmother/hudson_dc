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
package locator;

import database.DBHelper;
import database.DBHelperFactory;
import org.apache.log4j.Logger;
import util.Checker;
import util.Utils;
import util.querytemplate.QueryTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static util.querytemplate.QueryTemplate.queryTemplate;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 07.05.13</p>
 *
 * @version 1.0
 */
public class BuildDCLocator {
    private static final Logger logger = Logger.getRootLogger();
    public final String HEADLINE = "headline";
    public final String DCNUMBER = "dcnumber";

    private static QueryTemplate dcQueryTemplate = queryTemplate(
            "select dcnumber, headline from dc where dbid in " +
                    "(select child_dbid from parent_child_links where child_entitydef_id in " +
                    "(select id from entitydef where name = 'DC') " +
                    "and parent_dbid in (select first_value(bb.dbid) over (order by bb.id desc) from build bb where bb.id is not null " +
                    "and bb.project in (select dbid from project where name = '${project}'))) " +
                    "and (headline like '%dql%' or headline like '%sql%') " +
                    "order by dbid asc",
            false
    );

    public HashMap<String, String> getLastBuildSQLDefChanges(String projectName) throws SQLException {
        Checker.checkStringForEmpty("project name", projectName, false);

        String query = dcQueryTemplate.substitute(
                "project", projectName
        );

        DBHelper cqConnection = DBHelperFactory.getClearQuestHelper();
        HashMap<String, String> sqlMap = new HashMap<String, String>();

        ResultSet set = cqConnection.executeStatementWithoutCommit(query);
        while (set.next()) {
            String dcHeadline = set.getString(HEADLINE);
            if (dcHeadline.contains("sql")) {
                sqlMap.put(set.getString(DCNUMBER), dcHeadline);
            }
        }

        return sqlMap;
    }

    public HashMap<String, String> getLastBuildDQLDefChanges(String projectName) throws SQLException {
        Checker.checkStringForEmpty("project name", projectName, false);

        String query = dcQueryTemplate.substitute(
                "project", projectName
        );

        DBHelper cqConnection = DBHelperFactory.getClearQuestHelper();
        HashMap<String, String> dqlmap = new HashMap<String, String>();

        ResultSet set = cqConnection.executeStatementWithoutCommit(query);
        while (set.next()) {
            String dcHeadline = set.getString(HEADLINE);
            if (dcHeadline.contains("dql")) {
                dqlmap.put(set.getString(DCNUMBER), dcHeadline);
            }
        }

        return dqlmap;
    }

    public HashMap<String, String> getAllBuildDefChanges(String projectName) throws SQLException {
        Checker.checkStringForEmpty("project name", projectName, false);

        String query = dcQueryTemplate.substitute(
                "project", projectName
        );

        DBHelper helper = DBHelperFactory.getClearQuestHelper();
        logger.info("database helper is connected? " + helper.getConnection().isValid(10));

        HashMap<String, String> dcMap = new HashMap<String, String>();
        ResultSet set = null;

        try {
            logger.info("query \n" + query);

            set = helper.executeStatementWithoutCommit(query);
            logger.info("resultset rows count " + set.getRow());

            while (set.next()) {
                logger.info("resultset rows count " + set.getRow());

                dcMap.put(set.getString(DCNUMBER), set.getString(HEADLINE));
            }
        } finally {
            Utils.closeResources(set);
        }

        return dcMap;
    }
}

