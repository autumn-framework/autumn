package com.autumn.executors.listeners;

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

import com.autumn.executors.annotations.SkipCondition;
import com.autumn.reporting.globals.GlobalVariables;
import org.testng.*;
import java.lang.reflect.Method;

public class SkipAnalyserListener implements ITestListener {

    public static String skipCondition;

    @Override
    public synchronized void onTestStart(ITestResult result) {
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        Class callingClass = result.getTestClass().getRealClass();
        if ((method!=null) && (method.isAnnotationPresent(SkipCondition.class)))
        {
            SkipCondition skip = method.getAnnotation(SkipCondition.class);
            String[] arr = skip.SkipConditions();
            for (int i = 0; i < arr.length; i++) {
                if(arr[i].equalsIgnoreCase(skipCondition)){
                    throw new SkipException("These Tests are marked to be skipped");
                }
                }
        }else if((callingClass!=null) && (callingClass.isAnnotationPresent(SkipCondition.class))){
            SkipCondition skip = (SkipCondition) callingClass.getAnnotation(SkipCondition.class);
            String[] arr = skip.SkipConditions();
            for (int i = 0; i <arr.length; i++) {
                if(arr[i].equalsIgnoreCase(skipCondition)){
                    throw new SkipException("These Tests are marked to be skipped");
                }
            }
        }
    }
}
