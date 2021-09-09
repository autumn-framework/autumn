package com.autumn.reporting.dataObjects;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class APIStatusTimeDTO {

    String basePath;
    Long averageResponseTime = 0L;
    Long minTime = 99999L;
    Long maxTime = 0L;
    Long totalHits = 0L;

    public APIStatusTimeDTO(String basePath) {
        this.basePath = basePath;
    }

    public void setResponseTime(Long responseTime){
        if(responseTime!=0){
            setAverageResponseTime(responseTime);
            setMaxTime(responseTime);
            setMinTime(responseTime);
            this.totalHits++;
        }
    }

    public void setAverageResponseTime(Long responseTime) {
        if(this.averageResponseTime!=0){
            this.averageResponseTime = ((this.averageResponseTime + responseTime) / 2);
        }else{
            this.averageResponseTime=responseTime;
        }
    }

    public void setMinTime(Long minTime) {
        if (this.minTime > minTime)
            this.minTime = minTime;
    }

    public void setMaxTime(Long maxTime) {
        if (this.maxTime < maxTime)
            this.maxTime = maxTime;
    }

}
