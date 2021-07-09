package com.dt.autumn.executors.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SkipCondition {
    String[] SkipConditions();
}
