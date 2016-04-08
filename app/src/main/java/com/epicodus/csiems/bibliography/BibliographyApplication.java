package com.epicodus.csiems.bibliography;


import android.app.Application;

import com.firebase.client.Firebase;

public class BibliographyApplication extends Application {
    private static BibliographyApplication app;
    private Firebase mFirebaseRef;

    public static BibliographyApplication getAppInstance() {
        return app;
    }

    public Firebase getFirebaseRef() {
        return mFirebaseRef;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(this.getString(R.string.firebase_url));
    }
}

