package com.stak.smartlock.security;

import android.util.Log;

import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by gospo on 30.12.14.
 */
public class X509TrustManagerImpl implements X509TrustManager {

    SecurityHelper securityHelper = new SecurityHelper();

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        Certificate cert = chain[0];
        securityHelper.createTrustStore(cert);
        Log.i("", "");
    }
}
