package com.dt.autumn.jira.dataObjects;

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
