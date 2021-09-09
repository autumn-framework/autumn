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
public class TCLevelInfoDTO {

    int tcStatus;
    String methodName = null;
    Boolean isProductionBug = false;
    String packageName = null;
    Object[] parameters;
    String className = null;
    Throwable exception = null;
    String description = null;
    String tcReportingCategory = null;

}
