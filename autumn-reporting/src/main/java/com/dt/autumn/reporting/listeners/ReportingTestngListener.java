package com.dt.autumn.reporting.listeners;

import com.dt.autumn.reporting.annotations.TestCategorizer;
import com.dt.autumn.reporting.annotations.ProductionBug;
import com.dt.autumn.reporting.dataObjects.TCLevelInfoDTO;
import com.dt.autumn.reporting.extentReport.ExtentManager;
import com.dt.autumn.reporting.extentReport.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.HashMap;
import java.util.Set;

public class ReportingTestngListener extends ReportingCommonListener implements ITestListener {

    private static ThreadLocal<TCLevelInfoDTO> tcCompletionDTOThreadLocal = new ThreadLocal<>();
    private static ITestStatusListener testStatusListener = null;
    public static final String KEY_PRODBUG = "ProductionBug";


    @Override
    public synchronized void onFinish(ITestContext context) {
        removeRetriedTestsFromTestNG(context);
        logFinishSuite();
    }

    @Override
    public synchronized void onTestStart(ITestResult result) {
        TCLevelInfoDTO tcLevelInfoDTO = new TCLevelInfoDTO();
        String className = getSimpleClassName(result);
        String methodName = getSimpleMethodName(result);
        String packageName = getPackageNameFromTestMethod(result);
        String description = getSimpleDescription(result);
        String dpsInfo = getSimpleDPSName(result);
        tcCompletionDTOThreadLocal.set(tcLevelInfoDTO);
        tcCompletionDTOThreadLocal.get().setTcReportingCategory(dpsInfo);
        tcCompletionDTOThreadLocal.get().setDescription(description);
        tcCompletionDTOThreadLocal.get().setClassName(className).setMethodName(methodName).setPackageName(packageName);
        tcCompletionDTOThreadLocal.get().setParameters(result.getParameters());
        logStartTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        if (testStatusListener != null)
            testStatusListener.performOnSuccess(result);
        tcCompletionDTOThreadLocal.get().setParameters(result.getParameters());
        setTCCompletionDTO(result);
        logSuccessTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        if (testStatusListener != null)
            testStatusListener.performOnFailure(result);
        tcCompletionDTOThreadLocal.get().setParameters(result.getParameters());
        setTCCompletionDTO(result);
        if (tcCompletionDTOThreadLocal.get().getIsProductionBug())
            logProductionBug(tcCompletionDTOThreadLocal.get());
        else
            logFailedTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        tcCompletionDTOThreadLocal.get().setParameters(result.getParameters());
        setTCCompletionDTO(result);
        if (!ExtentManager.isRemoveRetriedTests()) {
            if (testStatusListener != null)
                testStatusListener.performOnSkip(result);
        }
        logSkippedTest(tcCompletionDTOThreadLocal.get());
    }


    private synchronized String getPackageNameFromTestMethod(ITestResult result) {
        String strList[] = result.getTestClass().getRealClass().getPackage().getName().split("\\.");
        return strList[strList.length - 1];
    }

    private synchronized String getSimpleClassName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName();
    }

    private synchronized String getSimpleMethodName(ITestResult result) {
        return result.getName();
    }

    private synchronized String getSimpleDescription(ITestResult result) {
        return result.getMethod().getDescription();
    }


    private synchronized void removeRetriedTestsFromTestNG(ITestContext context) {
        Set<ITestResult> skippedTests = context.getSkippedTests().getAllResults();
        for (ITestResult temp : skippedTests) {
            ITestNGMethod method = temp.getMethod();

            if (context.getFailedTests().getResults(method).size() > 0) {
                skippedTests.remove(temp);
            } else {
                if (context.getPassedTests().getResults(method).size() > 0) {
                    skippedTests.remove(temp);
                }
            }
        }
    }

    public synchronized void setTCCompletionDTO(ITestResult result) {
        tcCompletionDTOThreadLocal.get().setTcReportingCategory(getSimpleDPSName(result));
        tcCompletionDTOThreadLocal.get().setTcStatus(result.getStatus());
        tcCompletionDTOThreadLocal.get().setIsProductionBug(isProductionBug(result));
        tcCompletionDTOThreadLocal.get().setException(result.getThrowable());
    }


    public static synchronized boolean isProductionBug(ITestResult result) {
        boolean flag = false;
        if (result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(ProductionBug.class) != null)
            flag = true;
        else {
            if (result.getMethod().isDataDriven()) {
                if (result.getParameters().length > 0 && result.getParameters()[0] instanceof HashMap) {
                    HashMap<String, String> hm = (HashMap<String, String>) result.getParameters()[0];
                    if (hm.containsKey(KEY_PRODBUG) && hm.get(KEY_PRODBUG).equalsIgnoreCase("true"))
                        flag = true;
                }
            }
        }
        return flag;
    }

    private synchronized String getSimpleDPSName(ITestResult result) {
        try {
            if (result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestCategorizer.class) != null) {
                return result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(TestCategorizer.class).Category();
            }
        } catch (Exception e) {
            Logger.logInfoInLogger("XXXXX Some Exception in getting DPSInfo from Method = " + result.getMethod().getMethodName());
        }
        return null;
    }


}
