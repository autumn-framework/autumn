package com.dt.autumn.utils.exceptions;

public class ErrorEncrypting extends  RuntimeException{


    public ErrorEncrypting() {
    }

    private ErrorEncrypting(String var1) {
        super(var1);
    }

    public ErrorEncrypting(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public ErrorEncrypting(boolean var1) {
        this(String.valueOf(var1));
    }

    public ErrorEncrypting(char var1) {
        this(String.valueOf(var1));
    }

    public ErrorEncrypting(int var1) {
        this(String.valueOf(var1));
    }

    public ErrorEncrypting(long var1) {
        this(String.valueOf(var1));
    }

    public ErrorEncrypting(float var1) {
        this(String.valueOf(var1));
    }

    public ErrorEncrypting(double var1) {
        this(String.valueOf(var1));
    }

    public ErrorEncrypting(String var1, Throwable var2) {
        super(var1, var2);
    }


}
