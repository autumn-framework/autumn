package com.dt.autumn.reporting.dataObjects;

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

