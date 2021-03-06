package com.autumn.reporting.listeners;

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

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    static int RETRYLIMIT = 0;
    int counter = 0;

    public static void setRetryLimit(int retryLimit) {
        RetryAnalyzer.RETRYLIMIT = retryLimit;
    }

    @Override
    public boolean retry(ITestResult result) {
        if (counter < RetryAnalyzer.RETRYLIMIT) {
            counter++;
            return true;
        } else {
            return false;
        }

    }

}
