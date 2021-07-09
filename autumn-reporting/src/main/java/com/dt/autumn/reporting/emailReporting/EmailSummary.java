package com.dt.autumn.reporting.emailReporting;

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

import com.dt.autumn.reporting.ServerReporterPath;
import com.dt.autumn.reporting.extentReport.ExtentManager;
import com.dt.autumn.reporting.generics.ReportingGenericFunctions;
import com.dt.autumn.reporting.globals.GlobalVariables;
import com.dt.autumn.reporting.perfStatusReport.CreateAPIPerfReport;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class EmailSummary {

    public static String emailReportPath;
    private static String emailReportFileName = "Email.html";
    private static String emailReportTitle;
    private static String reportLoginUser;
    private static String reportLoginPassword;

    public static String getReportLoginUser() {
        return reportLoginUser;
    }

    public static String getReportLoginPassword() {
        return reportLoginPassword;
    }

    public static void setEmailReportTitle(String emailReportTitle) {
        EmailSummary.emailReportTitle = emailReportTitle;
    }

    public static void setReportLoginUser(String reportLoginUser) {
        EmailSummary.reportLoginUser = reportLoginUser;
    }

    public static void setReportLoginPassword(String reportLoginPassword) {
        EmailSummary.reportLoginPassword = reportLoginPassword;
    }

    public static void emailHtml() {
        File destFolderPath = new File(ServerReporterPath.getReportBaseDirectory() + "/EmailData");
        try {
            if (destFolderPath.mkdir()) {
            } else {
                FileUtils.forceDelete(destFolderPath);
                destFolderPath.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = null;
        try {
            file = new File(destFolderPath, emailReportFileName);
            file.createNewFile();
            emailReportPath = file.getAbsolutePath();
            int prodBugs = GlobalVariables.SUITE_TEST_STATUS_DTO.getProductionBugs();
            FileOutputStream consolidateHtmlFile = new FileOutputStream(file, true);
            PrintStream PrintHtml = new PrintStream(consolidateHtmlFile);
            PrintHtml.println("<html>");
            PrintHtml.println("<head>");
            PrintHtml.println("<title>" + emailReportTitle + "</title>");
            PrintHtml.println("</head>");
            PrintHtml.println("<body>");
            PrintHtml.println("<style>th {color:black;background-color:#abe;} td {background-color: #FAFAD2;}</style>");

            PrintHtml.println("<h1 style = 'color:black; font-size:150%; text-align:center;'> <u>" + emailReportTitle + " </h1></u>");
            for (String key : GlobalVariables.systemInfo.keySet()) {
                PrintHtml.println("<h3 style = 'color:black; font-size:120%; text-align:left;'> " + key + " :- "
                        + GlobalVariables.systemInfo.get(key) + "</h3>");
            }
            PrintHtml.println("<br><table border=\"0.5\" style='width:100%;'>");
            PrintHtml.println(
                    "<tr><th colspan=\"6\" style='color:black; background-color: #000000; color:#FFFFFF'>Complete Report Summary</th></tr>");
            if (prodBugs == 0) {
                PrintHtml.println(
                        "<tr align='center'><th>Total Test Cases </th><th>Passed Test Cases </th><th>Failed Test Cases</th><th>Skipped Test Cases</th>" +
                                "<th>Pass%</th></tr>");
                PrintHtml.println("<tr align='center'><td><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalTest()
                        + "</td><td style='color:#32CD32'><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass()
                        + "</td><td  style='color: #FF0000;'><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail()
                        + "</td><td><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalSkip()
                        + "</td><td style='color:#32CD32'><b>" + (GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass() * 100) / (GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass() + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail()) + "%"
                        + "</td></tr>");
                PrintHtml.println("</table>");
                PrintHtml.println("<br><table border=\"0.5\" style='width:100%;'>");
                PrintHtml.println(
                        "<tr><th colspan=\"6\" style='color:black; background-color: #000000; color:#FFFFFF'>MicroService Based Summary</th></tr>");
                PrintHtml.println(
                        "<tr align='center'><th>MicroService </th><th>Total Test Cases </th><th>Passed Test Cases </th><th>Failed Test Cases</th><th>Skipped Test Cases</th>" +
                                "<th>Pass%</th></tr>");
                for (String packageName : GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().keySet()) {
                    int totalTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalTest();
                    int passTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalPass();
                    int failTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalFail();
                    int skipTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalSkip();
                    PrintHtml.println("<tr align='center'><td><b>" + packageName
                            + "</td><td><b>" + totalTCs
                            + "</td><td style='color:#32CD32'><b>" + passTCs
                            + "</td><td  style='color: #FF0000;'><b>" + failTCs
                            + "</td><td><b>" + skipTCs
                            + "</td><td style='color:#32CD32'><b>" + (passTCs * 100) / (passTCs + failTCs) + "%"
                            + "</td></tr>");
                }
            } else {
                PrintHtml.println(
                        "<tr align='center'><th>Total Test Cases </th><th>Passed Test Cases </th><th>Failed Test Cases</th><th>Skipped Test Cases</th>" +
                                "<th>Production Bugs</th><th>Pass%</th></tr>");
                PrintHtml.println("<tr align='center'><td><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalTest()
                        + "</td><td style='color:#32CD32'><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass()
                        + "</td><td  style='color: #FF0000;'><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail()
                        + "</td><td><b>" + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalSkip()
                        + "</td><td><b>" + prodBugs
                        + "</td><td style='color:#32CD32'><b>" + (GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass() * 100) / (GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass() + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail() + prodBugs) + "%"
                        + "</td></tr>");
                PrintHtml.println("</table>");
                PrintHtml.println("<br><table border=\"0.5\" style='width:100%;'>");
                PrintHtml.println(
                        "<tr><th colspan=\"7\" style='color:black; background-color: #000000; color:#FFFFFF'>MicroService Based Summary</th></tr>");
                PrintHtml.println(
                        "<tr align='center'><th>MicroService </th><th>Total Test Cases </th><th>Passed Test Cases </th><th>Failed Test Cases</th><th>Skipped Test Cases</th><th>Production Bugs</th>" +
                                "<th>Pass%</th></tr>");
                for (String packageName : GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().keySet()) {
                    int totalTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalTest();
                    int passTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalPass();
                    int failTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalFail();
                    int skipTCs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getTotalSkip();
                    int packageProdBugs = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap().get(packageName).getProductionBugs();
                    PrintHtml.println("<tr align='center'><td><b>" + packageName
                            + "</td><td><b>" + totalTCs
                            + "</td><td style='color:#32CD32'><b>" + passTCs
                            + "</td><td  style='color: #FF0000;'><b>" + failTCs
                            + "</td><td><b>" + skipTCs
                            + "</td><td><b>" + packageProdBugs
                            + "</td><td style='color:#32CD32'><b>" + (passTCs * 100) / (passTCs + failTCs + packageProdBugs) + "%"
                            + "</td></tr>");
                }
            }

            PrintHtml.println("</table>");
            PrintHtml.println("<br><b><hr><label><font style='size: 4;' color='#000000' face='Tahoma'>"
                    + "Please follow the below links for detailed automation report, test data and logs: " + "</font></label>");
            PrintHtml.println("<br><a href=\"" + ServerReporterPath.getJenkinsReportPath() + "\">" + " Detailed Status Report" + "</a>");
            if(ExtentManager.getLoggingEnabled()) {
                PrintHtml.println("<br><a href=\"" + ServerReporterPath.getJenkinsLog() + "\">" + " Detailed Logs" + "</a>");
            }
            if(CreateAPIPerfReport.getApiPerfReport()) {
                PrintHtml.println("<br><a href=\"" + ServerReporterPath.getJenkinsAPIPerfReport() + "\">" + " Details Perf Status Report" + "</a>");
            }
            if(!ReportingGenericFunctions.getKlovServerUrl().equals(""))
                PrintHtml.println("<br><a href=\"" + ReportingGenericFunctions.getKlovServerUrl() + "\">" + " Klov Report (showing TCs trend over previous runs)" + "</a>");
            PrintHtml.println("<br>");
            if (reportLoginUser != null && !reportLoginUser.equalsIgnoreCase("")) {
                PrintHtml.println("<br>" + "Use below credentials to open automation report and logs:");
                PrintHtml.println("<br><i>UserName: " + reportLoginUser + "</i>");
                if (reportLoginPassword != null && !reportLoginPassword.equalsIgnoreCase("")) {
                    PrintHtml.println("<br><i>Password: " + reportLoginPassword + "</i>");
                }
            }
            PrintHtml.println("</body>");
            PrintHtml.println("</html>");
            PrintHtml.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
