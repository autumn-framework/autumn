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

import com.dt.autumn.jira.apiServices.JiraService;
import com.dt.autumn.jira.dataObjects.*;
import com.dt.autumn.reporting.assertions.CustomAssert;
import com.dt.autumn.api.jsonProcessor.JacksonJsonImpl;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.List;

public class JiraManager {

    private static String jiraProjectName;

    private JiraManager(){

    }

    public static String getJiraProjectName() {
        return jiraProjectName;
    }

    public static void setJiraProjectName(String jiraProjectName) {
        JiraManager.jiraProjectName = jiraProjectName;
    }

    public static String getProjectID(String projectName) throws IOException {
        List<GetAllProjectsResponseDTO> getAllProjectsResponseList = JiraService.getAllProjects();
        for (GetAllProjectsResponseDTO getAllProjectsResponseDTO : getAllProjectsResponseList) {
            if (getAllProjectsResponseDTO.getName().equalsIgnoreCase(projectName)) {
                return getAllProjectsResponseDTO.getId();
            }
        }
        return null;
    }

    public static String getVersionID(String projectID, String versionName) throws IOException {
        List<GetAllVersionsResponseDTO> getAllVersionsResponseDTOList = JiraService.getAllVersions(projectID);
        for (GetAllVersionsResponseDTO getAllVersionsResponseDTO : getAllVersionsResponseDTOList) {
            if (getAllVersionsResponseDTO.getName().equalsIgnoreCase(versionName)) {
                return getAllVersionsResponseDTO.getId();
            }
        }
        return null;
    }

    public static List<ProjectMetadataResponseDTO.IssueType> getIssueTypesList(String projectId) throws IOException {
        return getProjectMetadata(projectId).getIssueTypes();
    }

    public static ProjectMetadataResponseDTO getProjectMetadata(String projectId) throws IOException {
        return JiraService.getProjectMetadata(projectId);
    }

    public static String getIssueTypeId(String projectId, String issueTypeName) throws IOException {
        List<ProjectMetadataResponseDTO.IssueType> issueTypeList = getIssueTypesList(projectId);
        for(ProjectMetadataResponseDTO.IssueType issueType : issueTypeList){
            if(issueType.getName().equalsIgnoreCase(issueTypeName)){
                return issueType.getId();
            }
        }
        return null;
    }

    public static void editJiraIssue(String request, String jiraId) throws IOException {
        Response response = JiraService.editJiraIssue(request, jiraId);
        CustomAssert.assertEquals(response.getStatusCode(),204,"");
    }

    public static String createJiraIssue(String request) throws IOException {
        CreateIssueResponseDTO createIssueResponseDTO = JiraService.createJiraIssue(request);
        return createIssueResponseDTO.getKey();
    }

    public static String createBasicJira(String projectId, String summary, String issueTypeName) throws IOException {
        String issueTypeId = getIssueTypeId(projectId,issueTypeName);
        String defaultRequest = "{\"fields\":{\"project\":{\"id\":\"\"},\"summary\":\"\",\"issuetype\":{\"id\":\"\"},\"labels\":[],\"description\":\"\"}}";
        CreateIssueRequestDTO createIssueRequestDTO = JacksonJsonImpl.getInstance().fromJson(defaultRequest,CreateIssueRequestDTO.class);
        createIssueRequestDTO.getFields().setSummary(summary).setDescription(summary);
        createIssueRequestDTO.getFields().getProject().setId(projectId);
        createIssueRequestDTO.getFields().getIssuetype().setId(issueTypeId);
        String requestJson = JacksonJsonImpl.getInstance().toJSon(createIssueRequestDTO);
        return createJiraIssue(requestJson);
    }

    public static String uploadTest(String projectId, String summary, String issueTypeName, String description, List<Object> labels) throws IOException {
        String issueTypeId = getIssueTypeId(projectId,issueTypeName);
        String defaultRequest = "{\"fields\":{\"project\":{\"id\":\"\"},\"summary\":\"\",\"issuetype\":{\"id\":\"\"},\"labels\":[],\"description\":\"\"}}";
        CreateIssueRequestDTO createIssueRequestDTO = JacksonJsonImpl.getInstance().fromJson(defaultRequest,CreateIssueRequestDTO.class);
        createIssueRequestDTO.getFields().setSummary(summary).setDescription(description);
        createIssueRequestDTO.getFields().setLabels(labels);
        createIssueRequestDTO.getFields().getProject().setId(projectId);
        createIssueRequestDTO.getFields().getIssuetype().setId(issueTypeId);
        String requestJson = JacksonJsonImpl.getInstance().toJSon(createIssueRequestDTO);
        return createJiraIssue(requestJson);
    }

    public static void editTest(String summary, String description, List<Object> labels, String jiraId) throws IOException {
        String defaultRequest = "{\"fields\":{\"summary\":\"\",\"labels\":[],\"description\":\"\"}}";
        CreateIssueRequestDTO createIssueRequestDTO = JacksonJsonImpl.getInstance().fromJson(defaultRequest,CreateIssueRequestDTO.class);
        createIssueRequestDTO.getFields().setSummary(summary).setDescription(description);
        createIssueRequestDTO.getFields().setLabels(labels);
        String requestJson = JacksonJsonImpl.getInstance().toJSon(createIssueRequestDTO);
        editJiraIssue(requestJson, jiraId);
    }

    public static String createBasicJiraIssue(String projectName, String summary, String issueType) throws IOException {
        String projectId = getProjectID(projectName);
        return createBasicJira(projectId,summary,issueType);
    }

    public static String uploadZephyrTestCase(String summary, String description, List<Object> labels) throws IOException {
        String projectId = getProjectID(JiraManager.getJiraProjectName());
        return uploadTest(projectId,summary,"Test",description,labels);
    }


}
