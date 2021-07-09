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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTestFolderResponseDTO {

    @JsonProperty("id")
    private String id;
    @JsonProperty("responseMessage")
    private String responseMessage;
    @JsonProperty("projectId")
    private Integer projectId;
    @JsonProperty("versionId")
    private Integer versionId;
    @JsonProperty("projectKey")
    private String projectKey;
    @JsonProperty("versionName")
    private String versionName;
    @JsonProperty("cycleId")
    private Integer cycleId;
    @JsonProperty("cycleName")
    private String cycleName;
    @JsonProperty("error")
    private String error;

}
