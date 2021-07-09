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
