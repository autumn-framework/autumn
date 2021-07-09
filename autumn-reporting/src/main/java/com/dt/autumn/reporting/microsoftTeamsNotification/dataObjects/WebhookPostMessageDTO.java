package com.dt.autumn.reporting.microsoftTeamsNotification.dataObjects;

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
public class WebhookPostMessageDTO {

    @JsonProperty("@type")
    private String type;
    @JsonProperty("@context")
    private String context;
    @JsonProperty("themeColor")
    private String themeColor;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("sections")
    private List<Section> sections = null;
    @JsonProperty("potentialAction")
    private List<PotentialAction> potentialAction = null;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class PotentialAction {

        @JsonProperty("@type")
        private String type;
        @JsonProperty("name")
        private String name;
        @JsonProperty("target")
        private List<String> target = null;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Section {

        @JsonProperty("activityTitle")
        private String activityTitle;
        @JsonProperty("activitySubtitle")
        private String activitySubtitle;
        @JsonProperty("facts")
        private List<Fact> facts = null;
        @JsonProperty("markdown")
        private Boolean markdown;
        @JsonProperty("title")
        private String title;


        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Accessors(chain = true)
        public static class Fact {

            @JsonProperty("name")
            private String name;
            @JsonProperty("value")
            private String value;


        }

    }


}
