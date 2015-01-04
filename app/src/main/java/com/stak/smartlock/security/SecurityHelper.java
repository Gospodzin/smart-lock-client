package com.stak.smartlock.security;

import com.stak.smartlock.SmartLockApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

import static com.stak.smartlock.security.Constants.TRUST_STORE_FILE;
import static com.stak.smartlock.security.Constants.TRUST_STORE_PASSWORD;

/**
 * Created by gospo on 28.12.14.
 */
public class SecurityHelper {

    public void createTrustStore(Certificate cert){
        String filesDir = SmartLockApp.getContext().getFilesDir().getPath();
        try{
            FileOutputStream fos = new FileOutputStream(filesDir + "/" + TRUST_STORE_FILE);
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            trustStore.setCertificateEntry("SmartLock", cert);
            trustStore.store(fos, TRUST_STORE_PASSWORD.toCharArray());
            fos.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTrustStoreFileExistent() {
        String filesDir = SmartLockApp.getContext().getFilesDir().getPath();
        File file = new File(filesDir + "/" + TRUST_STORE_FILE);
        return file.exists();
    }
}
