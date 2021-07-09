package com.dt.autumn.reporting.extentReport;

/*-
 * #%L
 * autumn-reporting
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

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentKlovReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.aventstack.extentreports.reporter.configuration.ViewName;
import com.dt.autumn.reporting.ServerReporterPath;
import com.dt.autumn.reporting.dataObjects.TCLevelInfoDTO;
import com.dt.autumn.reporting.globals.GlobalVariables;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

public class ExtentManager {

    private static ExtentKlovReporter klovReporter = null;
    private static ExtentReports extent = null;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static boolean removeRetriedTests;
    private static boolean addScreenshotsToReport;
    private static final Logger logger = LogManager.getLogger();
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<LinkedList<String>> logTracking = new ThreadLocal<>();
    private static ThreadLocal<LinkedList<Markup>> extentTestBefore = new ThreadLocal<>();
    private static String extentReportLocation;
    private static final String OUTPUT_FOLDER_SCREENSHOTS = "screenshots/";
    private static String reportName = "AutomationSuiteReport";
    private static Boolean loggingEnabled = true;
    private static String loggerName = "LoggerFile";

    public ExtentManager() {
    }

    public static ThreadLocal<LinkedList<Markup>> getExtentTestBefore() {
        return extentTestBefore;
    }

    public static void setExtentTestBefore(Markup log) {
        if (extentTestBefore.get() == null) {
            LinkedList<Markup> logList = new LinkedList<>();
            getExtentTestBefore().set(logList);
        }
        getExtentTestBefore().get().add(log);
    }

    public static Boolean getLoggingEnabled() {
        return loggingEnabled;
    }

    public static void setLoggingEnabled(Boolean loggingEnabled) {
        ExtentManager.loggingEnabled = loggingEnabled;
    }

    public static String getLoggerName() {
        return loggerName;
    }

    public static void setLoggerName(String loggerName) {
        ExtentManager.loggerName = loggerName;
    }

    public static String getReportName() {
        return reportName;
    }

    public static void setReportName(String reportName) {
        ExtentManager.reportName = reportName;
    }

    public synchronized static ThreadLocal<ExtentTest> getTest() {
        return test;
    }

    public synchronized static ThreadLocal<LinkedList<String>> getLog() {
        return logTracking;
    }

    public synchronized static Logger getLogger() {
        return logger;
    }

    public synchronized static void setTest(ExtentTest test) {
        getTest().set(test);
    }

    public synchronized static void setLog() {
        LinkedList<String> logs = new LinkedList<>();
        getLog().set(logs);
    }

    public static String getExtentReportLocation() {
        return extentReportLocation;
    }

    public static void setExtentReportLocation(String extentReportLocation) {
        ExtentManager.extentReportLocation = extentReportLocation;
    }

    public synchronized static ExtentReports createInstance(String documentTitle, boolean removeRetriedTests, boolean addScreenshotsToReport, String reportName) {
        setReportName(reportName);
        if (ServerReporterPath.getReportBaseDirectory() == null)
            ServerReporterPath.setReportBaseDirectory(GlobalVariables.REPORT_BASE_PACKAGE + "-" + ServerReporterPath.getCurrentDateTime("dd-MMM-HH-mm-ss") + "/");
        setRemoveRetriedTests(removeRetriedTests);
        setAddScreenshotsToReport(addScreenshotsToReport);
        setExtentReportLocation(ServerReporterPath.getReportBaseDirectory() + reportName + ".html");
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(getExtentReportLocation());
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setReportName(documentTitle);
        sparkReporter.config().setTimelineEnabled(true);
        sparkReporter.config().setDocumentTitle(documentTitle);
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setTimeStampFormat("MM/dd/yyyy, hh:mm:ss a '('zzz')'");
        sparkReporter.viewConfigurer()
                .viewOrder()
                .as(new ViewName[]{ViewName.DASHBOARD, ViewName.TEST, ViewName.AUTHOR, ViewName.CATEGORY, ViewName.EXCEPTION, ViewName.LOG})
                .apply();
        extent = new ExtentReports();
//        extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
        extent.attachReporter(sparkReporter);
        if (klovReporter != null)
            extent.attachReporter(klovReporter);
        return extent;
    }

    private static synchronized void writeLog4j() {
        if(getLoggingEnabled()) {
            if (logTracking.get() != null) {
                for (String message : logTracking.get()) {
                    logger.info(message);
                }
            }
        }
    }

    public synchronized static void deleteCurrentTest() {
        try {
            extent.removeTest(getTest().get());
        } catch (Exception e) {
            com.dt.autumn.reporting.extentReport.Logger.logInfoInLogger("XXXXX Unable to Delete Extent Test XXXXX");
        }
    }

    public synchronized static void flush() {
        extent.flush();
        writeLog4j();
        if (getTest().get() != null) {
            getTest().remove();
        }
        if (getLog().get() != null) {
            getLog().remove();
        }
    }

    public synchronized static void setPassPercentage(String value) {
        extent.setSystemInfo("Pass %", MarkupHelper.createLabel(value, ExtentColor.GREEN).getMarkup());
        flush();
    }

    public synchronized static void addSystemInfo(String key, String value) {
        extent.setSystemInfo(key, value);
        flush();
    }

    public synchronized static void setTestRunnerOutput(String log) {
        extent.addTestRunnerOutput(log);
    }

    public synchronized static void createTest(String testName, String description) {
        setTest(extent.createTest(testName, description));
        setLog();
        System.out.println("************" + testName + "************");
        logTracking.get().add("************" + testName + "************");
        if (getExtentTestBefore().get() != null && getExtentTestBefore().get().size() > 0) {
            for (Markup log : getExtentTestBefore().get()) {
                getTest().get().log(Status.INFO, log);
            }
            getExtentTestBefore().remove();
        }
    }

    public synchronized static boolean isRemoveRetriedTests() {
        return removeRetriedTests;
    }

    public synchronized static void setRemoveRetriedTests(boolean removeRetriedTests) {
        ExtentManager.removeRetriedTests = removeRetriedTests;
    }

    public synchronized static boolean isAddScreenshotsToReport() {
        return addScreenshotsToReport;
    }

    public synchronized static void setAddScreenshotsToReport(boolean addScreenshotsToReport) {
        ExtentManager.addScreenshotsToReport = addScreenshotsToReport;
    }

    public synchronized static WebDriver getDriver() {
        return ExtentManager.driver.get();
    }

    public synchronized static void setDriver(WebDriver driver) {
        ExtentManager.driver.set(driver);
    }

    public synchronized static void addGroupNamesSystemInfo(String key, String value) {
        ExtentManager.addSystemInfo(key, getFomattedGroupNames(value));
        flush();
    }

    public static synchronized void startKlovReporter(String projectName, String serverURL, String mongoDbIP, int mongoDbPort) {
        klovReporter = new ExtentKlovReporter(projectName, ServerReporterPath.getCurrentDateTime("E, dd MMM yyyy hh:mm:ss a"));
        klovReporter.initMongoDbConnection(mongoDbIP, mongoDbPort);
        klovReporter.initKlovServerConnection(serverURL);
    }

    private synchronized static String getFomattedGroupNames(String groupNames) {
        String finalStr = "";
        for (String str : groupNames.split(",")) {
            if (str.contains(".")) {
                finalStr = finalStr + str.split("\\.")[1] + "<br>";
            } else {
                finalStr = finalStr + str + "<br>";
            }
        }
        return finalStr;
    }

    private synchronized static String takeScreenshot(WebDriver driver, String methodName) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss_SSS");
        Date date = new Date();
        String dateName = dateFormat.format(date);
        String filePath="";
        String filePathExtent = OUTPUT_FOLDER_SCREENSHOTS + methodName + "_" + dateName + ".png";
        if(Boolean.parseBoolean(System.getProperty("JenkinsRun"))){
            filePath=ServerReporterPath.getJenkinsReportLoc()+filePathExtent;
        }
        else{
            filePath = "./" + ServerReporterPath.getReportBaseDirectory() + filePathExtent;
        }
        File f = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(f, new File(filePath));
        return filePathExtent;
    }

    public synchronized static void addScreenShotToReport(TCLevelInfoDTO tcLevelInfoDTO) {
        if (ExtentManager.isAddScreenshotsToReport()) {
            try {
                String screenshotPath="";
                if(Boolean.parseBoolean(System.getProperty("JenkinsRun"))){
                    screenshotPath=ServerReporterPath.getReportServerIp()+takeScreenshot(ExtentManager.getDriver(), tcLevelInfoDTO.getMethodName());
                }
                else{
                    screenshotPath=Paths.get("./" + takeScreenshot(ExtentManager.getDriver(), tcLevelInfoDTO.getMethodName())).toString();
                }
                ExtentManager.getTest().get().addScreenCaptureFromPath(screenshotPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
