package com.dt.autumn.utils.encryptionUtils;

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

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class EncryptionStandardPBEUtil {


    private static volatile EncryptionStandardPBEUtil instance;
    private static String algorithm;
    private static String password;

    private EncryptionStandardPBEUtil(){

    }

    public static EncryptionStandardPBEUtil getInstance(String algorithm, String password){
        if (instance == null) {
            synchronized (EncryptionStandardPBEUtil.class) {
                if (instance == null) {
                    setAlgorithm(algorithm);
                    setPassword(password);
                    instance = new EncryptionStandardPBEUtil();
                }
            }
        }
        return instance;
    }

    public static EncryptionStandardPBEUtil getInstance(){
        if (instance == null) {
            synchronized (EncryptionStandardPBEUtil.class) {
                if (instance == null) {
                    instance = new EncryptionStandardPBEUtil();
                }
            }
        }
        return instance;
    }

    public static EncryptionStandardPBEUtil getInstance(String algorithm){
        if (instance == null) {
            synchronized (EncryptionStandardPBEUtil.class) {
                if (instance == null) {
                    setAlgorithm(algorithm);
                    instance = new EncryptionStandardPBEUtil();
                }
            }
        }
        return instance;
    }

    private static void setAlgorithm(String algorithm) {
        EncryptionStandardPBEUtil.algorithm = algorithm;
    }

    private static void setPassword(String password) {
        EncryptionStandardPBEUtil.password = password;
    }

    private static String getAlgorithm() {
        return algorithm;
    }

    private static String getPassword() {
        return password;
    }

    public String encryptText(String text) {
        return encryptText(text,getPassword());
    }

    public String decryptText(String text) {
        return decryptText(text,getPassword());
    }

    public String encryptText(String text, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(getAlgorithm());
        encryptor.setIvGenerator(new RandomIvGenerator());
        encryptor.setPassword(password);
        return encryptor.encrypt(text);
    }

    public String decryptText(String text, String password) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm(getAlgorithm());
        encryptor.setIvGenerator(new RandomIvGenerator());
        encryptor.setPassword(password);
        return encryptor.decrypt(text);
    }

}
