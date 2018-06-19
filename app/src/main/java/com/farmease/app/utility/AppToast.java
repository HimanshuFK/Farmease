package com.farmease.app.utility;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Himanshu on 25/1/17.
 */

public class AppToast {


    private static Toast toast;

    public static void showToast(Context mContext, String st, int length){ //"Toast toast" is declared in the class
        try{ toast.getView().isShown();     // true if visible
            toast.setText(st);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(mContext, st,length);
        }
        toast.show();  //finally display it
    }



}
