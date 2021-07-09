package com.dt.autumn.utils.encryptionUtils;

import com.dt.autumn.utils.exceptions.ErrorDecrypting;
import com.dt.autumn.utils.exceptions.ErrorEncrypting;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class EncryptionMessageDigestUtil {

    private static volatile EncryptionMessageDigestUtil instance;
    private static String algorithm;
    private static String password;

    private EncryptionMessageDigestUtil() {

    }

    public static EncryptionMessageDigestUtil getInstance(String algorithm, String password) {
        if (instance == null) {
            synchronized (EncryptionMessageDigestUtil.class) {
                if (instance == null) {
                    setAlgorithm(algorithm);
                    setPassword(password);
                    instance = new EncryptionMessageDigestUtil();
                }
            }
        }
        return instance;
    }

    public static EncryptionMessageDigestUtil getInstance() {
        if (instance == null) {
            synchronized (EncryptionMessageDigestUtil.class) {
                if (instance == null) {
                    instance = new EncryptionMessageDigestUtil();
                }
            }
        }
        return instance;
    }

    public static EncryptionMessageDigestUtil getInstance(String algorithm) {
        if (instance == null) {
            synchronized (EncryptionMessageDigestUtil.class) {
                if (instance == null) {
                    setAlgorithm(algorithm);
                    instance = new EncryptionMessageDigestUtil();
                }
            }
        }
        return instance;
    }

    private static void setAlgorithm(String algorithm) {
        EncryptionMessageDigestUtil.algorithm = algorithm;
    }

    private static void setPassword(String password) {
        EncryptionMessageDigestUtil.password = password;
    }

    private static String getAlgorithm() {
        return algorithm;
    }

    private static String getPassword() {
        return password;
    }

    public String encryptTextSH256(String text) {
        return encryptTextSH256(text, getPassword());
    }

    public String decryptTextSH256(String text) {
        return decryptTextSH256(text, getPassword());
    }

    public String encryptTextSH256(String text, String password) {
        try {
            byte[] keyBytes = new byte[0];
            keyBytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(getAlgorithm(), new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptionOutput = cipher.doFinal(text.getBytes());
            return Base64.getEncoder().encodeToString(encryptionOutput);
        } catch (NoSuchAlgorithmException e) {
           throw new ErrorEncrypting(e);
        } catch (NoSuchPaddingException e) {
            throw new ErrorEncrypting(e);
        } catch (InvalidKeyException e) {
            throw new ErrorEncrypting(e);
        } catch (IllegalBlockSizeException e) {
            throw new ErrorEncrypting(e);
        } catch (BadPaddingException e) {
            throw new ErrorEncrypting(e);
        }
    }

    public String decryptTextSH256(String text, String password) {
        try {
            byte[] keyBytes = MessageDigest.getInstance("SHA-256").digest(password.getBytes());
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance(getAlgorithm(), new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptionInput = Base64.getDecoder().decode(text.getBytes());
            byte[] decryptionOutput = cipher.doFinal(decryptionInput);
            return new String(decryptionOutput);
        } catch (NoSuchAlgorithmException e) {
            throw new ErrorDecrypting(e);
        } catch (InvalidKeyException e) {
            throw new ErrorDecrypting(e);
        } catch (NoSuchPaddingException e) {
            throw new ErrorDecrypting(e);
        } catch (BadPaddingException e) {
            throw new ErrorDecrypting(e);
        } catch (IllegalBlockSizeException e) {
            throw new ErrorDecrypting(e);
        }
    }


}
