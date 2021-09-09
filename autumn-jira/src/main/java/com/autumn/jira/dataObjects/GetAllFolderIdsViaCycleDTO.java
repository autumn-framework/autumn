package com.autumn.jira.dataObjects;

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
