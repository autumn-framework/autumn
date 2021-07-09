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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DBZephyrTestCaseDTO {

    @JsonProperty("_id")
    private Id id;
    @JsonProperty("PACKAGENAME")
    private String packageName;
    @JsonProperty("CLASSNAME")
    private String className;
    @JsonProperty("METHODNAME")
    private String methodName;
    @JsonProperty("DESCRIPTION")
    private String description;
    @JsonProperty("JIRAID")
    private String jiraId;


    @Getter
    @Setter
    public class Id {

        @JsonProperty("$oid")
        private String $oid;

    }


}
