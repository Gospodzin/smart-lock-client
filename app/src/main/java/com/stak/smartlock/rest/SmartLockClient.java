package com.stak.smartlock.rest;

import com.stak.smartlock.SmartLockApp;
import com.stak.smartlock.rest.dto.CommandDTO;
import com.stak.smartlock.rest.dto.ConfirmDTO;
import com.stak.smartlock.security.SecurityHelper;
import com.stak.smartlock.security.X509TrustManagerImpl;

import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.Parameter;
import org.restlet.engine.ssl.SslContextFactory;
import org.restlet.resource.ClientResource;
import org.restlet.util.Series;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import static com.stak.smartlock.security.Constants.TRUST_STORE_FILE;
import static com.stak.smartlock.security.Constants.TRUST_STORE_PASSWORD;
import static org.restlet.data.Protocol.HTTPS;

/**
 * Created by gospo on 03.01.15.
 */
public class SmartLockClient {

    private SecurityHelper securityHelper = new SecurityHelper();

    private ClientResource clRegisterResource;
    private RegisterResource registerResource;

    private ClientResource clSmartLockResource;
    private SmartLockResource smartLockResource;

    public SmartLockClient(String host, int port) {
        try {
            String addr = host + ":" + port;

            Client client = createClient();

            clRegisterResource = new ClientResource("https://" + addr + "/confirm");
            clRegisterResource.setNext(client);
            registerResource = clRegisterResource.wrap(RegisterResource.class);

            clSmartLockResource = new ClientResource("https://"+ addr + "/lock");
            clSmartLockResource.setNext(createClient());
            smartLockResource = clSmartLockResource.wrap(SmartLockResource.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Client createClient() {
        Client client = new Client(new Context(), HTTPS);
        Context context = client.getContext();
        if(!securityHelper.isTrustStoreFileExistent())
            context.getAttributes().put("sslContextFactory", new SslContextFactoryImpl());
        else {
            context.getParameters().add("truststorePath", SmartLockApp.getContext().getFilesDir() + "/" + TRUST_STORE_FILE);
            context.getParameters().add("truststorePassword", TRUST_STORE_PASSWORD);
            context.getParameters().add("trustManagerAlgorithm", TrustManagerFactory.getDefaultAlgorithm());
        }

        return client;
    }

    public String confirm(String username, String pin) {
        return registerResource.confirm(new ConfirmDTO(username, pin));
    }

    public boolean open(String token) {
        return smartLockResource.command(new CommandDTO(token, "open"));
    }

    public boolean close(String token) {
        return smartLockResource.command(new CommandDTO(token, "close"));
    }

    private static class SslContextFactoryImpl extends SslContextFactory {

        @Override
        public SSLContext createSslContext() throws Exception {
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new X509TrustManagerImpl()}, null);
            return sslContext;
        }

        @Override
        public void init(Series<Parameter> parameters) {

        }
    }

    public void release() {
        clSmartLockResource.release();
        clRegisterResource.release();
    }
}
