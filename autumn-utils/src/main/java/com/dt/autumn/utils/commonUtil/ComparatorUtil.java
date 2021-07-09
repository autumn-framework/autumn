package com.dt.autumn.utils.commonUtil;

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
