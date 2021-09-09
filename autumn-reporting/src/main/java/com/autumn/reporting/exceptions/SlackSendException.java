package com.autumn.reporting.exceptions;

/*-
 * #%L
 * autumn-reporting
 * %%
 * Copyright (C) 2021 Deutsche Telekom AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
