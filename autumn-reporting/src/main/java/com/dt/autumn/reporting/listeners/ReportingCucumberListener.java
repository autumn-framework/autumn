package com.dt.autumn.reporting.listeners;

import com.dt.autumn.reporting.dataObjects.TCLevelInfoDTO;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.ScenarioImpl;
import gherkin.formatter.model.Result;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class ReportingCucumberListener extends ReportingCommonListener implements ITestListener {

    private static ThreadLocal<TCLevelInfoDTO> tcCompletionDTOThreadLocal = new ThreadLocal<>();
    private static HashMap<String ,Integer> retryMap=new HashMap<>();
    private static ArrayList<String> passScenarioMap=new ArrayList<>();


    @Override
    public synchronized void onFinish(ITestContext context) {
        removeRetriedTestsFromTestNG(context);
        logFinishSuite();
    }

    @Before
    public synchronized void onCucumberTestStart(Scenario scenario) {
        TCLevelInfoDTO tcLevelInfoDTO = new TCLevelInfoDTO();
        tcCompletionDTOThreadLocal.set(tcLevelInfoDTO);
        tcCompletionDTOThreadLocal.get().setDescription(scenario.getName());
        tcCompletionDTOThreadLocal.get().setClassName(getFeatureFileNameFromScenarioId(scenario)).setMethodName(getFeatureFileNameFromScenarioId(scenario)).setPackageName(getPackageNameFromScenarioId(scenario));
        tcCompletionDTOThreadLocal.get().setParameters(null);
        logStartTest(tcCompletionDTOThreadLocal.get());
    }


    @After
    public synchronized void onCucumberTestFinish(Scenario scenario){
        String scenarioStatus=scenario.getStatus().toUpperCase();
        setTCCompletionDTO(scenario);

        if(AnnotationTransformer.getRetryFlag()){
            if(scenarioStatus.equals("PASSED"))
                checkPassedRetryScenarios(scenario);
            else
                checkFailedRetryScenarios(scenario);
        }
        else{
            if(scenarioStatus.equals("PASSED"))
                logSuccessTest(tcCompletionDTOThreadLocal.get());
            else if (tcCompletionDTOThreadLocal.get().getIsProductionBug())
                logProductionBug(tcCompletionDTOThreadLocal.get());
            else
                logFailedTest(tcCompletionDTOThreadLocal.get());
        }

    }

    private synchronized void checkPassedRetryScenarios(Scenario scenario){
        if(!passScenarioMap.contains(scenario.getId())){
            logSuccessTest(tcCompletionDTOThreadLocal.get());
            passScenarioMap.add(scenario.getId());
        }
        else
            logSkippedTest(tcCompletionDTOThreadLocal.get());
    }



    public synchronized void setTCCompletionDTO(Scenario scenario){
        tcCompletionDTOThreadLocal.get().setTcStatus(getScenarioStatus(scenario.getStatus()));
        tcCompletionDTOThreadLocal.get().setIsProductionBug(isProductionBug(scenario));
        tcCompletionDTOThreadLocal.get().setException(getThrowableError(scenario));
    }

    public static synchronized boolean isProductionBug(Scenario scenario) {
        boolean flag = false;
        ArrayList<String > tags = (ArrayList<String>) scenario.getSourceTagNames();
        tags= (ArrayList<String>) tags.stream().filter(String -> String.toUpperCase().contains("PRODUCTIONBUG")).collect(Collectors.toList());
        int count = tags.size();
        if (count!=0)
            flag = true;
        return flag;
    }

    private static Throwable getThrowableError(Scenario scenario) {
        Field field = FieldUtils.getField(((ScenarioImpl) scenario).getClass(), "stepResults", true);
        field.setAccessible(true);
        Throwable th=null;
        try {
            ArrayList<Result> results = (ArrayList<Result>) field.get(scenario);
            for (Result result : results) {
                if (result.getError() != null)
                    th= result.getError();

            }
        } catch (Exception e) {
        }
        return th;
    }


    private synchronized void checkFailedRetryScenarios(Scenario scenario){
        int retry;
        if(retryMap.containsKey(scenario.getId()))
            retry=retryMap.get(scenario.getId());
        else{
            retryMap.put(scenario.getId(), RetryAnalyzer.RETRYLIMIT);
            retry=retryMap.get(scenario.getId());
        }

        if(retry>0){
            logSkippedTest(tcCompletionDTOThreadLocal.get());
            retryMap.put(scenario.getId(),retryMap.get(scenario.getId())-1);
        }
        else if (tcCompletionDTOThreadLocal.get().getIsProductionBug())
            logProductionBug(tcCompletionDTOThreadLocal.get());
        else
            logFailedTest(tcCompletionDTOThreadLocal.get());


    }

    public synchronized int getScenarioStatus(String status){
        if(status.equalsIgnoreCase("PASSED"))
            return 1;
        else if (status.equalsIgnoreCase("FAILED"))
            return 2;
        else if (status.equalsIgnoreCase("SKIPPED"))
            return 3;

        else return 0;
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

    private String getFeatureFileNameFromScenarioId(Scenario scenario) {
        String featureName = "Feature ";
        String rawFeatureName = scenario.getId().split(";")[0].replace("-"," ");
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
