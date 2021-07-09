package com.dt.autumn.jira.dataObjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectMetadataResponseDTO {


    @JsonProperty("expand")
    private String expand;
    @JsonProperty("self")
    private String self;
    @JsonProperty("id")
    private String id;
    @JsonProperty("key")
    private String key;
    @JsonProperty("description")
    private String description;
    @JsonProperty("lead")
    private Lead lead;
    @JsonProperty("components")
    private List<Component> components = null;
    @JsonProperty("issueTypes")
    private List<IssueType> issueTypes = null;
    @JsonProperty("assigneeType")
    private String assigneeType;
    @JsonProperty("versions")
    private List<Version> versions = null;
    @JsonProperty("name")
    private String name;
    @JsonProperty("roles")
    private Roles roles;
    @JsonProperty("avatarUrls")
    private Map<String, String> avatarUrls;
    @JsonProperty("projectTypeKey")
    private String projectTypeKey;
    @JsonProperty("archived")
    private Boolean archived;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Lead {

        @JsonProperty("self")
        private String self;
        @JsonProperty("key")
        private String key;
        @JsonProperty("name")
        private String name;
        @JsonProperty("avatarUrls")
        private Map<String, String> avatarUrls;
        @JsonProperty("displayName")
        private String displayName;
        @JsonProperty("active")
        private Boolean active;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Roles {

        @JsonProperty("Read-only users")
        private String readOnlyUsers;
        @JsonProperty("Developers")
        private String developers;
        @JsonProperty("Administrators")
        private String administrators;
        @JsonProperty("Mito project administrators")
        private String mitoProjectAdministrators;
        @JsonProperty("Users")
        private String users;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Component {

        @JsonProperty("self")
        private String self;
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("isAssigneeTypeValid")
        private Boolean isAssigneeTypeValid;
        @JsonProperty("description")
        private String description;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Version {

        @JsonProperty("self")
        private String self;
        @JsonProperty("id")
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("archived")
        private Boolean archived;
        @JsonProperty("released")
        private Boolean released;
        @JsonProperty("startDate")
        private String startDate;
        @JsonProperty("userStartDate")
        private String userStartDate;
        @JsonProperty("projectId")
        private Integer projectId;
        @JsonProperty("releaseDate")
        private String releaseDate;
        @JsonProperty("overdue")
        private Boolean overdue;
        @JsonProperty("userReleaseDate")
        private String userReleaseDate;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class IssueType {

        @JsonProperty("self")
        private String self;
        @JsonProperty("id")
        private String id;
        @JsonProperty("description")
        private String description;
        @JsonProperty("iconUrl")
        private String iconUrl;
        @JsonProperty("name")
        private String name;
        @JsonProperty("subtask")
        private Boolean subtask;
        @JsonProperty("avatarId")
        private Integer avatarId;

    }

}
