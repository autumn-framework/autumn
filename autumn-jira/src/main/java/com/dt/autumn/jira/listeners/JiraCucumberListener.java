package com.dt.autumn.jira.listeners;

import com.dt.autumn.jira.dataObjects.JiraTCLevelInfoDTO;
import com.dt.autumn.reporting.dataObjects.TCLevelInfoDTO;
import com.dt.autumn.reporting.extentReport.Logger;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.testng.ITestContext;
import org.testng.ITestListener;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class JiraCucumberListener extends JiraCommonListener implements ITestListener {

    private static ThreadLocal<JiraTCLevelInfoDTO> tcCompletionDTOThreadLocal = new ThreadLocal<>();


    @Override
    public synchronized void onStart(ITestContext context) {
        logStart();
    }

    @Override
    public synchronized void onFinish(ITestContext context) {
        logFinishSuite();
    }

    @Before
    public synchronized void onCucumberTestStart(Scenario scenario) {
        JiraTCLevelInfoDTO jiraTCLevelInfoDTO = new JiraTCLevelInfoDTO();
        tcCompletionDTOThreadLocal.set(jiraTCLevelInfoDTO);
        tcCompletionDTOThreadLocal.get().setClassName(getFeatureFileNameFromScenarioId(scenario)).setMethodName(getFeatureFileNameFromScenarioId(scenario)).setPackageName(getPackageNameFromScenarioId(scenario));
        tcCompletionDTOThreadLocal.get().setJiraId(getJiraIdFromMethod(scenario));
        logStartTest(tcCompletionDTOThreadLocal.get());
    }

    @After
    public synchronized void onCucumberTestFinish(Scenario scenario) {
        String scenarioStatus=scenario.getStatus().toUpperCase();
        if (scenarioStatus.equals("PASSED"))
            logSuccessTest(tcCompletionDTOThreadLocal.get());
        else
            logFailedTest(tcCompletionDTOThreadLocal.get());

    }


    private synchronized String getJiraIdFromMethod(Scenario scenario) {
        String jiraIDString = "";
        ArrayList<String> jiras = (ArrayList<String>) scenario.getSourceTagNames();
        jiras = (ArrayList<String>) jiras.stream().filter(String -> String.toUpperCase().contains("JIRAID:")).collect(Collectors.toList());
        int count = jiras.size();
        try {
            if (count == 1) {
                jiraIDString = jiras.get(0).split(":")[1];

            }

        } catch (Exception e) {
            Logger.logInfoInLogger("XXXXX Some Exception in getting Jira ID from Method = " + getFeatureFileNameFromScenarioId(scenario));
        }
        return jiraIDString;
    }

    private String getFeatureFileNameFromScenarioId(Scenario scenario) {
        String featureName = "Feature ";
        String rawFeatureName = scenario.getId().split(";")[0].replace("-", " ");
        featureName = featureName + rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);
        return featureName;
    }

    private String getPackageNameFromScenarioId(Scenario scenario) {
        String featureName ="";
        String rawFeatureName = scenario.getId().split(";")[0].replace("-"," ");
        featureName = featureName + rawFeatureName.substring(0, 1).toUpperCase() + rawFeatureName.substring(1);
        return featureName;
    }

}
