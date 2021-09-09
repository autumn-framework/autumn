package com.autumn.executors.testSetup;

/*-
 * #%L
 * autumn-executors
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


public class TestEnvironmentSetup {

    //Values for now are regression, sanity, smoke
    public static final String TESTTYPE = System.getProperty("TestingType", null);
    //Values can be methods, system, package, group, class
    public static final String SUITETYPE = System.getProperty("suiteType", "system");
    public static final int THREADCOUNT = Integer.parseInt(System.getProperty("ThreadCount", "1"));


    public static void main(String[] args) throws Exception {
        CreateTestNGXml.createXml(TestEnvironmentSetup.SUITETYPE, TestEnvironmentSetup.TESTTYPE, TestEnvironmentSetup.THREADCOUNT, args[1], args[0]);
    }
}
