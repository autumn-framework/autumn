package com.autumn.reporting;

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

import com.autumn.reporting.extentReport.ExtentManager;
import com.autumn.reporting.perfStatusReport.CreateAPIPerfReport;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ServerReporterPath {
	private static File reportFolderPath;
	private static String jenkinsReport,jenkinsLog,jenkinsAPIPerfReport;
	private static String reportBaseDirectory;
	private static String reportServerIp;
	private static String reportFolderName;
	private static String jenkinsReportLoc;

	public static void setReportServerIp(String reportServerIp) {
		ServerReporterPath.reportServerIp = reportServerIp;
	}

	public static String getReportServerIp() {
		return reportServerIp;
	}

	public static void setReportFolderName(String reportFolderName) {
		ServerReporterPath.reportFolderName = reportFolderName;
	}

	public static void setJenkinsReportLoc(String jenkinsReportLoc) {
		ServerReporterPath.jenkinsReportLoc = jenkinsReportLoc;
	}

	public static String getJenkinsReportLoc() {
		return jenkinsReportLoc;
	}

	public static String getJenkinsReportPath(){
		return ServerReporterPath.jenkinsReport;
	}

	public static String getJenkinsAPIPerfReport() {
		return jenkinsAPIPerfReport;
	}

	public static String getJenkinsLog(){
		return ServerReporterPath.jenkinsLog;
	}

	public static void createDirectory(){
		reportFolderName = reportFolderName + "-" + getCurrentDateTime("dd-MMM-HH-mm-ss");
		reportFolderPath = new File(jenkinsReportLoc + reportFolderName );
		reportFolderPath.mkdir();
		setReportBaseDirectory(reportFolderPath.getAbsolutePath()+"/");

	}

	public static void createReportLinks(){
		jenkinsReport = reportServerIp + reportFolderName + "/" + ExtentManager.getReportName() ;
		System.out.println("Extent Report Location :- " + jenkinsReport);
		jenkinsAPIPerfReport = reportServerIp + reportFolderName + "/" + CreateAPIPerfReport.getApiPerfReportFileName();
		System.out.println("API Performance Report Location :- " + jenkinsAPIPerfReport);
		jenkinsLog = reportServerIp + reportFolderName + "/" + ExtentManager.getLoggerName() + ".log" ;
		System.out.println("Log File Location :- " + jenkinsLog);
	}

	public static void moveLogFileToReportDirectory() {
		try {
			File file = new File(ServerReporterPath.getReportBaseDirectory());
			File log4jReportFileName = file.createTempFile(ExtentManager.getLoggerName(), ".log", file);
			File sourceFile = new File(System.getProperty("user.dir"), "loggerFile.log");
			if (sourceFile.exists()) {
				FileUtils.copyFile(sourceFile, log4jReportFileName);
				File log4JFile = new File(ServerReporterPath.getReportBaseDirectory() + "/" + ExtentManager.getLoggerName()+".log");
				log4jReportFileName.renameTo(log4JFile);
				sourceFile.delete();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized static String getReportBaseDirectory() {
		return reportBaseDirectory;
	}

	public synchronized static void setReportBaseDirectory(String reportBaseDirectory) {
		ServerReporterPath.reportBaseDirectory = reportBaseDirectory;
	}

	public static String getCurrentDateTime(String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		String currentDateTime = dateFormat.format(cal.getTime());
		return currentDateTime;
	}


}
