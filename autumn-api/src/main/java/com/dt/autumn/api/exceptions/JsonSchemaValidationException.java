package com.dt.autumn.api.exceptions;

public class JsonSchemaValidationException extends RuntimeException{


    public JsonSchemaValidationException() {
    }

    private JsonSchemaValidationException(String var1) {
        super(var1);
    }

    public JsonSchemaValidationException(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public JsonSchemaValidationException(boolean var1) {
        this(String.valueOf(var1));
    }

    public JsonSchemaValidationException(char var1) {
        this(String.valueOf(var1));
    }

    public JsonSchemaValidationException(int var1) {
        this(String.valueOf(var1));
    }

    public JsonSchemaValidationException(long var1) {
        this(String.valueOf(var1));
    }

    public JsonSchemaValidationException(float var1) {
        this(String.valueOf(var1));
    }

    public JsonSchemaValidationException(double var1) {
        this(String.valueOf(var1));
    }

    public JsonSchemaValidationException(String var1, Throwable var2) {
        super(var1, var2);
    }


}
