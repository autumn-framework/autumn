package com.dt.autumn.jira.dataObjects;

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
