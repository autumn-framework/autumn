package com.dt.autumn.reporting.dataObjects;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SuiteTestStatusDTO {

    int totalTest;
    int totalPass;
    int totalFail;
    int totalSkip;
    int productionBugs;
    HashMap<String, PackageTestStatusDTO> packageTestStatusDTOHashMap;

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
}
