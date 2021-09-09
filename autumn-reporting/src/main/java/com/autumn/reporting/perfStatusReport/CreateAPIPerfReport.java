package com.autumn.reporting.perfStatusReport;

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

import com.autumn.reporting.ServerReporterPath;
import com.autumn.reporting.globals.GlobalVariables;
import com.autumn.reporting.dataObjects.APIStatusTimeDTO;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class CreateAPIPerfReport {

    public static String APIPerfReportPath;
    private static Boolean apiPerfReport = false;
    private static String apiPerfReportFileName = "PerformanceSuiteReport";


    public static void setApiPerfReport(Boolean apiPerfReport) {
        CreateAPIPerfReport.apiPerfReport = apiPerfReport;
    }

    public static Boolean getApiPerfReport() {
        return apiPerfReport;
    }

    public static String getAPIPerfReportPath() {
        return APIPerfReportPath;
    }

    public static String getApiPerfReportFileName() {
        return apiPerfReportFileName;
    }

    public static void setApiPerfReportFileName(String apiPerfReportFileName) {
        CreateAPIPerfReport.apiPerfReportFileName = apiPerfReportFileName;
    }

    public static void createHTMLReport(String apiPerfReportFileName, String apiPerfReportTitle) {
        setApiPerfReportFileName(apiPerfReportFileName);
        File destFolderPath = new File(ServerReporterPath.getReportBaseDirectory() + "/APIPerf");
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
            file = new File(destFolderPath, apiPerfReportFileName);
            file.createNewFile();
            APIPerfReportPath = file.getAbsolutePath();
            FileOutputStream consolidateHtmlFile = new FileOutputStream(file, true);
            PrintStream PrintHtml = new PrintStream(consolidateHtmlFile);
            PrintHtml.println("<html>");
            PrintHtml.println("<head>");
            PrintHtml.println("<title>" + apiPerfReportTitle + "</title>");
            PrintHtml.println("</head>");
            PrintHtml.println("<body>");
            PrintHtml.println("<style>th {color:black;background-color:#abe;} td {background-color: #FAFAD2;}</style>");

            PrintHtml.println("<h1 style = 'color:black; font-size:150%; text-align:center;'> <u>" + apiPerfReportTitle + " </h1></u>");
            for (String key : GlobalVariables.systemInfo.keySet()) {
                PrintHtml.println("<h3 style = 'color:black; font-size:120%; text-align:left;'> " + key + " :- "
                        + GlobalVariables.systemInfo.get(key) + "</h3>");
            }
            PrintHtml.println("<br><table border=\"0.5\" style='width:100%;'>");
            PrintHtml.println(
                    "<tr><th colspan=\"6\" style='color:black; background-color: #000000; color:#FFFFFF'>MicroService Based Summary</th></tr>");
            PrintHtml.println(
                    "<tr align='center'><th>API Base Path </th><th>Total Number of Hits </th><th>Average Response Time </th><th>Max Response Time</th><th>Minimum Response Time</th>" +
                            "</tr>");
            for (String apiBasePath : GlobalVariables.API_STATUS_TIME_DTOMAP.keySet()) {
                Long totalHits = GlobalVariables.API_STATUS_TIME_DTOMAP.get(apiBasePath).getTotalHits();
                Long averageResponseTime = GlobalVariables.API_STATUS_TIME_DTOMAP.get(apiBasePath).getAverageResponseTime();
                Long maxTime = GlobalVariables.API_STATUS_TIME_DTOMAP.get(apiBasePath).getMaxTime();
                Long minTime = GlobalVariables.API_STATUS_TIME_DTOMAP.get(apiBasePath).getMinTime();
                PrintHtml.println("<tr align='center'><td><b>" + apiBasePath
                        + "</td><td><b>" + totalHits
                        + "</td><td><b>" + averageResponseTime
                        + "</td><td><b>" + maxTime
                        + "</td><td><b>" + minTime
                        + "</td></tr>");
            }
            PrintHtml.println("</table>");
            PrintHtml.println("<br>");
            PrintHtml.println("</body>");
            PrintHtml.println("</html>");
            PrintHtml.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureAPIStatusTimeDTO(String basePath, Long timeInMilliSeconds) {
        if (GlobalVariables.API_STATUS_TIME_DTOMAP.containsKey(basePath)) {
            APIStatusTimeDTO apiStatusTimeDTO = GlobalVariables.API_STATUS_TIME_DTOMAP.get(basePath);
            apiStatusTimeDTO.setResponseTime(timeInMilliSeconds);
        } else {
            APIStatusTimeDTO apiStatusTimeDTO = new APIStatusTimeDTO(basePath);
            apiStatusTimeDTO.setResponseTime(timeInMilliSeconds);
            GlobalVariables.API_STATUS_TIME_DTOMAP.put(basePath, apiStatusTimeDTO);
        }

    }


}
