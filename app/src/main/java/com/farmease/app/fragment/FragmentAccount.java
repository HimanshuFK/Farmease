package com.farmease.app.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.farmease.app.R;
import com.farmease.app.activity.MyAccountActivity;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.bean.BeanLogin;
import com.farmease.app.login.LoginActivity;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.Constants;
import com.farmease.app.utility.CustomProgressBar;
import com.farmease.app.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class FragmentAccount extends Fragment implements View.OnClickListener {
    private Unbinder unbinder;
    @BindView(R.id.txt_account)
    TextView txtAccount;
    @BindView(R.id.txt_signout)
    TextView txtSignout;
    private CustomProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myaccount, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressBar=CustomProgressBar.getInstance();
        txtAccount.setOnClickListener(this);
        txtSignout.setOnClickListener(this);
        if (!Utility.getBooleanDataFromsharedPrefences(getActivity(),Constants.LOGIN)){
            txtSignout.setText("LOGIN");
            txtAccount.setClickable(false);
        }
        return view;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_account:
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.txt_signout:
                if (Utility.getBooleanDataFromsharedPrefences(getActivity(),Constants.LOGIN)){
                    String token=Utility.getPref(getActivity(),Constants.token,null);

                    if (token!=null){
                        if (Utility.isInternetConnected(getActivity())){
                            logout(token);
                        }else {
                            AppToast.showToast(getActivity(),"No Internet Found",Toast.LENGTH_SHORT);
                        }

                    }else {
                        Utility.saveBooleanDataTosharedPrefences(getActivity(), Constants.LOGIN,false);
                        Toast.makeText(getActivity(), "Session Expired Login again", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finishAffinity();
                    }
                    Utility.clearAll(getActivity());
                }else {
                    startActivity(new Intent(getActivity(),LoginActivity.class));
                }


                break;
        }
    }

    private void logout(final String token) {

        progressBar.showProgress(getActivity());
        Retrofit retrofit = RetrofitFactory.getRetrofitInstance(token);
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.logout("logout");

        call.enqueue(new Callback<BeanCommon>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    Utility.saveBooleanDataTosharedPrefences(getActivity(), Constants.LOGIN,false);
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finishAffinity();
                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(getActivity());
                    errorHandler.responseOnError(statusCode);
                    // Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BeanCommon> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
