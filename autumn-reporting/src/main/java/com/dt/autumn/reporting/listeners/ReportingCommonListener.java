package com.dt.autumn.reporting.listeners;

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.dt.autumn.reporting.ServerReporterPath;
import com.dt.autumn.reporting.dataObjects.ClassTestStatusDTO;
import com.dt.autumn.reporting.dataObjects.PackageTestStatusDTO;
import com.dt.autumn.reporting.dataObjects.TCLevelInfoDTO;
import com.dt.autumn.reporting.enums.TCStatus;
import com.dt.autumn.reporting.extentReport.ExtentManager;
import com.dt.autumn.reporting.extentReport.Logger;
import com.dt.autumn.reporting.globals.GlobalVariables;
import org.testng.Reporter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReportingCommonListener {

    public synchronized void logFinishSuite() {
        addExecutionDetails_Extent(GlobalVariables.systemInfo);
        addConsoleLogsToReport();
        ServerReporterPath.moveLogFileToReportDirectory();
    }

    public synchronized void logStartTest(TCLevelInfoDTO tcLevelInfoDTO) {
        ExtentManager.createTest(getMethodNameWithParams(tcLevelInfoDTO), getTestDescription(tcLevelInfoDTO));
        addParametersInReport(tcLevelInfoDTO);
    }

    public synchronized void logProductionBug(TCLevelInfoDTO tcLevelInfoDTO) {
        logStatusToConsole(tcLevelInfoDTO);
        assignCategoryToTest(tcLevelInfoDTO);
        assignExceptionToTest(tcLevelInfoDTO);
        ExtentManager.addScreenShotToReport(tcLevelInfoDTO);
        addExtentLabelToTest(tcLevelInfoDTO);
        updateTestStatusInSuiteDTO(tcLevelInfoDTO);
        ExtentManager.flush();
    }

    public synchronized void logFailedTest(TCLevelInfoDTO tcLevelInfoDTO) {
        logStatusToConsole(tcLevelInfoDTO);
        assignCategoryToTest(tcLevelInfoDTO);
        assignExceptionToTest(tcLevelInfoDTO);
        ExtentManager.addScreenShotToReport(tcLevelInfoDTO);
        addExtentLabelToTest(tcLevelInfoDTO);
        updateTestStatusInSuiteDTO(tcLevelInfoDTO);
        ExtentManager.flush();
    }

    public synchronized void logSkippedTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (ExtentManager.isRemoveRetriedTests()) {
            removeRetriedTest(tcLevelInfoDTO);
        } else {
            logStatusToConsole(tcLevelInfoDTO);
            assignCategoryToTest(tcLevelInfoDTO);
            assignExceptionToTest(tcLevelInfoDTO);
            ExtentManager.addScreenShotToReport(tcLevelInfoDTO);
            addExtentLabelToTest(tcLevelInfoDTO);
            updateTestStatusInSuiteDTO(tcLevelInfoDTO);
            ExtentManager.flush();
        }
    }

    public synchronized void logSuccessTest(TCLevelInfoDTO tcLevelInfoDTO) {
        logStatusToConsole(tcLevelInfoDTO);
        assignCategoryToTest(tcLevelInfoDTO);
        addExtentLabelToTest(tcLevelInfoDTO);
        updateTestStatusInSuiteDTO(tcLevelInfoDTO);
        ExtentManager.flush();
    }


    private void addExecutionDetails_Extent(Map<String, String> executionProperties) {
        for (String key : executionProperties.keySet()) {
            ExtentManager.addSystemInfo(key, executionProperties.get(key));
        }
    }

    protected synchronized void addConsoleLogsToReport() {
        for (String s : Reporter.getOutput()) {
            ExtentManager.setTestRunnerOutput(s + System.lineSeparator());
        }
    }


    private synchronized void addParametersInReport(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0 && tcLevelInfoDTO.getParameters()[0] instanceof HashMap) {
            Logger.logPass(MarkupHelper.createTable(getParameterArray((HashMap<String, String>) tcLevelInfoDTO.getParameters()[0])));
        }
    }

    private static synchronized String[][] getParameterArray(HashMap<String, String> hm) {
        String[][] parameters = new String[hm.size()][2];
        int row = 0;
        int column = 0;
        for (String str : hm.keySet()) {
            parameters[row][column] = "<b>" + str + "</b>";
            column++;
            parameters[row][column] = hm.get(str);
            row++;
            column = 0;
        }
        return parameters;
    }

    protected synchronized String getMethodNameWithParams(TCLevelInfoDTO tcLevelInfoDTO) {
        String methodName = tcLevelInfoDTO.getMethodName();
        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0) {
            methodName = methodName + " [" + tcLevelInfoDTO.getParameters()[0].toString() + "]";
        }

        return methodName;
    }

    private synchronized String getTestDescription(TCLevelInfoDTO tcLevelInfoDTO) {
        String description = tcLevelInfoDTO.getDescription();
        String nextLineCharacter = "<br>";
        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0) {
            description = description + nextLineCharacter + tcLevelInfoDTO.getParameters()[0].toString();
        }
        return description;
    }

    private synchronized void logStatusToConsole(TCLevelInfoDTO tcLevelInfoDTO) {
        String status = "";

        if (tcLevelInfoDTO.getIsProductionBug())
            status = "Production Bug";

        else if (tcLevelInfoDTO.getTcStatus() == TCStatus.SUCCESS)
            status = "Pass";

        else if (tcLevelInfoDTO.getTcStatus() == TCStatus.FAILURE)
            status = "Fail";

        else if (tcLevelInfoDTO.getTcStatus() == TCStatus.SKIP)
            status = "Skip";


        Logger.logInfoInLogger(tcLevelInfoDTO.getMethodName() + " = [" + status + "]<br>");
    }

    private synchronized void assignCategoryToTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getParameters() != null && tcLevelInfoDTO.getParameters().length > 0) {
            if(tcLevelInfoDTO.getTcReportingCategory()!=null) {
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getTcReportingCategory(), tcLevelInfoDTO.getParameters()[0].toString());
            }else {
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getClassName(), tcLevelInfoDTO.getParameters()[0].toString());
            }
        } else {
            if(tcLevelInfoDTO.getTcReportingCategory()!=null) {
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getTcReportingCategory());
            }else{
                ExtentManager.getTest().get().assignCategory(tcLevelInfoDTO.getClassName());
            }
        }
    }

    private synchronized void assignExceptionToTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getException() != null) {
            Logger.logFail(tcLevelInfoDTO.getException());
        }
    }

    private synchronized void addExtentLabelToTest(TCLevelInfoDTO tcLevelInfoDTO) {
        if (tcLevelInfoDTO.getTcStatus() == TCStatus.SUCCESS)
            ExtentManager.getTest().get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));

        else if (tcLevelInfoDTO.getTcStatus() == TCStatus.FAILURE) {
            if (tcLevelInfoDTO.getIsProductionBug())
                ExtentManager.getTest().get().warning(MarkupHelper.createLabel("Production Bug", ExtentColor.TEAL));
            else
                ExtentManager.getTest().get().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
        } else
            ExtentManager.getTest().get().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.ORANGE));

    }


    private synchronized void updateTestStatusInSuiteDTO(TCLevelInfoDTO tcLevelInfoDTO) {
        GlobalVariables.SUITE_TEST_STATUS_DTO.incrementTotalTest();

        switch (tcLevelInfoDTO.getTcStatus()) {
            case TCStatus.SUCCESS:
                GlobalVariables.SUITE_TEST_STATUS_DTO.incrementTotalPass();
                break;

            case TCStatus.FAILURE:
                if (tcLevelInfoDTO.getIsProductionBug())
                    GlobalVariables.SUITE_TEST_STATUS_DTO.incrementProductionBugs();
                else
                    GlobalVariables.SUITE_TEST_STATUS_DTO.incrementTotalFail();
                break;

            case TCStatus.SKIP:
                GlobalVariables.SUITE_TEST_STATUS_DTO.incrementTotalSkip();
                break;
        }
        System.out.println("Total Test Cases :- " + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalTest());
        System.out.println("Total Pass Cases :- " + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalPass());
        System.out.println("Total Fail Cases :- " + (GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalFail() + GlobalVariables.SUITE_TEST_STATUS_DTO.getProductionBugs()));
        System.out.println("Total Skip Cases :- " + GlobalVariables.SUITE_TEST_STATUS_DTO.getTotalSkip());

        HashMap<String, PackageTestStatusDTO> packageWiseTestStatusMap = GlobalVariables.SUITE_TEST_STATUS_DTO.getPackageTestStatusDTOHashMap();

        if (packageWiseTestStatusMap.get(tcLevelInfoDTO.getPackageName()) == null) {
            PackageTestStatusDTO packageTestStatusDTO = new PackageTestStatusDTO(tcLevelInfoDTO.getPackageName(), 0, 0, 0, 0, 0, new LinkedHashMap<>());
            packageWiseTestStatusMap.put(tcLevelInfoDTO.getPackageName(), updatePackageDTO(packageTestStatusDTO, tcLevelInfoDTO));
        } else {
            PackageTestStatusDTO packageTestStatusDTO = packageWiseTestStatusMap.get(tcLevelInfoDTO.getPackageName());
            packageWiseTestStatusMap.put(tcLevelInfoDTO.getPackageName(), updatePackageDTO(packageTestStatusDTO, tcLevelInfoDTO));
        }

    }

    private synchronized PackageTestStatusDTO updatePackageDTO(PackageTestStatusDTO packageTestStatusDTO, TCLevelInfoDTO tcLevelInfoDTO) {
        packageTestStatusDTO.incrementTotalTest();

        switch (tcLevelInfoDTO.getTcStatus()) {
            case TCStatus.SUCCESS:
                packageTestStatusDTO.incrementTotalPass();
                break;

            case TCStatus.FAILURE:
                if (tcLevelInfoDTO.getIsProductionBug())
                    packageTestStatusDTO.incrementProductionBugs();
                else
                    packageTestStatusDTO.incrementTotalFail();

                break;

            case TCStatus.SKIP:
                packageTestStatusDTO.incrementTotalSkip();
                break;
        }


        HashMap<String, ClassTestStatusDTO> classWiseTestStatusMap = packageTestStatusDTO.getClassTestStatusDTOHashMap();

        if (classWiseTestStatusMap.get(tcLevelInfoDTO.getClassName()) == null) {
            ClassTestStatusDTO classTestStatusDTO = new ClassTestStatusDTO(tcLevelInfoDTO.getPackageName(), 0, 0, 0, 0, 0);
            classWiseTestStatusMap.put(tcLevelInfoDTO.getClassName(), updateClassStatusDTO(classTestStatusDTO, tcLevelInfoDTO));
        } else {
            ClassTestStatusDTO classTestStatusDTO = classWiseTestStatusMap.get(tcLevelInfoDTO.getClassName());
            classWiseTestStatusMap.put(tcLevelInfoDTO.getClassName(), updateClassStatusDTO(classTestStatusDTO, tcLevelInfoDTO));
        }

        return packageTestStatusDTO;
    }

    private synchronized ClassTestStatusDTO updateClassStatusDTO(ClassTestStatusDTO classTestStatusDTO, TCLevelInfoDTO tcLevelInfoDTO) {
        classTestStatusDTO.incrementTotalTest();

        switch (tcLevelInfoDTO.getTcStatus()) {
            case TCStatus.SUCCESS:
                classTestStatusDTO.incrementTotalPass();
                break;

            case TCStatus.FAILURE:
                if (tcLevelInfoDTO.getIsProductionBug())
                    classTestStatusDTO.incrementProductionBugs();
                else
                    classTestStatusDTO.incrementTotalFail();

                break;

            case TCStatus.SKIP:
                classTestStatusDTO.incrementTotalSkip();
                break;
        }

        return classTestStatusDTO;
    }

    public synchronized void removeRetriedTest(TCLevelInfoDTO tcLevelInfoDTO) {
        logRetryStatusToConsole(tcLevelInfoDTO);
        deleteCurrentTestFromReport();
        ExtentManager.flush();
    }

    private synchronized void logRetryStatusToConsole(TCLevelInfoDTO tcLevelInfoDTO) {
        Logger.logInfoInLogger("-------- Retry = " + tcLevelInfoDTO.getMethodName() + "--------<br>");
        System.out.println("-------- Retry = " + tcLevelInfoDTO.getMethodName() + "--------" + System.lineSeparator());
    }


    protected synchronized void deleteCurrentTestFromReport() {
        ExtentManager.deleteCurrentTest();
    }

}