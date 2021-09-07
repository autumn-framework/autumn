package com.dt.autumn.executors.testSetup;

/*-
 * #%L
 * autumn-executors
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

import com.dt.autumn.reporting.listeners.RetryListener;
import org.testng.xml.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateTestNGXml {

    private static final String FILENAME_TESTNGXML = "testng.xml";


    static void createXml(String type, String gName, int threadCount, String basePackage, String parallelMode) throws Exception {
        // Create Suite
        XmlSuite suite = new XmlSuite();
        suite.setName("Test Suite");
        suite.setVerbose(10);
        suite.setParallel(XmlSuite.ParallelMode.valueOf(parallelMode));
        suite.addListener(RetryListener.class.getName());
        suite.setThreadCount(threadCount);

        // Add Test Suite
        XmlTest test = new XmlTest(suite);
        test.setName("Test Suite");
        test.setThreadCount(threadCount);
        test.setParallel(XmlSuite.ParallelMode.valueOf(parallelMode));
        List<XmlPackage> packages;
        List<XmlClass> classes;

        switch (type) {
            case "system":
                packages = new ArrayList<>();
                packages.add(new XmlPackage(basePackage + ".*"));
                test.setPackages(packages);
                break;

            case "package":
                packages = new ArrayList<>();
                if (gName == null) {
                    packages.add(new XmlPackage(basePackage + ".*"));
                } else {
                    String[] packageArr = gName.split(",");
                    for (String s : packageArr) {
                        packages.add(new XmlPackage(basePackage + "." + s));
                    }
                }
                test.setPackages(packages);
                break;

            case "group":
                packages = new ArrayList<>();
                packages.add(new XmlPackage(basePackage + ".*"));
                test.setPackages(packages);
                String[] groups = gName.split(",");
                test.setIncludedGroups(new ArrayList<>(Arrays.asList(groups)));
                break;

            case "class":
                classes = new ArrayList<>();
                String[] classArr = gName.split(",");
                for (String s : classArr) {
                    try {
                        XmlClass xmlClass = new XmlClass();
                        xmlClass.setName(basePackage + "." + s);
                        classes.add(xmlClass);
                    } catch (Exception e) {

                    }
                }
                test.setClasses(classes);
                break;

            case "method":
                classes = new ArrayList<>();
                String[] pair = gName.split(":");
                XmlClass testClass = new XmlClass();
                testClass.setName(basePackage + "." + pair[0]);
                List<XmlInclude> methods = new ArrayList<>();
                methods.add(new XmlInclude(pair[1]));
                testClass.setIncludedMethods(methods);
                classes.add(testClass);
                test.setClasses(classes);
                break;
            default:
                break;
        }

        FileWriter writer = new FileWriter(new File(FILENAME_TESTNGXML));
        writer.write(suite.toXml());
        writer.flush();
        writer.close();

        System.out.println("--------- Created TestNG XML ----------");
    }

}
