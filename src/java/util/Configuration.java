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
package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 13.05.13</p>
 *
 * @version 1.0
 */
public class Configuration {
    private static final Properties config_properties = new Properties();

    static {
        try {
            InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream("config.properties");
            if (inputStream == null) {
                throw new FileNotFoundException("property file 'config.properties' not found in the classpath");
            }
            config_properties.load(inputStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Properties getConfig_properties() {
        return config_properties;
    }
}
