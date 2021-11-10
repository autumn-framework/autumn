package com.autumn.utils.exceptions;

/*-
 * #%L
 * autumn-utils
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

public class MessageNotFoundInKafkaConsumerException extends RuntimeException {
    public MessageNotFoundInKafkaConsumerException() {
    }

    private MessageNotFoundInKafkaConsumerException(String var1) {
        super(var1);
    }

    public MessageNotFoundInKafkaConsumerException(Object var1) {
        this(String.valueOf(var1));
        if (var1 instanceof Throwable) {
            this.initCause((Throwable)var1);
        }

    }

    public MessageNotFoundInKafkaConsumerException(boolean var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKafkaConsumerException(char var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKafkaConsumerException(int var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKafkaConsumerException(long var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKafkaConsumerException(float var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKafkaConsumerException(double var1) {
        this(String.valueOf(var1));
    }

    public MessageNotFoundInKafkaConsumerException(String var1, Throwable var2) {
        super(var1, var2);
    }
}
