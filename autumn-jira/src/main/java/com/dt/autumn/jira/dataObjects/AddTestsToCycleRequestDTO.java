package com.dt.autumn.jira.dataObjects;

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