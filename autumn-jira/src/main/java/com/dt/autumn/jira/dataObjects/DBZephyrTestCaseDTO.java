package com.dt.autumn.jira.dataObjects;

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
