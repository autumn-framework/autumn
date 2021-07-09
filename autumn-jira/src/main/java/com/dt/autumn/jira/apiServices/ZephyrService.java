package com.dt.autumn.jira.apiServices;

import com.dt.autumn.jira.dataObjects.*;
import com.dt.autumn.api.BaseApi;
import com.dt.autumn.api.ContentType;
import com.dt.autumn.api.MethodType;
import com.dt.autumn.api.jsonProcessor.JacksonJsonImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.*;

public class ZephyrService {


    public static CreateTestCycleResponseDTO createTestCycle(String projectId, String versionId, String testRepoCycleName) throws IOException {

        return createTestCycle(projectId,versionId,testRepoCycleName,"");
    }

    public static CreateTestCycleResponseDTO createTestCycle(String projectId, String versionId, String testRepoCycleName, String cloneCycleId) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.POST);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setContentType(ContentType.JSON);
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/cycle");
        CreateTestCycleRequestDTO createTestCycleRequestDTO = new CreateTestCycleRequestDTO();
        createTestCycleRequestDTO.setClonedCycleId(cloneCycleId).setName(testRepoCycleName).setDescription("Created by Automation Suite").setBuild("").setEnvironment("").setStartDate("22/Sep/20").setEndDate("22/Sep/20");
        createTestCycleRequestDTO.setProjectId(projectId).setVersionId(versionId);
        baseApi.setBody(createTestCycleRequestDTO);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        CreateTestCycleResponseDTO createTestCycleResponseDTO = JacksonJsonImpl.getInstance().fromJson(response.asString(), CreateTestCycleResponseDTO.class);
        return createTestCycleResponseDTO;
    }


    public static CreateTestFolderResponseDTO createFolderTestCycle(String projectId, String versionId, String cycleId, String folderName, String cloneFolderId) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.POST);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setContentType(ContentType.JSON);
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/folder/create");
        CreateTestFolderRequestDTO createTestFolderRequestDTO = new CreateTestFolderRequestDTO();
        createTestFolderRequestDTO.setClonedFolderId(cloneFolderId).setName(folderName).setDescription("Created by Automation Suite").setCycleId(cycleId);
        createTestFolderRequestDTO.setProjectId(projectId).setVersionId(versionId);
        baseApi.setBody(createTestFolderRequestDTO);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        CreateTestFolderResponseDTO createTestFolderResponseDTO = JacksonJsonImpl.getInstance().fromJson(response.asString(), CreateTestFolderResponseDTO.class);
        return createTestFolderResponseDTO;
    }

    public static Map<String, GetAllCyclesResponseDTO> getAllCycles(String projectId, String versionId) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.GET);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/cycle");
        baseApi.addQueryParam("projectId", projectId);
        baseApi.addQueryParam("versionId", versionId);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();

        JsonObject jsonObject = JsonParser.parseString(response.asString()).getAsJsonObject();
        Map<String, GetAllCyclesResponseDTO> getAllCyclesResponseDTO = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey();
            if (!key.equalsIgnoreCase("recordsCount")) {
                GetAllCyclesResponseDTO getCyclesResponseDTO = JacksonJsonImpl.getInstance().fromJson(entry.getValue().toString(), GetAllCyclesResponseDTO.class);
                getAllCyclesResponseDTO.put(key, getCyclesResponseDTO);
            }
        }
        return getAllCyclesResponseDTO;
    }

    public static GetAllExecutionsResponseDTO getAllExecutions(Map<String, String> queryParams) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.GET);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/execution");
        baseApi.addQueryParams(queryParams);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        GetAllExecutionsResponseDTO getAllExecutionsResponseDTO = JacksonJsonImpl.getInstance().fromJson(response.asString(), GetAllExecutionsResponseDTO.class);
        return getAllExecutionsResponseDTO;
    }

    public static List<GetAllFolderIdsViaCycleDTO> getAllFolderIdsForCycle(String projectId, String cycleId, String versionId) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.GET);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/cycle/{cycleId}/folders");
        baseApi.addPathParam("cycleId", cycleId);
        baseApi.addQueryParam("projectId", projectId);
        baseApi.addQueryParam("versionId", versionId);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        GetAllFolderIdsViaCycleDTO[] getAllFolderIdsViaCycleDTOS = JacksonJsonImpl.getInstance().fromJson(response.asString(), GetAllFolderIdsViaCycleDTO[].class);
        List<GetAllFolderIdsViaCycleDTO> getAllFolderIdsViaCycleDTOList = Arrays.asList(getAllFolderIdsViaCycleDTOS);
        return getAllFolderIdsViaCycleDTOList;
    }


    public static AddTestToCycleResponseDTO addTestsToCycle(AddTestsToCycleRequestDTO addTestsToCycleRequestDTO) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.POST);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setContentType(ContentType.JSON);
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/execution/addTestsToCycle");
        baseApi.setBody(addTestsToCycleRequestDTO);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        AddTestToCycleResponseDTO addTestToCycleResponseDTO = JacksonJsonImpl.getInstance().fromJson(response.asString(), AddTestToCycleResponseDTO.class);
        return addTestToCycleResponseDTO;
    }

    public static BulkTestCaseUpdateResponseDTO bulkUpdateTestCaseStatus(List<String> issueIds, String status) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.PUT);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setContentType(ContentType.JSON);
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/execution/updateBulkStatus");
        BulkTestCaseUpdateRequestDTO bulkTestCaseUpdateRequestDTO = new BulkTestCaseUpdateRequestDTO();
        bulkTestCaseUpdateRequestDTO.setExecutions(issueIds).setStatus(status);
        baseApi.setBody(bulkTestCaseUpdateRequestDTO);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        BulkTestCaseUpdateResponseDTO bulkTestCaseUpdateResponseDTO = JacksonJsonImpl.getInstance().fromJson(response.asString(), BulkTestCaseUpdateResponseDTO.class);
        return bulkTestCaseUpdateResponseDTO;
    }

    public static Response updateTestCaseStatus(String issueId, String status) throws IOException {
        BaseApi baseApi = new BaseApi();
        baseApi.setMethod(MethodType.PUT);
        baseApi.setBaseUri(JiraService.getJiraUrl());
        baseApi.setContentType(ContentType.JSON);
        baseApi.setBasicAuth(JiraService.getJiraUserName(), JiraService.getJiraPassword());
        baseApi.setBasePath("zapi/latest/execution/{issueId}/execute");
        baseApi.addPathParam("issueId", issueId);
        UpdateTestCaseStatusRequestDTO updateTestCaseStatusRequestDTO = new UpdateTestCaseStatusRequestDTO();
        updateTestCaseStatusRequestDTO.setStatus(status);
        baseApi.setBody(updateTestCaseStatusRequestDTO);
        baseApi.setCaptureAPIDetails(false);
        Response response = baseApi.execute();
        return response;
    }


}
