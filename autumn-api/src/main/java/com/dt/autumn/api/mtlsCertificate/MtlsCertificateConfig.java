package com.dt.autumn.api.mtlsCertificate;


import io.restassured.config.SSLConfig;
import org.apache.http.conn.ssl.SSLSocketFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class MtlsCertificateConfig {

    private static volatile MtlsCertificateConfig _instance;
    TrustManagerFactory trustManagerFactory;
    KeyManagerFactory keyManagerFactory;


    public static MtlsCertificateConfig getInstance() {
        if (_instance == null) {
            synchronized (MtlsCertificateConfig.class) {
                if (_instance == null) {
                    _instance = new MtlsCertificateConfig();
                }
            }
        }
        return _instance;
    }


    public void setKeyStore(String certificate, String certificatePassword) {
        try {

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            keyStore.load(new FileInputStream(this.getClass().getClassLoader().getResource(certificate).getFile()), certificatePassword.toCharArray());

            keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, certificatePassword.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTrustStore(String certificate, String certificateAlias) {
        try {

            File crtFile = new File(this.getClass().getClassLoader().getResource(certificate).getFile());
            Certificate certificateCert = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(crtFile));


            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            trustStore.setCertificateEntry(certificateAlias, certificateCert);

            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public SSLConfig getSSLconfig() {
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
        SSLSocketFactory clientAuthFactory = null;
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        clientAuthFactory = new SSLSocketFactory(sslContext);

        return new SSLConfig().with().sslSocketFactory(clientAuthFactory).and().allowAllHostnames();

    }

}
