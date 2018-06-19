package com.farmease.app.network;

import android.content.Context;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.utility.AppToast;


/**
 * Created by Himanshu on 19/6/18.
 */
public class RetrofitErrorHandler {

    Context context;

    public RetrofitErrorHandler(Context context) {
        this.context = context;
    }


    public void responseOnError(int statusCode) {
        switch (statusCode) {
            case 400:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.an_error_occured),
                        Toast.LENGTH_SHORT);
                break;
            case 401:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.unauthorized_user),
                        Toast.LENGTH_SHORT);
                break;
            case 403:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.forbidden),
                        Toast.LENGTH_LONG);
                break;
            case 404:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.server_unable_to_process),
                        Toast.LENGTH_SHORT);
                break;
            case 408:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.toastResponseTimeout),
                        Toast.LENGTH_SHORT);
                break;
            case 500:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.internal_server_error),
                        Toast.LENGTH_SHORT);
                break;
            case 600:
                AppToast.showToast(context,
                        context.getResources().getString(R.string.toastHostError),
                        Toast.LENGTH_SHORT);
                break;
            case 601:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.toastConnectionFailed),
                        Toast.LENGTH_SHORT);
                break;
            case 503:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.server_unable_to_process),
                        Toast.LENGTH_SHORT);
                break;
            default:
                AppToast.showToast(
                        context,
                        context.getResources().getString(
                                R.string.toastServerFailedRespond),
                        Toast.LENGTH_SHORT);
                break;
        }
    }

}