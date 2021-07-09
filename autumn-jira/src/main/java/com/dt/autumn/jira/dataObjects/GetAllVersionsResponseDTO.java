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
public class GetAllVersionsResponseDTO {

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
