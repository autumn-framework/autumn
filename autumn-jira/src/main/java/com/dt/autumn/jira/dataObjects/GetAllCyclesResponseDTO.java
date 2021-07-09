package com.dt.autumn.jira.dataObjects;

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
