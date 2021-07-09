package com.dt.autumn.utils.encryptionUtils;

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
