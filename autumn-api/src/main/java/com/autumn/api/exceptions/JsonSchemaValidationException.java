package com.autumn.api.exceptions;

/*-
 * #%L
 * autumn-api
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
