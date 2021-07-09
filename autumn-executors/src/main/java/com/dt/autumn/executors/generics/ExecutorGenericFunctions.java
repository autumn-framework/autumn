package com.dt.autumn.executors.generics;

import com.dt.autumn.executors.listeners.SkipAnalyserListener;

public class ExecutorGenericFunctions {

    /**
     * Set the parameter value of the condition on basis of which SkipTest case should work
     * @param skipCondition
     */
    public static void setSkipCondition(String skipCondition){
        SkipAnalyserListener.skipCondition = skipCondition;
    }

}
