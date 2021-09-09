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

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddTestsToCycleRequestDTO {

    @JsonProperty("components")
    private String components;
    @JsonProperty("cycleId")
    private String cycleId;
    @JsonProperty("fromCycleId")
    private String fromCycleId;
    @JsonProperty("fromVersionId")
    private String fromVersionId;
    @JsonProperty("hasDefects")
    private Boolean hasDefects;
    @JsonProperty("labels")
    private String labels;
    @JsonProperty("method")
    private String method;
    @JsonProperty("priorities")
    private String priorities;
    @JsonProperty("projectId")
    private String projectId;
    @JsonProperty("statuses")
    private String statuses;
    @JsonProperty("versionId")
    private String versionId;
    @JsonProperty("folderId")
    private String folderId;
    @JsonProperty("assigneeType")
    private String assigneeType;
    @JsonProperty("issues")
    private List<String> issues;

}
