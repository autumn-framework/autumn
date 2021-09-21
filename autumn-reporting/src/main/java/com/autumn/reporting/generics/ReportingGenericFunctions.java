package com.autumn.reporting.generics;

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

import com.autumn.reporting.listeners.RetryAnalyzer;
import com.autumn.reporting.ServerReporterPath;
import com.autumn.reporting.emailReporting.EmailSend;
import com.autumn.reporting.emailReporting.EmailSummary;
import com.autumn.reporting.extentReport.ExtentManager;
import com.autumn.reporting.globals.GlobalVariables;
import com.autumn.reporting.microsoftTeamsNotification.SendMessage;
import com.autumn.reporting.perfStatusReport.CreateAPIPerfReport;
import com.autumn.reporting.slackNotification.SendSlackMessage;

import java.util.Map;

public class ReportingGenericFunctions {

    private static Boolean moveToJenkinsLoc = false;
    private static String klovServerUrl="";

    public static String getKlovServerUrl() {
        return klovServerUrl;
    }

    public static void setKlovServerUrl(String klovServerUrl) {
        ReportingGenericFunctions.klovServerUrl = klovServerUrl;
    }

    public static void setSMTPDetails_Email(String smtpHost, String smtpPort, String senderEmailId, String senderEmailPassword, String sentMailToGroup){
        EmailSend.getInstance().setSmtpHost(smtpHost);
        EmailSend.getInstance().setSmtpPort(smtpPort);
        EmailSend.getInstance().setSenderEmailId(senderEmailId);
        EmailSend.getInstance().setSenderEmailPassword(senderEmailPassword);
        EmailSend.getInstance().setEmailSendToGroup(sentMailToGroup);
    }

    public static void initLogger(String logFileName){
        ExtentManager.setLoggingEnabled(true);
        ExtentManager.setLoggerName(logFileName);
    }


    public static void initEmailReport(String projectName, String environment, String emailReportTitle, String reportLoginUser, String reportLoginPassword) {
        EmailSummary.setEmailReportTitle(emailReportTitle);
        EmailSummary.setReportLoginUser(reportLoginUser);
        EmailSummary.setReportLoginPassword(reportLoginPassword);
        if (!moveToJenkinsLoc) {
            ServerReporterPath.createReportLinks();
            moveToJenkinsLoc = true;
        }
        EmailSummary.emailHtml();
        EmailSend.getInstance().sendEmailAfterTest(projectName, environment);
    }

    public static void initEmailReport(String projectName, String environment, String emailReportTitle) {
        EmailSummary.setEmailReportTitle(emailReportTitle);
        EmailSummary.setReportLoginUser(null);
        EmailSummary.setReportLoginPassword(null);
        if (!moveToJenkinsLoc) {
            ServerReporterPath.createReportLinks();
            moveToJenkinsLoc = true;
        }
        EmailSummary.emailHtml();
        EmailSend.getInstance().sendEmailAfterTest(projectName, environment);
    }

    public static void setReportLoc(String reportFolderName, String jenkinsReportLoc, String reportServerIp){
        ServerReporterPath.setReportServerIp(reportServerIp);
        ServerReporterPath.setJenkinsReportLoc(jenkinsReportLoc);
        ServerReporterPath.setReportFolderName(reportFolderName);
        ServerReporterPath.createDirectory();
    }

    public static void initAPIPerfReporter(String apiPerfReportFileName, String apiPerfReportTitle){
        CreateAPIPerfReport.setApiPerfReport(true);
        CreateAPIPerfReport.createHTMLReport(apiPerfReportFileName,apiPerfReportTitle);
    }

    public static void addExecutionDetails(Map<String, String> systemInfo) {
        GlobalVariables.systemInfo = systemInfo;
        GlobalVariables.systemInfo.put("ExecutionType", GlobalVariables.SUITETYPE);
        if (!GlobalVariables.SUITETYPE.equalsIgnoreCase("System"))
            GlobalVariables.systemInfo.put("Groups", GlobalVariables.TESTTYPE);
    }

    @Deprecated
    public static void initExtentReporter(boolean removeRetriedTests, boolean updateJIRA, boolean addScreenshotsToReport, String reportName, String reportTitle) {
        ExtentManager.createInstance(reportTitle, removeRetriedTests, addScreenshotsToReport, reportName);
    }

    public static void initExtentReporter(boolean removeRetriedTests, boolean addScreenshotsToReport, String reportName, String reportTitle) {
        ExtentManager.createInstance(reportTitle, removeRetriedTests, addScreenshotsToReport, reportName);
    }

    public static void initKlovReporter(String project, String serverURL, String mongoDbIP, int mongoDbPort) {
        setKlovServerUrl(serverURL);
        ExtentManager.startKlovReporter(project, serverURL, mongoDbIP, mongoDbPort);
    }

    public static void initMicrosoftTeamsNotification(String uri, String environment, String summary) {
        if (!moveToJenkinsLoc) {
            ServerReporterPath.createReportLinks();
            moveToJenkinsLoc = true;
        }
        SendMessage sendMessage = new SendMessage();
        String reportPath = ServerReporterPath.getJenkinsReportPath();
        sendMessage.createBody(summary, environment, reportPath,klovServerUrl);
        sendMessage.webHookPostMessage(uri);
    }

    public static void initSlackNotification(String uri, String environment, String summary) {
        if (!moveToJenkinsLoc) {
            ServerReporterPath.createReportLinks();
            moveToJenkinsLoc = true;
        }
        SendSlackMessage sendSlackMessage = new SendSlackMessage();
        String reportPath = ServerReporterPath.getJenkinsReportPath();
        sendSlackMessage.createBody(summary, environment, reportPath,klovServerUrl);
        sendSlackMessage.webHookPostMessage(uri);
    }

    public static void initRetryListener(int retryCount){
        RetryAnalyzer.setRetryLimit(retryCount);

    }


}
