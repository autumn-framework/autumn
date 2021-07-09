package com.dt.autumn.utils.commonUtil;

/*-
 * #%L
 * autumn-utils
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

import java.util.ArrayList;
import java.util.List;

public class ComparatorUtil {

    public Boolean compareList(List<String> actual, List<String> expected) {
        List<String> expectedEntryL = new ArrayList<>(expected);
        List<String> actualentryL = new ArrayList<>(actual);
        if(actualentryL.size()==0 && expectedEntryL.size()==0){
            return true;
        }
        if (actualentryL.size() == expectedEntryL.size()) {
            Boolean flag = false;
            for (String expectedEntry : expectedEntryL) {
                List<String> actualEntryCopy = new ArrayList<>(actualentryL);
                for (String actualEntry : actualEntryCopy) {
                    if (actualEntry.equalsIgnoreCase(expectedEntry)) {
                        flag = true;
                        actualentryL.remove(actualEntry);
                        break;
                    } else {
                        flag = false;
                    }
                }
                if (flag == false) {
                    break;
                }
            }
            return flag;
        } else {
            return false;
        }
    }

}
