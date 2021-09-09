package com.autumn.executors.generics;

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

import com.autumn.executors.listeners.SkipAnalyserListener;

public class ExecutorGenericFunctions {

    /**
     * Set the parameter value of the condition on basis of which SkipTest case should work
     * @param skipCondition
     */
    public static void setSkipCondition(String skipCondition){
        SkipAnalyserListener.skipCondition = skipCondition;
    }

}
