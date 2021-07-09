package com.dt.autumn.jira.generics;

import com.dt.autumn.jira.DBZephyrTestCaseMapper;
import com.dt.autumn.jira.JiraManager;
import com.dt.autumn.jira.ZephyrManager;
import com.dt.autumn.jira.apiServices.JiraService;
import com.dt.autumn.jira.listeners.JiraCommonListener;

import java.io.IOException;
import java.util.List;

public class JiraGenericFunctions {

    private static Boolean ENABLE_TC_UPLOAD_MONGO = false;

    public static String uploadZephyrTestCaseJira(String summary, String description, List<Object> labels){
        try {
            return JiraManager.uploadZephyrTestCase(summary,description,labels);
        } catch (IOException e) {
            return "";
        }
    }

    public static void initZephyrReporter(String newReleaseName, String testRepoReleaseName, String testRepoCycleName, String newcycleName) throws IOException {
        JiraCommonListener.setUpdateZephyrStatus(true);
        ZephyrManager.createJIRACycleforExecution(newReleaseName, testRepoReleaseName, testRepoCycleName, newcycleName);
    }

    public static void setJiraDetails(String jiraUrl, String jiraUserName, String jiraPassword, String jiraProjectName){
        JiraService.setJiraUrl(jiraUrl);
        JiraService.setJiraUserName(jiraUserName);
        JiraService.setJiraPassword(jiraPassword);
        JiraManager.setJiraProjectName(jiraProjectName);
    }

    public static void setMongoZephyrTCDBDetails(String zephyrTCMappingUrl, String zephyrTCMappingCollection){
        ENABLE_TC_UPLOAD_MONGO = true;
        DBZephyrTestCaseMapper.getInstance().setZephyrTCMappingUrl(zephyrTCMappingUrl);
        DBZephyrTestCaseMapper.getInstance().setZephyrTCMappingCollection(zephyrTCMappingCollection);
    }

    public static Boolean getEnableTcUploadMongo(){
        return ENABLE_TC_UPLOAD_MONGO;
    }

}
