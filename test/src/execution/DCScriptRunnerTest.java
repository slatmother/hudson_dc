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

import org.apache.log4j.Logger;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 16.05.13</p>
 *
 * @version 1.0
 */
public class DCScriptRunnerTest {
    public static final Logger log = Logger.getRootLogger();

    public static final String QUERY_TEST_1 = "select user_os_name, user_privileges from dm_user where user_name = 'dmowner'";
    public static final String QUERY_TEST_2 = "select user_os_name, user_privileges from dm_user where user_name = '" + Math.random() * 100 + "'";
    public static final String QUERY_TEST_3 = "select title, owner_name  from dm_job where title = 'Docbase' enable(return_top 2)";
    public static final String QUERY_TEST_REPEATING = "select user_os_name, user_privileges from dm_user where user_name = '" + Math.random() * 100 + "'";


    public void runScript() {

    }
}
