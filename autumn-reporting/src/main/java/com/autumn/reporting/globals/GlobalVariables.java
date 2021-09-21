package com.autumn.reporting.globals;

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

import com.autumn.reporting.dataObjects.APIStatusTimeDTO;
import com.autumn.reporting.dataObjects.SuiteTestStatusDTO;

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
