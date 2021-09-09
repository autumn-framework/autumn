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
	private static File AutomationReportFileName,LogFileName,APIPerfReportFileName;
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

	public static void moveReportToJenkins(){
		String folderName = reportFolderName + "-" + getCurrentDateTime("dd-MMM-HH-mm-ss");
		reportFolderPath = new File(jenkinsReportLoc + folderName );
		reportFolderPath.mkdir();
		moveAutomationReportInServer();
		moveLogInServer();
		if(CreateAPIPerfReport.getApiPerfReport())
			movePerformanceStatusReport();
		File sourceFolder = new File(ServerReporterPath.getReportBaseDirectory());
		sourceFolder.delete();
		jenkinsReport = reportServerIp + folderName + "/" + ExtentManager.getReportName()+".html";
		jenkinsAPIPerfReport = reportServerIp + folderName + "/" + CreateAPIPerfReport.getApiPerfReportFileName()+".html";
		jenkinsLog=reportServerIp+folderName+"/"+ExtentManager.getLoggerName()+".log";

	}

	private static void moveAutomationReportInServer(){
		try{

			File file = new File(reportFolderPath.getPath());
			AutomationReportFileName = file.createTempFile(ExtentManager.getReportName(), ".html", file);
			File sourceFile = new File(ExtentManager.getExtentReportLocation());
			if (sourceFile.exists()) {
				FileUtils.copyFile(sourceFile, AutomationReportFileName);
				File AutomationReportFile = new File(reportFolderPath.getPath() + "/"+ExtentManager.getReportName()+".html");
				AutomationReportFileName.renameTo(AutomationReportFile);
				if (sourceFile.exists())
					sourceFile.delete();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void movePerformanceStatusReport(){
		try{
			File file = new File(reportFolderPath.getPath());
			APIPerfReportFileName = file.createTempFile(CreateAPIPerfReport.getApiPerfReportFileName(), ".html", file);
			File sourceFile = new File(CreateAPIPerfReport.getAPIPerfReportPath());
			if (sourceFile.exists()) {
				FileUtils.copyFile(sourceFile, APIPerfReportFileName);
				File APIPerfReportFile = new File(reportFolderPath.getPath() + "/"+CreateAPIPerfReport.getApiPerfReportFileName()+".html");
				APIPerfReportFileName.renameTo(APIPerfReportFile);
				if (sourceFile.exists())
					sourceFile.delete();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void moveLogInServer(){
		try{
			File file = new File(reportFolderPath.getPath());
			LogFileName = file.createTempFile(ExtentManager.getLoggerName(), ".log", file);
			File sourceFile = new File(ServerReporterPath.getReportBaseDirectory(), ExtentManager.getLoggerName()+".log");
			if (sourceFile.exists()) {
				FileUtils.copyFile(sourceFile, LogFileName);
				File LogFile = new File(reportFolderPath.getPath() + "/"+ExtentManager.getLoggerName()+".log");
				LogFileName.renameTo(LogFile);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
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
