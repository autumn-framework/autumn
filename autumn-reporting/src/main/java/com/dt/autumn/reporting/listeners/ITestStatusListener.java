package com.dt.autumn.reporting.listeners;

import org.testng.ITestResult;

public interface ITestStatusListener {

    void performOnSuccess(ITestResult result);

    void performOnFailure(ITestResult result);

    void performOnSkip(ITestResult result);

}
