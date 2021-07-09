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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllCyclesResponseDTO {

    @JsonProperty("totalExecutions")
    private Integer totalExecutions;
    @JsonProperty("endDate")
    private String endDate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("totalExecuted")
    private Integer totalExecuted;
    @JsonProperty("started")
    private String started;
    @JsonProperty("versionName")
    private String versionName;
    @JsonProperty("cycleOrderId")
    private Integer cycleOrderId;
    @JsonProperty("isExecutionWorkflowEnabledForProject")
    private Boolean isExecutionWorkflowEnabledForProject;
    @JsonProperty("expand")
    private String expand;
    @JsonProperty("projectKey")
    private String projectKey;
    @JsonProperty("versionId")
    private Integer versionId;
    @JsonProperty("environment")
    private String environment;
    @JsonProperty("createdDate")
    private String createdDate;
    @JsonProperty("totalCycleExecutions")
    private Integer totalCycleExecutions;
    @JsonProperty("totalDefects")
    private Integer totalDefects;
    @JsonProperty("isTimeTrackingEnabled")
    private Boolean isTimeTrackingEnabled;
    @JsonProperty("build")
    private String build;
    @JsonProperty("ended")
    private String ended;
    @JsonProperty("name")
    private String name;
    @JsonProperty("modifiedBy")
    private String modifiedBy;
    @JsonProperty("projectId")
    private Integer projectId;
    @JsonProperty("startDate")
    private String startDate;
    @JsonProperty("executionSummaries")
    private ExecutionSummaries executionSummaries;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("totalFolders")
    private Integer totalFolders;
    @JsonProperty("createdByDisplay")
    private String createdByDisplay;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class ExecutionSummaries {

        @JsonProperty("executionSummary")
        private List<Object> executionSummary = null;

    }
}
