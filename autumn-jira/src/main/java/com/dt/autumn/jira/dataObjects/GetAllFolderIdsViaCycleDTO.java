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
public class GetAllFolderIdsViaCycleDTO {

    @JsonProperty("folderId")
    private Integer folderId;
    @JsonProperty("folderName")
    private String folderName;
    @JsonProperty("folderDescription")
    private String folderDescription;
    @JsonProperty("cycleId")
    private Integer cycleId;
    @JsonProperty("cycleName")
    private String cycleName;
    @JsonProperty("projectId")
    private Integer projectId;
    @JsonProperty("projectKey")
    private String projectKey;
    @JsonProperty("versionName")
    private String versionName;
    @JsonProperty("versionId")
    private Integer versionId;
    @JsonProperty("folderOrderId")
    private Integer folderOrderId;
    @JsonProperty("executionSummaries")
    private ExecutionSummaries executionSummaries;
    @JsonProperty("totalExecutions")
    private Integer totalExecutions;
    @JsonProperty("totalExecuted")
    private Integer totalExecuted;
    @JsonProperty("isTimeTrackingEnabled")
    private Boolean isTimeTrackingEnabled;

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
            @JsonProperty("statusDescription")
            private String statusDescription;


        }

    }


}
