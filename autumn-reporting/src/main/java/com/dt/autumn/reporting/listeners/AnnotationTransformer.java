package com.dt.autumn.reporting.listeners;

import com.dt.autumn.reporting.listeners.RetryAnalyzer;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationTransformer implements IAnnotationTransformer {

    private static Boolean retryFlag=true;

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    public static Boolean getRetryFlag() {
        return retryFlag;
    }

    public static void setRetryFlag(Boolean retryFlag) {
        AnnotationTransformer.retryFlag = retryFlag;
    }
}