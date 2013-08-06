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
package factory;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

import static org.junit.Assert.assertTrue;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 08.05.13</p>
 *
 * @version 1.0
 */
public class JdbcTemplateFactoryTest {
    public static final Logger log = Logger.getRootLogger();
    public static final String TEST_QUERY = "Select 1 from dual";

    @Test
    public void testCQTemplate() {
        try {
            JdbcTemplate cqTemplate = JdbcTemplateFactory.getClearQuestDBTemplate();
            Integer obj = cqTemplate.queryForObject(TEST_QUERY, Integer.class);
            assertTrue(obj == 1);
        } catch (SQLException e) {
            log.error(e);
        }
    }

    @Test
    public void testProjectTemplate() {
        try {
            JdbcTemplate template = JdbcTemplateFactory.getProjectDBTemplate();
            Integer obj = template.queryForObject(TEST_QUERY, Integer.class);
            assertTrue(obj == 1);
        } catch (SQLException e) {
            log.error(e);
        }

    }
}
