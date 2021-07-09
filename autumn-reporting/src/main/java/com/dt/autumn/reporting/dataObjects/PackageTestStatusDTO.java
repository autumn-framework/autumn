package com.dt.autumn.reporting.dataObjects;

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

import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PackageTestStatusDTO implements Comparable<PackageTestStatusDTO> {

    String packageName;
    int totalTest;
    int totalPass;
    int totalFail;
    int totalSkip;
    int productionBugs;
    HashMap<String, ClassTestStatusDTO> classTestStatusDTOHashMap;


    public void incrementTotalTest(){
        this.totalTest++;
    }

    public void incrementTotalPass(){
        this.totalPass++;
    }

    public void incrementTotalFail(){
        this.totalFail++;
    }

    public void incrementTotalSkip(){
        this.totalSkip++;
    }

    public void incrementProductionBugs(){
        this.productionBugs++;
    }

    public HashMap<String, ClassTestStatusDTO> getClassTestStatusDTOHashMap() {
        return classTestStatusDTOHashMap;
    }

    public void setClassTestStatusDTOHashMap(HashMap<String, ClassTestStatusDTO> classTestStatusDTOHashMap) {
        this.classTestStatusDTOHashMap = classTestStatusDTOHashMap;
    }

    @Override
    public int compareTo(PackageTestStatusDTO o) {
        return o.getTotalFail() - this.getTotalFail();
    }
}

