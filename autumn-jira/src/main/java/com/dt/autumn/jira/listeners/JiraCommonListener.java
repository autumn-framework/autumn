package com.dt.autumn.jira.listeners;

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

import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.dt.autumn.jira.DBZephyrTestCaseMapper;
import com.dt.autumn.jira.ZephyrManager;
import com.dt.autumn.jira.dataObjects.JiraTCLevelInfoDTO;
import com.dt.autumn.jira.generics.JiraGenericFunctions;
import com.dt.autumn.jira.dataObjects.JiraIdStatusDTO;
import com.dt.autumn.reporting.extentReport.ExtentManager;
import com.dt.autumn.reporting.extentReport.Logger;

import java.io.IOException;

public class JiraCommonListener {

    public static Boolean updateZephyrStatus = false;

    public static Boolean getUpdateZephyrStatus() {
        return updateZephyrStatus;
    }

    public static void setUpdateZephyrStatus(Boolean updateZephyrStatus) {
        JiraCommonListener.updateZephyrStatus = updateZephyrStatus;
    }

    public synchronized void logStart() {
        if(JiraGenericFunctions.getEnableTcUploadMongo()){
            DBZephyrTestCaseMapper.getInstance().fetchDataFromDB();
        }
    }

    public synchronized void logFinishSuite() {
        if (getUpdateZephyrStatus()) {
            try {
                DBZephyrTestCaseMapper.getInstance().addTCZephyrTestCycle();
                ZephyrManager.createCycleAndMoveCases();
                pushResultsInJIRA();
            } catch (IOException ie) {
            }
        }
    }

    public synchronized void logStartTest(JiraTCLevelInfoDTO jiraTCLevelInfoDTO) {
        addJiraIDinReport(jiraTCLevelInfoDTO);
    }

    public synchronized void logFailedTest(JiraTCLevelInfoDTO jiraTCLevelInfoDTO) {
        JiraIdStatusDTO.addFailedJira(jiraTCLevelInfoDTO.getJiraId());
    }

    public synchronized void logSkippedTest(JiraTCLevelInfoDTO jiraTCLevelInfoDTO) {
        if (ExtentManager.isRemoveRetriedTests()) {
            removeRetriedTest(jiraTCLevelInfoDTO);
        }
    }

    public synchronized void logSuccessTest(JiraTCLevelInfoDTO jiraTCLevelInfoDTO) {
        JiraIdStatusDTO.addPassedJira(jiraTCLevelInfoDTO.getJiraId());
    }



    protected synchronized void pushResultsInJIRA() {
        ZephyrManager.updateResultsInZephyr(JiraIdStatusDTO.getPassedJira(), "1");
        ZephyrManager.updateResultsInZephyr(JiraIdStatusDTO.getFailedJira(), "2");
    }

    private synchronized void addJiraIDinReport(JiraTCLevelInfoDTO jiraTCLevelInfoDTO) {
        String id = jiraTCLevelInfoDTO.getJiraId();
        if (id != null && id.length() > 0)
            Logger.logInfo(MarkupHelper.createLabel("<b>Test Case Jira ID : </b>" + id, ExtentColor.CYAN));
    }

    public synchronized void removeRetriedTest(JiraTCLevelInfoDTO jiraTCLevelInfoDTO) {
        JiraIdStatusDTO.removeFailedJira(jiraTCLevelInfoDTO.getJiraId());
    }



}
