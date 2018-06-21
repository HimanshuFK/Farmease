package com.farmease.app.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.farmease.app.R;

public class CustomProgressBar {
    public static CustomProgressBar mCShowProgress;
    public Dialog mDialog;

    public CustomProgressBar() {
    }

    public static CustomProgressBar getInstance() {
        if (mCShowProgress== null) {
            mCShowProgress= new CustomProgressBar();
        }
        return mCShowProgress;
    }

    public void showProgress(Context mContext) {
        mDialog= new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_progressbar);
        mDialog.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    public void hideProgress() {
        if (mDialog!= null) {
            mDialog.dismiss();
            mDialog= null;
        }
    }
}
