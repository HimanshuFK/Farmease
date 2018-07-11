package com.farmease.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.farmease.app.HomeActivity;
import com.farmease.app.R;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.bean.BeanLogin;
import com.farmease.app.login.LoginActivity;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
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

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.edtxt_currentpassword)
    EditText edtxtCurrentPassword;
    @BindView(R.id.edtxt_newpassword)
    EditText edtxtNewPassword;
    @BindView(R.id.btn_save)
    Button btnsave;
    private Unbinder unbinder;
    private CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        unbinder= ButterKnife.bind(this);
        progressBar=CustomProgressBar.getInstance();
        btnsave.setOnClickListener(this);
        if (getIntent().getExtras().getString("screen").equalsIgnoreCase("forget")){
            edtxtCurrentPassword.setHint("New Password");
            edtxtNewPassword.setHint("Confirm Password");
        }
    }

    private void changePassword() {

        progressBar.showProgress(ChangePasswordActivity.this);

        final String oldpassword = edtxtCurrentPassword.getText().toString().trim();
        final String newPassword = edtxtNewPassword.getText().toString().trim();

        String token=Utility.getPref(ChangePasswordActivity.this,Constants.token,null);
        String email=Utility.getPref(ChangePasswordActivity.this,Constants.useremail,null);

        Retrofit retrofit = RetrofitFactory.getRetrofitInstance(token);
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.changePassword(email,oldpassword, newPassword);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ChangePasswordActivity.this);
                    errorHandler.responseOnError(statusCode);
                    // Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BeanCommon> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void forgetchangePassword(final String email,final String otp) {

        progressBar.showProgress(ChangePasswordActivity.this);

        final String newPassword = edtxtNewPassword.getText().toString().trim();


        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.updateForgetPassword(email, newPassword,otp);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    Toast.makeText(ChangePasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ChangePasswordActivity.this);
                    errorHandler.responseOnError(statusCode);
                    // Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BeanCommon> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
                if (getIntent().getExtras().getString("screen").equalsIgnoreCase("forget")){
                   forgetchangePassword(getIntent().getExtras().getString("email"),getIntent().getExtras().getString("otp"));
                }else {
                    changePassword();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
