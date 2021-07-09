package com.dt.autumn.reporting.jacksonProcessor;

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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;


public class JacksonJsonProcessor {

    private static final ThreadLocal<ObjectMapper> tlObjectMapper = new ThreadLocal<ObjectMapper>();
    private static volatile JacksonJsonProcessor _instance;

    public JacksonJsonProcessor() {

    }

    public static JacksonJsonProcessor getInstance() {
        if (_instance == null) {
                synchronized (JacksonJsonProcessor.class) {
                if (_instance == null) {
                    _instance = new JacksonJsonProcessor();
                }
            }
        }
        return _instance;
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = tlObjectMapper.get();
        if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            objectMapper.setVisibilityChecker(
                    objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            tlObjectMapper.set(objectMapper);
        }
        return objectMapper;
    }

    public <T> String toJSon(T t) throws IOException {
        try {
            String json = getObjectMapper().writeValueAsString(t);
            return json;
        } catch (JsonGenerationException jge) {
            throw jge;
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    public <T> T fromJson(String json,
                          Class<T> clazz) throws IOException {

        try {
            T t = getObjectMapper().readValue(json, clazz);
            return t;
        } catch (JsonParseException jpe) {
            throw jpe;
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (IOException ioe) {
            throw ioe;
        }
    }

    public <T> JsonNode toJsonNode(String json) throws IOException {
        try {
            JsonNode jsonNode = getObjectMapper().readTree(json);
            return jsonNode;
        } catch (JsonGenerationException jge) {
            throw jge;
        } catch (JsonMappingException jme) {
            throw jme;
        } catch (IOException ioe) {
            throw ioe;
        }
    }
}
