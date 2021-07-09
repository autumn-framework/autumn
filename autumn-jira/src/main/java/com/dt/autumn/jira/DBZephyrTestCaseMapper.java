package com.dt.autumn.jira;

import com.dt.autumn.jira.dataObjects.DBZephyrTestCaseDTO;
import com.dt.autumn.utils.databaseUtils.MongoDBUtil;
import com.dt.autumn.api.jsonProcessor.JacksonJsonImpl;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DBZephyrTestCaseMapper {

    private static Map<String, DBZephyrTestCaseDTO> zephyrDBMap = new LinkedHashMap<>();
    private static Map<String, List<String>> jiraIDsList = new LinkedHashMap<>();
    private static final String SEPERATOR = "#!";
    private static volatile DBZephyrTestCaseMapper instance;
    private static String zephyrTCMappingUrl;
    private static String zephyrTCMappingCollection;

    private DBZephyrTestCaseMapper() {

    }

    public static DBZephyrTestCaseMapper getInstance() {
        if (instance == null) {
            synchronized (DBZephyrTestCaseMapper.class) {
                if (instance == null) {
                    instance = new DBZephyrTestCaseMapper();
                }
            }
        }
        return instance;
    }

    public String getZephyrTCMappingUrl() {
        return zephyrTCMappingUrl;
    }

    public String getZephyrTCMappingCollection() {
        return zephyrTCMappingCollection;
    }

    public void setZephyrTCMappingUrl(String zephyrTCMappingUrl) {
        DBZephyrTestCaseMapper.zephyrTCMappingUrl = zephyrTCMappingUrl;
    }

    public void setZephyrTCMappingCollection(String zephyrTCMappingCollection) {
        DBZephyrTestCaseMapper.zephyrTCMappingCollection = zephyrTCMappingCollection;
    }

    public void fetchDataFromDB() {
        try {
            List<String> homeGatewaysZephyr = MongoDBUtil.getInstance().findAllDocuments(this.getZephyrTCMappingUrl(), this.getZephyrTCMappingCollection());
            if (homeGatewaysZephyr != null && homeGatewaysZephyr.size() > 0) {
                for (String zephyrDe : homeGatewaysZephyr) {
                    DBZephyrTestCaseDTO dbZephyrTestCaseDTO = JacksonJsonImpl.getInstance().fromJson(zephyrDe, DBZephyrTestCaseDTO.class);
                    zephyrDBMap.put(dbZephyrTestCaseDTO.getPackageName() + SEPERATOR + dbZephyrTestCaseDTO.getClassName() + SEPERATOR + dbZephyrTestCaseDTO.getMethodName(), dbZephyrTestCaseDTO);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJiraId(String packageName, String className, String methodName, String description, String jiraId) {
        String key = packageName + SEPERATOR + className + SEPERATOR + methodName;
        if (zephyrDBMap.containsKey(key)) {
            jiraId = zephyrDBMap.get(key).getJiraId();
            if (jiraId == null || jiraId.equalsIgnoreCase("") || jiraId.equalsIgnoreCase("null")) {
                jiraId = newDBZephyrTestCase_Doc(packageName, className, description);
            }
            if (!zephyrDBMap.get(key).getDescription().equalsIgnoreCase(description)) {
                try {
                    JiraManager.editTest(description, null, null, jiraId);
                } catch (IOException e) {
                }
            }
        } else {
            if (jiraId.equalsIgnoreCase("")) {
                jiraId = newDBZephyrTestCase_Doc(packageName, className, description);
            }
        }
        addUpdateZephyrDB(packageName, className, methodName, description, jiraId);
        return jiraId;

    }

    public String newDBZephyrTestCase_Doc(String packageName, String className, String description) {
        try {
            List<Object> labels = new LinkedList<>();
            labels.add(className);
            labels.add(packageName);
            String jiraId = JiraManager.uploadZephyrTestCase(description, description, labels);
            return jiraId;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addUpdateZephyrDB(String packageName, String className, String methodName, String description, String jiraId) {
        String key = packageName + SEPERATOR + className + SEPERATOR + methodName;
        if (zephyrDBMap.containsKey(key)) {
            Boolean updateDBFlag = false;
            if (!zephyrDBMap.get(key).getDescription().equalsIgnoreCase(description)) {
                zephyrDBMap.get(key).setDescription(description);
                updateDBFlag = true;
            }
            if (zephyrDBMap.get(key).getJiraId() == null || zephyrDBMap.get(key).getJiraId().equalsIgnoreCase("")) {
                zephyrDBMap.get(key).setJiraId(jiraId);
                updateDBFlag = true;
            }
            if (updateDBFlag) {
                updateDocumentZephyr(packageName, className, methodName, description, jiraId);
            }
        } else {
            synchronized (DBZephyrTestCaseMapper.class) {
                List<String> jiraIds = new LinkedList<>();
                if (jiraIDsList.containsKey(packageName.toUpperCase())) {
                    jiraIds = jiraIDsList.get(packageName.toUpperCase());
                    jiraIds.add(jiraId);
                } else {
                    jiraIds.add(jiraId);
                }
                jiraIDsList.put(packageName.toUpperCase(), jiraIds);
            }
            insertNewDocuments(createJson(packageName, className, methodName, description, jiraId));
            DBZephyrTestCaseDTO dbZephyrTestCaseDTO = new DBZephyrTestCaseDTO();
            dbZephyrTestCaseDTO.setDescription(description).setJiraId(jiraId).setClassName(className).setPackageName(packageName).setMethodName(methodName);
            zephyrDBMap.put(key, dbZephyrTestCaseDTO);
        }
    }

    public String createJson(String packageName, String className, String methodName, String description, String jiraId) {
        String zephyDBJson = "{" +
                "  \"PACKAGENAME\":\"" + packageName + "\"," +
                "  \"CLASSNAME\":\"" + className + "\"," +
                "  \"METHODNAME\":\"" + methodName + "\"," +
                "  \"DESCRIPTION\":\"" + description + "\"," +
                "  \"JIRAID\":\"" + jiraId + "\"" +
                "}";
        return zephyDBJson;
    }

    public void insertNewDocuments(String zephyDBJson) {
        try {
            Document document = JacksonJsonImpl.getInstance().fromJson(zephyDBJson, Document.class);
            MongoDBUtil.getInstance().insertDocument(this.getZephyrTCMappingUrl(),document, this.getZephyrTCMappingCollection());
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public void updateDocumentZephyr(String packageName, String className, String methodName, String description, String jiraId) {
        Bson f1 = Filters.eq("PACKAGENAME", packageName);
        Bson f2 = Filters.eq("CLASSNAME", className);
        Bson f3 = Filters.eq("METHODNAME", methodName);
        Bson searchFilter = Filters.and(f1, f2, f3);
        Bson updateFilter = Updates.combine(Updates.set("JIRAID", jiraId), Updates.set("DESCRIPTION", description));
        MongoDBUtil.getInstance().updateManyDocumentWithFilter(this.getZephyrTCMappingUrl(), this.getZephyrTCMappingCollection(), searchFilter, updateFilter);
    }

    public void addTCZephyrTestCycle() {
        if (jiraIDsList.size() > 0) {
            for (String packageName : jiraIDsList.keySet()) {
                try {
                    ZephyrManager.addTCZephyrCycle(packageName, jiraIDsList.get(packageName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
