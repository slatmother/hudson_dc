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
package dao;

import util.Configuration;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.SQLException;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.05.13</p>
 *
 * @version 1.0
 */
public class DatabaseHelperFactory {
    public static DatabaseHelper getClearQuestDAO() throws SQLException {
        String cq_user = Configuration.getConfig_properties().getProperty("cq.db.user");
        String cq_passwd = Configuration.getConfig_properties().getProperty("cq.db.password");
        String cq_url = Configuration.getConfig_properties().getProperty("cq.db.url");

        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(cq_user);
        dataSource.setPassword(cq_passwd);
        dataSource.setURL(cq_url);
        return new DatabaseHelper(dataSource);
    }

    public static DatabaseHelper getProjectDAO() throws SQLException {
        String project_db_user = Configuration.getConfig_properties().getProperty("project.db.user");
        String project_db_passwd = Configuration.getConfig_properties().getProperty("project.db.password");
        String project_db_url = Configuration.getConfig_properties().getProperty("project.db.url");

        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(project_db_user);
        dataSource.setPassword(project_db_passwd);
        dataSource.setURL(project_db_url);
        return new DatabaseHelper(dataSource);
    }

}
