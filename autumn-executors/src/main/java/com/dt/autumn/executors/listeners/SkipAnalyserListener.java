package com.dt.autumn.executors.listeners;

import com.dt.autumn.executors.annotations.SkipCondition;
import com.dt.autumn.reporting.globals.GlobalVariables;
import org.testng.*;
import java.lang.reflect.Method;

public class SkipAnalyserListener implements ITestListener {

    public static String skipCondition;

    @Override
    public synchronized void onTestStart(ITestResult result) {
        Method method = result.getMethod().getConstructorOrMethod().getMethod();
        Class callingClass = result.getTestClass().getRealClass();
        if ((method!=null) && (method.isAnnotationPresent(SkipCondition.class)))
        {
            SkipCondition skip = method.getAnnotation(SkipCondition.class);
            String[] arr = skip.SkipConditions();
            for (int i = 0; i < arr.length; i++) {
                if(arr[i].equalsIgnoreCase(skipCondition)){
                    throw new SkipException("These Tests are marked to be skipped");
                }
                }
        }else if((callingClass!=null) && (callingClass.isAnnotationPresent(SkipCondition.class))){
            SkipCondition skip = (SkipCondition) callingClass.getAnnotation(SkipCondition.class);
            String[] arr = skip.SkipConditions();
            for (int i = 0; i <arr.length; i++) {
                if(arr[i].equalsIgnoreCase(skipCondition)){
                    throw new SkipException("These Tests are marked to be skipped");
                }
            }
        }
    }
}
