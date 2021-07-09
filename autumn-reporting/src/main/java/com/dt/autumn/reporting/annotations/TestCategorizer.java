package com.dt.autumn.reporting.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestCategorizer {
        String Category();
    }

