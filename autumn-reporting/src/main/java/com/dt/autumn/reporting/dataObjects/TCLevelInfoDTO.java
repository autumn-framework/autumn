package com.dt.autumn.reporting.dataObjects;

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
