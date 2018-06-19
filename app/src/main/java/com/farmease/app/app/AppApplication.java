package com.farmease.app.app;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;

import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class AppApplication extends Application {

    private static AppApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
       // FacebookSdk.sdkInitialize(getApplicationContext());
        mInstance = this;
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.farmease.app",  // replace with your unique package name
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized AppApplication getInstance() {
        return mInstance;
    }

    public void setFontFamilyGib(TextView txt) {
        if (txt != null) {
            Typeface face = Typeface.createFromAsset(getAssets(),
                    "fonts/gibsonlight.ttf");
            txt.setTypeface(face);
        }
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

    }
}
