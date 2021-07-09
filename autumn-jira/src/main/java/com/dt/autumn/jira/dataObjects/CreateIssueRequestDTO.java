package com.dt.autumn.jira.dataObjects;

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
