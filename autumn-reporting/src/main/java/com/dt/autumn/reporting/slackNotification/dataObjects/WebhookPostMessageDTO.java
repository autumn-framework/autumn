package com.dt.autumn.reporting.slackNotification.dataObjects;

/*-
 * #%L
 * autumn-reporting
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
