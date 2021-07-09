package com.dt.autumn.reporting.globals;

import com.dt.autumn.reporting.dataObjects.APIStatusTimeDTO;
import com.dt.autumn.reporting.dataObjects.SuiteTestStatusDTO;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GlobalVariables {

    public static SuiteTestStatusDTO SUITE_TEST_STATUS_DTO = new SuiteTestStatusDTO(0, 0, 0, 0, 0, new LinkedHashMap<>());
    public static Map<String, APIStatusTimeDTO> API_STATUS_TIME_DTOMAP = new HashMap<>();
    public static Map<String, String> systemInfo = new LinkedHashMap<>();
    //Values for now are regression, sanity, smoke
    public static final String TESTTYPE;
    //Values can be methods, system, package, group, class
    public static final String SUITETYPE;
    public static final String REPORT_BASE_PACKAGE = "reports/Automation";


    static {
        try {
            SUITETYPE = System.getProperty("suiteType", "system");
            TESTTYPE = System.getProperty("TestingType", null);
        } catch (Throwable t) {
            t.printStackTrace();
            throw t;
        }
    }


}
