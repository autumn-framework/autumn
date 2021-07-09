package com.dt.autumn.utils.readWriteUtil;

/*-
 * #%L
 * autumn-utils
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.dt.autumn.reporting.extentReport.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * Utility class provides the methods to read write the property files.
 */
public class PropertyUtils {

    private static PropertyUtils prop;
    private Properties properties;

    private PropertyUtils() {
        properties = new Properties();
    }

    /**
     * Creates a new property file object if already not exist and returns it.
     *
     * @return The property file object.
     */
    public static synchronized PropertyUtils getInstance() {
        if (prop == null) {
            prop = new PropertyUtils();
        }
        return prop;
    }

    /**
     * Creates a new property file object if already not exist and returns it.
     *
     * @return The property file object.
     */
    public static synchronized PropertyUtils getProperties() {
        PropertyUtils prop = new PropertyUtils();
        return prop;
    }



    /**
     * Loads the specified property file to the InputStream.
     *
     * @param fileName
     *            Exact filename of the property file.
     */
    public Properties loadProperties(String fileName) {
        InputStream input;
        input = getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }



    /**
     * Loads the specified property file to the InputStream.
     *
     * @param fileName
     *            Exact filename of the property file.
     */
    public void load(String fileName) {
        InputStream input;
        Logger.logInfoInLogger("File name in CSV is "+fileName);

        input = getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the specified property file to the InputStream.
     *
     * @param file
     *            The file object property file.
     */
    public void load(File file) {
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Clears this Properties object so that it contains no keys.
     */
    public void clear() {
        properties.clear();
    }

    /**
     * Reads from the property file and provides the values of specified key.
     *
     * @param key
     *            The key whose value is required to be read.
     * @return The value of specified key.
     */
    public String getValue(String key) {
        return properties.getProperty(key).trim();
    }

    /**
     * Reads from the property file and provides the values of specified key, but if
     * that key is not present then it will return the default value.
     *
     * @param key
     *            The key whose value is required to be read.
     * @param defaultValue
     *            a defaultValue of the specified key if that key is not present or
     * @return The value of specified key or the default value.
     */
    public String getValue(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue).trim();
    }

    /**
     * Sets the value of specified key in the property file.
     *
     * @param key
     *            The key whose values is required to be set.
     * @param value
     *            The value that needed to be set for the specified key.
     */
    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Returns the keyset of the property file.
     */
    public Set<Object> getKeySet() {
        return properties.keySet();
    }

}
