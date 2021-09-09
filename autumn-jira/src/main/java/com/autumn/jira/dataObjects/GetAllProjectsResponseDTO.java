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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAllProjectsResponseDTO {

    @JsonProperty("expand")
    private String expand;
    @JsonProperty("self")
    private String self;
    @JsonProperty("id")
    private String id;
    @JsonProperty("key")
    private String key;
    @JsonProperty("name")
    private String name;
    @JsonProperty("avatarUrls")
    private AvatarUrls avatarUrls;
    @JsonProperty("projectTypeKey")
    private String projectTypeKey;
    @JsonProperty("projectCategory")
    private ProjectCategory projectCategory;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class ProjectCategory {

        @JsonProperty("self")
        private String self;
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public class AvatarUrls {

        @JsonProperty("48x48")
        private String _48x48;
        @JsonProperty("24x24")
        private String _24x24;
        @JsonProperty("16x16")
        private String _16x16;
        @JsonProperty("32x32")
        private String _32x32;

    }

}
