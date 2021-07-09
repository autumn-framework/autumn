package com.dt.autumn.jira;

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

import com.dt.autumn.jira.apiServices.ZephyrService;
import com.dt.autumn.jira.dataObjects.*;

import java.io.IOException;
import java.util.*;

public class ZephyrManager {
    static Map<String, String> testcaseIDMap;
    static String projectID;
    static String versionID;
    static String newCycleID;
    static String testRepoVersionId;
    static String testRepoCycleID;

    private ZephyrManager() {

    }

    public static String getCycleID(String projectID, String versionID, String cycleName) throws IOException {
        Map<String, GetAllCyclesResponseDTO> getAllCyclesResponseDTOS = ZephyrService.getAllCycles(projectID, versionID);
        for (String key : getAllCyclesResponseDTOS.keySet()) {
            if (getAllCyclesResponseDTOS.get(key).getName().equalsIgnoreCase(cycleName)) {
                return key;
            }
        }
        return null;
    }


    public static void addTestsToCycle(String newCycleID, String projectID, String versionID, String folderId, List<String> allTestCaseList) throws IOException {
        AddTestsToCycleRequestDTO addTestsToCycleRequestDTO = new AddTestsToCycleRequestDTO();
        addTestsToCycleRequestDTO.setComponents("").setCycleId(newCycleID).setMethod("1").setProjectId(projectID).setVersionId(versionID).setAssigneeType("").setIssues(allTestCaseList);
        if (folderId != null) {
            addTestsToCycleRequestDTO.setFolderId(folderId);
        }
        ZephyrService.addTestsToCycle(addTestsToCycleRequestDTO);
    }

    public static List<String> getAllIssuesList(String projectID, String versionID, String cycleID, Integer folderId) throws IOException {
        List<String> issuesList = new LinkedList<>();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("projectId", projectID);
        queryParams.put("versionId", versionID);
        queryParams.put("cycleId", cycleID);
        if (folderId != null) {
            queryParams.put("folderId", String.valueOf(folderId));
        }
        GetAllExecutionsResponseDTO getAllExecutionsResponseDTO = ZephyrService.getAllExecutions(queryParams);
        for (GetAllExecutionsResponseDTO.Execution execution : getAllExecutionsResponseDTO.getExecutions()) {
            issuesList.add(execution.getIssueKey());
        }
        return issuesList;
    }

    public static Map<String, String> getAllExecutions(String projectID, String versionID, String cycleID, Integer folderId) throws IOException {
        Map<String, String> issueKeyIDMap = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("projectId", projectID);
        queryParams.put("versionId", versionID);
        queryParams.put("cycleId", cycleID);
        if (folderId != null) {
            queryParams.put("folderId", String.valueOf(folderId));
        }
        GetAllExecutionsResponseDTO getAllExecutionsResponseDTO = ZephyrService.getAllExecutions(queryParams);
        for (GetAllExecutionsResponseDTO.Execution execution : getAllExecutionsResponseDTO.getExecutions()) {
            issueKeyIDMap.put(execution.getIssueKey(), String.valueOf(execution.getId()));
        }

        return issueKeyIDMap;
    }

    public static Map<Integer, String> getAllFolderIdsForCycle(String projectID, String versionID, String cycleID) throws IOException {
        Map<Integer, String> folderDetails = new HashMap<>();
        List<GetAllFolderIdsViaCycleDTO> getAllFolderIdsViaCycleDTOList = ZephyrService.getAllFolderIdsForCycle(projectID, cycleID, versionID);
        for (GetAllFolderIdsViaCycleDTO getAllFolderIdsViaCycleDTO : getAllFolderIdsViaCycleDTOList) {
            folderDetails.put(getAllFolderIdsViaCycleDTO.getFolderId(), getAllFolderIdsViaCycleDTO.getFolderName());
        }
        return folderDetails;
    }

    public static void updateTestCaseStatus(String issueID, String status) throws IOException {
        ZephyrService.updateTestCaseStatus(issueID, status);
    }

    public static String createTestCycle(String projectID, String versionID, String testRepoCycleName) throws IOException {
        CreateTestCycleResponseDTO createTestCycleResponseDTO = ZephyrService.createTestCycle(projectID, versionID, testRepoCycleName);
        return createTestCycleResponseDTO.getId();
    }

    public static String createTestFolder(String projectID, String versionID, String newCycleID, String folderName) throws IOException {
        CreateTestFolderResponseDTO createTestFolderResponseDTO = ZephyrService.createFolderTestCycle(projectID, versionID, newCycleID, folderName, "");
        return createTestFolderResponseDTO.getId();
    }

    public static void bulkUpdateTestCaseStatus(List<String> issueIds, String status) throws IOException {
        ZephyrService.bulkUpdateTestCaseStatus(issueIds, status);
    }


    static void updateResults(List<String> jiraIDs, String status) throws IOException {
        List<String> issueIds = new LinkedList<>();

        for (String jiraID : jiraIDs) {
            if (testcaseIDMap.get(jiraID) != null)
                issueIds.add(testcaseIDMap.get(jiraID));
        }

        if (issueIds.size() >= 1) {
            bulkUpdateTestCaseStatus(issueIds, status);
        }
    }

    public static void updateResultsInZephyr(List<String> jiraIDs, String status) {

        if (jiraIDs.size() >= 1) {
            try {
                if (testcaseIDMap == null) {
                    testcaseIDMap = new LinkedHashMap<>();
                    Map<Integer, String> folderIdsForCycle = getAllFolderIdsForCycle(projectID, versionID, newCycleID);
                    if (folderIdsForCycle.size() > 0) {
                        for (Integer folderId : folderIdsForCycle.keySet()) {
                            testcaseIDMap.putAll(getAllExecutions(projectID, versionID, newCycleID, folderId));
                        }
                    }
                    testcaseIDMap.putAll(getAllExecutions(projectID, versionID, newCycleID, null));
                }
                updateResults(jiraIDs, status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void createJIRACycleforExecution(String releaseName, String testRepoReleaseName, String testRepoCycleName, String newCycleName) throws IOException {
        projectID = JiraManager.getProjectID(JiraManager.getJiraProjectName());
        versionID = JiraManager.getVersionID(projectID, releaseName);
        newCycleID = getCycleID(projectID, versionID, newCycleName);
        if (newCycleID == null) {
            newCycleID = createTestCycle(projectID, versionID, newCycleName);
        }
        testRepoVersionId = JiraManager.getVersionID(projectID, testRepoReleaseName);
        testRepoCycleID = getCycleID(projectID, testRepoVersionId, testRepoCycleName);
    }


    public static void createCycleAndMoveCases() throws IOException {
        Map<Integer, String> folderIdsForCycle = getAllFolderIdsForCycle(projectID, testRepoVersionId, testRepoCycleID);
        Map<Integer, String> newFolderIdsForCycle = getAllFolderIdsForCycle(projectID, versionID, newCycleID);
        if (folderIdsForCycle.size() > 0) {
            for (Integer folderId : folderIdsForCycle.keySet()) {
                String folderName = folderIdsForCycle.get(folderId);
                List<String> allTestCasesList = getAllIssuesList(projectID, testRepoVersionId, testRepoCycleID, folderId);
                String newFolderId = null;
                if (newFolderIdsForCycle.size() > 0) {
                    for (Integer fdId : newFolderIdsForCycle.keySet()) {
                        if (folderName.equalsIgnoreCase(newFolderIdsForCycle.get(fdId))) {
                            newFolderId = Integer.toString(fdId);
                            break;
                        }
                    }
                }
                if (newFolderId == null) {
                    newFolderId = createTestFolder(projectID, versionID, newCycleID, folderName);
                }
                addTestsToCycle(newCycleID, projectID, versionID, newFolderId, allTestCasesList);
            }
        }
        List<String> allTestCasesList = getAllIssuesList(projectID, testRepoVersionId, testRepoCycleID, null);
        if (allTestCasesList.size() > 0) {
            addTestsToCycle(newCycleID, projectID, versionID, null, allTestCasesList);
        }

    }

    public static void addTCZephyrCycle(String zephyrFolderName, List<String> jiraIds) throws IOException {
        Map<Integer, String> folderIdsForCycle = getAllFolderIdsForCycle(projectID, testRepoVersionId, testRepoCycleID);
        if (folderIdsForCycle.size() > 0) {
            for (Integer folderId : folderIdsForCycle.keySet()) {
                String folderName = folderIdsForCycle.get(folderId);
                if (folderName.equalsIgnoreCase(zephyrFolderName)) {
                    addTestsToCycle(testRepoCycleID, projectID, testRepoVersionId, folderId.toString(), jiraIds);
                    return;
                }
            }
        }
        String newFolderId = createTestFolder(projectID, testRepoVersionId, testRepoCycleID, zephyrFolderName);
        if(newFolderId !=null && !newFolderId.equalsIgnoreCase("")) {
            addTestsToCycle(testRepoCycleID, projectID, testRepoVersionId, newFolderId, jiraIds);
        }
    }

}
