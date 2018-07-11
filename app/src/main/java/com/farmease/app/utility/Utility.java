package com.farmease.app.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Himanshu on 2/11/2016.
 */
public class Utility {
    public static Context context;

    public static String INTERNET_ERROR = "Check internet connection";

    synchronized public static void savePref(Context context, String field, String value) {
        SharedPreferences sp = context.getSharedPreferences("farmease", context.MODE_PRIVATE);
        sp.edit().putString(field, value).commit();
    }

    synchronized public static String getPref(Context context, String field, String def) {
        SharedPreferences sp = context.getSharedPreferences("farmease", context.MODE_PRIVATE);
        return sp.getString(field, def);
    }

    public static boolean isShowingKeyBoard(Context context) {
        return ((InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE)).isActive();
    }

    public static void hideKeyboard(View view, Context context) {
        if (isShowingKeyBoard(context)) {
            InputMethodManager in = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    public static void showKeyBoard(View view, Context context) {
        if (isShowingKeyBoard(context)) {

            InputMethodManager in = (InputMethodManager) context
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            in.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static boolean isInternetConnected(Context mContext) {

        ConnectivityManager cm = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;

    }

    public static String getDeviceId(Context context) {
        @SuppressLint("MissingPermission") String deviceId = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

		/*
         * String deviceId=Secure.getString(mContext.getContentResolver(),
		 * Secure.ANDROID_ID);
		 */
        return deviceId;
    }

    /**
     * function definition for ic_check is google play is installed or not
     */
    private static final String GooglePlayStorePackageNameOld = "com.google.market";
    private static final String GooglePlayStorePackageNameNew = "com.android.vending";
    static boolean googlePlayStoreInstalled = false;

    public static boolean isGooglePlayInstalled(Context ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> packages = packageManager
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(GooglePlayStorePackageNameOld)
                    || packageInfo.packageName
                    .equals(GooglePlayStorePackageNameNew)) {
                googlePlayStoreInstalled = true;
                break;
            }
        }
        return googlePlayStoreInstalled;
    }

    public static void isGmailInstalled(String packageName, Activity activity) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            List<ResolveInfo> resolveInfoList = activity.getPackageManager()
                    .queryIntentActivities(intent, 0);

            for (ResolveInfo info : resolveInfoList) {
                if (info.activityInfo.packageName.equalsIgnoreCase(packageName)) {
                    launchApp(info.activityInfo.packageName,
                            info.activityInfo.name, activity);
                    return;
                } else {
                    /*
                     * Toast toast = Toast.makeText(activity,
					 * "Can't find Gmail", Toast.LENGTH_LONG); toast.show();
					 */
                }
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(activity, "Can't find Gmail",
                    Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private static void launchApp(String packageName, String name,
                                  Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName(packageName, name));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void shareListUrl(Context ctx) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, "ShareList");
        share.putExtra(
                Intent.EXTRA_TEXT,
                "Hey, I buy groceries online from Bucketkart.com Lowest prices and very good service. Just try once! Download it from AppStore - \n "
                        + "https://play.google.com/store/apps/details?id=com.cognizant.eventum&hl=en");

        ctx.startActivity(Intent.createChooser(share, "Bucketkart Application"));
    }

    public static boolean appInstalledOrNot(Context ctx, String uri) {
        PackageManager pm = ctx.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static String getCurrentDate() {
        // String dateStr = "04/05/2010";
        StringBuilder dateStr = null;
        try {
            final Calendar c = Calendar.getInstance();
            int yy = c.get(Calendar.YEAR);
            int mm = c.get(Calendar.MONTH);
            int dd = c.get(Calendar.DAY_OF_MONTH);

            dateStr = new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(yy).append("").append("-").append(mm + 1)
                    .append("-").append(dd);

        } catch (Exception ex) {
            ex.getMessage();
        }
        return dateStr.toString();
    }

    public static void saveintegerDataTosharedPrefences(Context mContext,
                                                        String key, int data) {
        final String PREFS_NAME = "ItemCountDetails";
        final SharedPreferences SpyAppData = mContext.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = SpyAppData.edit();
        editor.putInt(key, data);
        editor.commit();
    }

    public static int getIntergerDataFromsharedPrefences(Context mContext,
                                                         String key) {
        final String PREFS_NAME = "ItemCountDetails";
        final SharedPreferences ToolsAppData = mContext.getSharedPreferences(
                PREFS_NAME, 0);
        final int preData = ToolsAppData.getInt(key, 0);
        return preData;

    }

    public static void saveBooleanDataTosharedPrefences(Context mContext,
                                                        String key, boolean data) {
        final String PREFS_NAME = "IsFirstRun";
        final SharedPreferences SpyAppData = mContext.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = SpyAppData.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public static boolean getBooleanDataFromsharedPrefences(Context mContext, String key) {
        final String PREFS_NAME = "IsFirstRun";
        final SharedPreferences ToolsAppData = mContext.getSharedPreferences(
                PREFS_NAME, 0);
        final boolean preData = ToolsAppData.getBoolean(key, false);
        return preData;

    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    public static boolean isValidEmail(CharSequence target) {

        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidEmailorNumber(CharSequence target) {
        if(!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()){
            return true;
        }else {

            return true;
        }

    }
}
