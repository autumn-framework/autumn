package com.dt.autumn.reporting.dataObjects;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ClassTestStatusDTO implements Comparable<PackageTestStatusDTO> {


    String className;
    int totalTest;
    int totalPass;
    int totalFail;
    int totalSkip;
    int productionBugs;


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

    @Override
    public int compareTo(PackageTestStatusDTO o) {
        return o.getTotalFail() - this.getTotalFail();
    }


}
