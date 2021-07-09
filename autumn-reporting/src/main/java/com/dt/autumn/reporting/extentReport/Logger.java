package com.dt.autumn.reporting.extentReport;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;

public class Logger {

    public synchronized static void logPass(String log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.PASS, log);
        logInfoInLogger(log);
    }

    public synchronized static void logPass(Markup log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.PASS, log);
        logInfoInLogger(log.getMarkup());
    }

    public synchronized static void logFail(String log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.FAIL, log);
        logInfoInLogger(log);
    }

    public synchronized static void logFail(Markup log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.FAIL, log);
        logInfoInLogger(log.getMarkup());
    }

    public synchronized static void logFail(Throwable throwable) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.FAIL, throwable);
        logInfoInLogger(throwable.toString());
    }

    public synchronized static void logInfo(String log){
        if(ExtentManager.getTest().get() != null){
            ExtentManager.getTest().get().log(Status.INFO, log);
        }else{
            ExtentManager.setExtentTestBefore(MarkupHelper.createLabel(log, ExtentColor.TRANSPARENT));
        }
        logInfoInLogger(log);
    }

    public synchronized static void logInfo(Markup log){
        if(ExtentManager.getTest().get() != null){
            ExtentManager.getTest().get().log(Status.INFO, log);
        }else{
            ExtentManager.setExtentTestBefore(log);
        }
        logInfoInLogger(log.getMarkup());
    }

    public synchronized static void logInfoInLogger(String log){
        if(ExtentManager.getLog().get() != null){
            ExtentManager.getLog().get().add(log);
        }
        else{
            ExtentManager.getLogger().info(log);
        }
    }

    public synchronized static void logSkip(String log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.SKIP, MarkupHelper.createLabel(log, ExtentColor.ORANGE));
        logInfoInLogger(log);
    }

    public synchronized static void logSkip(Markup log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.SKIP, log);
        logInfoInLogger(log.getMarkup());
    }


    public synchronized static void logWarning(String log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.WARNING, MarkupHelper.createLabel(log, ExtentColor.LIME));
        logInfoInLogger(log);
    }

    public synchronized static void logWarning(Markup log) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.WARNING, log);
        logInfoInLogger(log.getMarkup());
    }

    public synchronized static void logWarning(Throwable throwable) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(Status.WARNING, throwable);
        logInfoInLogger(throwable.toString());
    }

    /**
     * Try and avoid the use of the same
     *
     * @param status
     * @param throwable
     */
    public synchronized static void logException(Status status, Throwable throwable) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().log(status, throwable);
        logInfoInLogger(throwable.toString());
    }

    public synchronized static void logCategory(String category) {
        if (ExtentManager.getTest().get() != null)
            ExtentManager.getTest().get().assignCategory(category);
    }
}
