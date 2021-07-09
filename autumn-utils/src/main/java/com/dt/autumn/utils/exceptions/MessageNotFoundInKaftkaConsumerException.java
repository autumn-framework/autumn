package com.dt.autumn.utils.exceptions;

public class MessageNotFoundInKaftkaConsumerException extends RuntimeException {
    public MessageNotFoundInKaftkaConsumerException() {
    }

    private MessageNotFoundInKaftkaConsumerException(String var1) {
        super(var1);
    }

    public MessageNotFoundInKaftkaConsumerException(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public MessageNotFoundInKaftkaConsumerException(boolean var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKaftkaConsumerException(char var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKaftkaConsumerException(int var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKaftkaConsumerException(long var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKaftkaConsumerException(float var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKaftkaConsumerException(double var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKaftkaConsumerException(String var1, Throwable var2) {
        super(var1, var2);
    }
}
