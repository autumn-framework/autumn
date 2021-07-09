package com.dt.autumn.jira.dataObjects;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllExecutionsResponseDTO {

    @JsonProperty("status")
    private Map<String, Status> status;
    @JsonProperty("executions")
    private List<Execution> executions = null;
    @JsonProperty("currentlySelectedExecutionId")
    private String currentlySelectedExecutionId;
    @JsonProperty("executionSummaries")
    private ExecutionSummaries executionSummaries;
    @JsonProperty("totalExecutions")
    private Integer totalExecutions;
    @JsonProperty("totalExecuted")
    private Integer totalExecuted;
    @JsonProperty("recordsCount")
    private Integer recordsCount;
    @JsonProperty("totalExecutionEstimatedTime")
    private String totalExecutionEstimatedTime;
    @JsonProperty("totalExecutionLoggedTime")
    private String totalExecutionLoggedTime;
    @JsonProperty("executionsToBeLogged")
    private Integer executionsToBeLogged;
    @JsonProperty("isExecutionWorkflowEnabledForProject")
    private Boolean isExecutionWorkflowEnabledForProject;
    @JsonProperty("isTimeTrackingEnabled")
    private Boolean isTimeTrackingEnabled;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Status {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("color")
        private String color;
        @JsonProperty("description")
        private String description;
        @JsonProperty("name")
        private String name;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class ExecutionSummaries {

        @JsonProperty("executionSummary")
        private List<ExecutionSummary> executionSummary = null;


        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Accessors(chain = true)
        public static class ExecutionSummary {

            @JsonProperty("count")
            private Integer count;
            @JsonProperty("statusKey")
            private Integer statusKey;
            @JsonProperty("statusName")
            private String statusName;
            @JsonProperty("statusColor")
            private String statusColor;

        }

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Execution {

        @JsonProperty("id")
        private Long id;
        @JsonProperty("orderId")
        private Long orderId;
        @JsonProperty("executionStatus")
        private String executionStatus;
        @JsonProperty("executionWorkflowStatus")
        private Object executionWorkflowStatus;
        @JsonProperty("executedOn")
        private String executedOn;
        @JsonProperty("executedOnVal")
        private Long executedOnVal;
        @JsonProperty("executedBy")
        private String executedBy;
        @JsonProperty("executedByDisplay")
        private String executedByDisplay;
        @JsonProperty("comment")
        private String comment;
        @JsonProperty("htmlComment")
        private String htmlComment;
        @JsonProperty("cycleId")
        private Long cycleId;
        @JsonProperty("cycleName")
        private String cycleName;
        @JsonProperty("versionId")
        private Long versionId;
        @JsonProperty("versionName")
        private String versionName;
        @JsonProperty("projectId")
        private Long projectId;
        @JsonProperty("folderId")
        private Integer folderId;
        @JsonProperty("folderName")
        private String folderName;
        @JsonProperty("createdBy")
        private String createdBy;
        @JsonProperty("createdByDisplay")
        private String createdByDisplay;
        @JsonProperty("createdByUserName")
        private String createdByUserName;
        @JsonProperty("modifiedBy")
        private String modifiedBy;
        @JsonProperty("createdOn")
        private String createdOn;
        @JsonProperty("createdOnVal")
        private Long createdOnVal;
        @JsonProperty("issueId")
        private Long issueId;
        @JsonProperty("issueKey")
        private String issueKey;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("issueDescription")
        private String issueDescription;
        @JsonProperty("label")
        private String label;
        @JsonProperty("component")
        private String component;
        @JsonProperty("projectKey")
        private String projectKey;
        @JsonProperty("canViewIssue")
        private Boolean canViewIssue;
        @JsonProperty("isIssueEstimateNil")
        private Boolean isIssueEstimateNil;
        @JsonProperty("isExecutionWorkflowEnabled")
        private Boolean isExecutionWorkflowEnabled;
        @JsonProperty("isTimeTrackingEnabled")
        private Boolean isTimeTrackingEnabled;
        @JsonProperty("executionDefectCount")
        private Integer executionDefectCount;
        @JsonProperty("stepDefectCount")
        private Integer stepDefectCount;
        @JsonProperty("totalDefectCount")
        private Integer totalDefectCount;
        @JsonProperty("customFields")
        private String customFields;

    }

}
