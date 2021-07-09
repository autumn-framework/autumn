package com.dt.autumn.reporting.slackNotification.dataObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class WebhookPostMessageDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("icon_emoji")
    private String icon_emoji;
    @JsonProperty("attachments")
    List<Attachments> attachments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Attachments{
        @JsonProperty("color")
        private String color;
        @JsonProperty("title")
        private String title;
        @JsonProperty("text")
        private String text;
        @JsonProperty("fields")
        List<Fields> fields;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Fields{
        @JsonProperty("title")
        private String title;
        @JsonProperty("value")
        private String value;
        @JsonProperty("short")
        private Boolean Short;
    }
}
