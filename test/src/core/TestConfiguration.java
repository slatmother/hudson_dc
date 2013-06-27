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
package core;

import util.Configuration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

/**
 * $Id
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Author: g.alexeev (g.alexeev@i-teco.ru)</p>
 * <p>Date: 10.06.13</p>
 *
 * @version 1.0
 */
public class TestConfiguration {
    private Properties config_properties = new Properties();

    public Properties readConfig(String resource) throws IOException {
//        printClassPath();
        InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream(resource);

        if (inputStream == null) {
            throw new FileNotFoundException("property file " + resource + " not found in the classpath");
        }

        config_properties.load(inputStream);

        return config_properties;
    }

    public Properties getConfig_properties() {
        return config_properties;
    }

    public void printClassPath() {
        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader) cl).getURLs();

        for (URL url : urls) {
            System.out.println(url.getFile());
        }
    }
}
