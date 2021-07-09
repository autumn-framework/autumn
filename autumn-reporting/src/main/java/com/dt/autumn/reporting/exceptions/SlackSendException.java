package com.dt.autumn.reporting.exceptions;

public class SlackSendException extends RuntimeException{


    public SlackSendException() {
    }

    private SlackSendException(String var1) {
        super(var1);
    }

    public SlackSendException(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public SlackSendException(boolean var1) {
        this(String.valueOf(var1));
    }

    public SlackSendException(char var1) {
        this(String.valueOf(var1));
    }

    public SlackSendException(int var1) {
        this(String.valueOf(var1));
    }

    public SlackSendException(long var1) {
        this(String.valueOf(var1));
    }

    public SlackSendException(float var1) {
        this(String.valueOf(var1));
    }

    public SlackSendException(double var1) {
        this(String.valueOf(var1));
    }

    public SlackSendException(String var1, Throwable var2) {
        super(var1, var2);
    }


}
