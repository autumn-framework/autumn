package com.dt.autumn.jira.apiServices;

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

import com.dt.autumn.jira.dataObjects.*;
import com.dt.autumn.api.BaseApi;
import com.dt.autumn.api.ContentType;
import com.dt.autumn.api.MethodType;
import com.dt.autumn.api.jsonProcessor.JacksonJsonImpl;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JiraService {

    private static String jiraUrl;
    private static String jiraUserName;
    private static String jiraPassword;

    public static String getJiraUrl() {
        return jiraUrl;
    }

    public static String getJiraUserName() {
        return jiraUserName;
    }

    public static String getJiraPassword() {
        return jiraPassword;
    }

    public static void setJiraUrl(String jiraUrl) {
        JiraService.jiraUrl = jiraUrl;
    }

    public static void setJiraUserName(String jiraUserName) {
        JiraService.jiraUserName = jiraUserName;
    }

    public static void setJiraPassword(String jiraPassword) {
        JiraService.jiraPassword = jiraPassword;
    }

    public static List<GetAllProjectsResponseDTO> getAllProjects() throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.GET);
        baseApi.setBaseUri(JiraService.jiraUrl);
        baseApi.setBasicAuth(JiraService.jiraUserName, JiraService.jiraPassword);
        baseApi.setBasePath("api/latest/project");
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        GetAllProjectsResponseDTO[] getAllProjectsResponse = JacksonJsonImpl.getInstance().fromJson(response.asString(), GetAllProjectsResponseDTO[].class);
        List<GetAllProjectsResponseDTO> getAllProjectsResponseDTO = Arrays.asList(getAllProjectsResponse);
        return getAllProjectsResponseDTO;
    }

    public static List<GetAllVersionsResponseDTO> getAllVersions(String projectId) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.GET);
        baseApi.setBaseUri(JiraService.jiraUrl);
        baseApi.setBasicAuth(JiraService.jiraUserName, JiraService.jiraPassword);
        baseApi.setBasePath("api/latest/project/{projectId}/versions");
        baseApi.addPathParam("projectId", projectId);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        GetAllVersionsResponseDTO[] getAllVersionsResponse = JacksonJsonImpl.getInstance().fromJson(response.asString(), GetAllVersionsResponseDTO[].class);
        List<GetAllVersionsResponseDTO> getAllVersionsResponseDTO = Arrays.asList(getAllVersionsResponse);
        return getAllVersionsResponseDTO;
    }

    public static List<GetAllIssueTypesResponseDTO> getAllIssueType() throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.GET);
        baseApi.setBaseUri(JiraService.jiraUrl);
        baseApi.setBasicAuth(JiraService.jiraUserName, JiraService.jiraPassword);
        baseApi.setBasePath("api/latest/project");
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        GetAllIssueTypesResponseDTO[] getAllIssueTypesResponseDTOS = JacksonJsonImpl.getInstance().fromJson(response.asString(), GetAllIssueTypesResponseDTO[].class);
        List<GetAllIssueTypesResponseDTO> getAllIssueTypesResponseDTO = Arrays.asList(getAllIssueTypesResponseDTOS);
        return getAllIssueTypesResponseDTO;
    }

    public static ProjectMetadataResponseDTO getProjectMetadata(String projectId) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.GET);
        baseApi.setBaseUri(JiraService.jiraUrl);
        baseApi.setBasicAuth(JiraService.jiraUserName, JiraService.jiraPassword);
        baseApi.setBasePath("api/latest/project/{projectId}");
        baseApi.addPathParam("projectId", projectId);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        ProjectMetadataResponseDTO projectMetadataResponseDTOS = JacksonJsonImpl.getInstance().fromJson(response.asString(), ProjectMetadataResponseDTO.class);
        return projectMetadataResponseDTOS;
    }

    public static CreateIssueResponseDTO createJiraIssue(String request) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.POST);
        baseApi.setBaseUri(JiraService.jiraUrl);
        baseApi.setContentType(ContentType.JSON);
        baseApi.setBasicAuth(JiraService.jiraUserName, JiraService.jiraPassword);
        baseApi.setBasePath("api/latest/issue");
        baseApi.setBody(request);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        CreateIssueResponseDTO createIssueResponseDTO = JacksonJsonImpl.getInstance().fromJson(response.asString(), CreateIssueResponseDTO.class);
        return createIssueResponseDTO;
    }

    public static Response editJiraIssue(String request, String jiraID) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.PUT);
        baseApi.setBaseUri(JiraService.jiraUrl);
        baseApi.setContentType(ContentType.JSON);
        baseApi.setBasicAuth(JiraService.jiraUserName, JiraService.jiraPassword);
        baseApi.setBasePath("api/latest/issue/{JIRAID}");
        baseApi.addPathParam("JIRAID",jiraID);
        baseApi.setBody(request);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        return response;
    }

}
