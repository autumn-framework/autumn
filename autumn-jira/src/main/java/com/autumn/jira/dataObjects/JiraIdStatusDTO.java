package com.autumn.jira.dataObjects;

/*-
 * #%L
 * autumn-jira
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

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class JiraIdStatusDTO {

    static Set<String> passedJira = new HashSet<>();
    static Set<String> failedJira = new HashSet<>();

    public static List<String> getPassedJira() {
        return new ArrayList<>(passedJira);
    }

    public static synchronized void addPassedJira(String jiraIds) {
        if(jiraIds!=null) {
            for (String jiraID : jiraIds.split(",")) {
                passedJira.add(jiraID);
            }
        }
    }

    public static synchronized void removePassedJira(String jiraIds){
        if(jiraIds!=null) {
            for (String jiraID : jiraIds.split(",")) {
                passedJira.remove(jiraID);
            }
        }
    }

    public static List<String> getFailedJira() {
        return new ArrayList<>(failedJira);
    }

    public static synchronized void addFailedJira(String jiraIds) {
        if(jiraIds!=null) {
            for (String jiraID : jiraIds.split(",")) {
                failedJira.add(jiraID);
            }
        }
    }

    public static synchronized void removeFailedJira(String jiraIds){
        if(jiraIds!=null) {
            for (String jiraID : jiraIds.split(",")) {
                failedJira.remove(jiraID);
            }
        }
    }


}
