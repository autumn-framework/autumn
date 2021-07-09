package com.dt.autumn.executors.testSetup;

import com.dt.autumn.reporting.listeners.AnnotationTransformer;
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
        //suite.addListener(TestNGReportListener.class.getName());
            suite.setParallel(XmlSuite.ParallelMode.valueOf(parallelMode));
            suite.addListener(AnnotationTransformer.class.getName());
        suite.setThreadCount(threadCount);
//        suite.setPreserveOrder(true);

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
                    packages.add(new XmlPackage(basePackage +".*"));
                    test.setPackages(packages);
                    break;

                case "package":
                    packages = new ArrayList<>();
                    if (gName == null) {
                        packages.add(new XmlPackage(basePackage +".*"));
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
                    packages.add(new XmlPackage(basePackage +".*"));
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
