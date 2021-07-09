package com.dt.autumn.utils.exceptions;

public class ErrorDecrypting extends RuntimeException{


    public ErrorDecrypting() {
    }

    private ErrorDecrypting(String var1) {
        super(var1);
    }

    public ErrorDecrypting(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public ErrorDecrypting(boolean var1) {
        this(String.valueOf(var1));
    }

    public ErrorDecrypting(char var1) {
        this(String.valueOf(var1));
    }

    public ErrorDecrypting(int var1) {
        this(String.valueOf(var1));
    }

    public ErrorDecrypting(long var1) {
        this(String.valueOf(var1));
    }

    public ErrorDecrypting(float var1) {
        this(String.valueOf(var1));
    }

    public ErrorDecrypting(double var1) {
        this(String.valueOf(var1));
    }

    public ErrorDecrypting(String var1, Throwable var2) {
        super(var1, var2);
    }


}
