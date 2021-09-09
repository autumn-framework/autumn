package com.autumn.jira.generics;

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
import com.autumn.jira.JiraManager;
import com.autumn.jira.ZephyrManager;
import com.autumn.jira.listeners.JiraCommonListener;
import com.autumn.jira.apiServices.JiraService;

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
