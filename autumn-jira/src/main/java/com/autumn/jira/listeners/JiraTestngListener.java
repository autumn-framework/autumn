package com.autumn.jira.listeners;

/*-
 * #%L
 * autumn-jira
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

import com.autumn.jira.DBZephyrTestCaseMapper;
import com.autumn.jira.annotations.JiraID;
import com.autumn.jira.dataObjects.JiraTCLevelInfoDTO;
import com.autumn.jira.generics.JiraGenericFunctions;
import com.autumn.reporting.extentReport.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.HashMap;

public class JiraTestngListener extends JiraCommonListener implements ITestListener {

    private static ThreadLocal<JiraTCLevelInfoDTO> tcCompletionDTOThreadLocal = new ThreadLocal<>();
    public static final String KEY_JIRAID = "JiraID";


    @Override
    public synchronized void onStart(ITestContext context) {
        logStart();
    }


    @Override
    public synchronized void onFinish(ITestContext context) {
        logFinishSuite();
    }


    @Override
    public synchronized void onTestStart(ITestResult result) {
        JiraTCLevelInfoDTO jiraTCLevelInfoDTO = new JiraTCLevelInfoDTO();
        String className = getSimpleClassName(result);
        String methodName = getSimpleMethodName(result);
        String packageName = getPackageNameFromTestMethod(result);
        String description = getSimpleDescription(result);
        String methodJiraId = getJiraIdFromMethod(result);
        tcCompletionDTOThreadLocal.set(jiraTCLevelInfoDTO);
        tcCompletionDTOThreadLocal.get().setClassName(className).setMethodName(methodName).setPackageName(packageName);
        if (JiraGenericFunctions.getEnableTcUploadMongo()) {
            tcCompletionDTOThreadLocal.get().setJiraId(DBZephyrTestCaseMapper.getInstance().getJiraId(packageName, className, methodName, description, methodJiraId));
        } else {
            tcCompletionDTOThreadLocal.get().setJiraId(methodJiraId);
        }
        logStartTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        logSuccessTest(tcCompletionDTOThreadLocal.get());
    }

    @Override
    public synchronized void onTestFailure(ITestResult result) {
        logFailedTest(tcCompletionDTOThreadLocal.get());

    }

    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        logSkippedTest(tcCompletionDTOThreadLocal.get());
    }


    private synchronized String getJiraIdFromMethod(ITestResult iTestResult) {
        String jiraIDString = "";
        try {
            if (iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(JiraID.class) != null) {
                jiraIDString = iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(JiraID.class).value();
                if (jiraIDString.length() > 1) {
                    if (jiraIDString.equalsIgnoreCase("DataProvider")) {
                        HashMap<String, String> hm = ((HashMap<String, String>) iTestResult.getParameters()[0]);
                        if (hm.containsKey(KEY_JIRAID) && hm.get(KEY_JIRAID).length() > 1)
                            jiraIDString = hm.get(KEY_JIRAID);
                    }
                }
            }
        } catch (Exception e) {
            Logger.logInfoInLogger("XXXXX Some Exception in getting Jira ID from Method = " + iTestResult.getMethod().getMethodName());
        }
        return jiraIDString;
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


    private synchronized String getPackageNameFromTestMethod(ITestResult result) {
        String strList[] = result.getTestClass().getRealClass().getPackage().getName().split("\\.");
        return strList[strList.length - 1];
    }


}
