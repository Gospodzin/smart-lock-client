package com.stak.smartlock;

import android.app.Application;
import android.content.Context;

import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;
import org.restlet.ext.nio.HttpsClientHelper;

/**
 * Created by gospo on 30.12.14.
 */
public class SmartLockApp extends Application {
    static Context app;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    public void init() {
        this.app = this;

        Engine.getInstance().getRegisteredClients().clear();
        Engine.getInstance().getRegisteredClients().add(new HttpsClientHelper(null));
        Engine.getInstance().getRegisteredConverters().clear();
        Engine.getInstance().getRegisteredConverters().add(new GsonConverter());
    }

    public static Context getContext() {
        return app;
    }
}
