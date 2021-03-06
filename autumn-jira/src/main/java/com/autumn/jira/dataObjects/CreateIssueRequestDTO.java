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
public class CreateIssueRequestDTO {


    @JsonProperty("fields")
    private Fields fields;


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("fields", fields).toString();
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Fields {

        @JsonProperty("project")
        private FieldDetails project;
        @JsonProperty("summary")
        private String summary;
        @JsonProperty("issuetype")
        private FieldDetails issuetype;
        @JsonProperty("labels")
        private List<Object> labels = null;
        @JsonProperty("versions")
        private List<Object> versions = null;
        @JsonProperty("description")
        private String description;
        @JsonProperty("fixVersions")
        private List<Object> fixVersions = null;
        @JsonProperty("components")
        private List<Object> components = null;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Accessors(chain = true)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public class FieldDetails {

            @JsonProperty("id")
            private String id;


        }

    }

}
