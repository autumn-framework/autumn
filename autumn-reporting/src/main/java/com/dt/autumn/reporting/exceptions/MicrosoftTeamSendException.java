package com.dt.autumn.reporting.exceptions;

public class MicrosoftTeamSendException extends RuntimeException{


    public MicrosoftTeamSendException() {
    }

    private MicrosoftTeamSendException(String var1) {
        super(var1);
    }

    public MicrosoftTeamSendException(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public MicrosoftTeamSendException(boolean var1) {
        this(String.valueOf(var1));
    }

    public MicrosoftTeamSendException(char var1) {
        this(String.valueOf(var1));
    }

    public MicrosoftTeamSendException(int var1) {
        this(String.valueOf(var1));
    }

    public MicrosoftTeamSendException(long var1) {
        this(String.valueOf(var1));
    }

    public MicrosoftTeamSendException(float var1) {
        this(String.valueOf(var1));
    }

    public MicrosoftTeamSendException(double var1) {
        this(String.valueOf(var1));
    }

    public MicrosoftTeamSendException(String var1, Throwable var2) {
        super(var1, var2);
    }


}
