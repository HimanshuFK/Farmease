package com.farmease.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.bean.BeanCommon;
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

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.btn_proceed)
    Button btnProceed;
    @BindView(R.id.edtxt_email)
    EditText edtEmail;
    private CustomProgressBar progressBar;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        unbinder= ButterKnife.bind(this);
        progressBar=CustomProgressBar.getInstance();
        btnProceed.setOnClickListener(this);
    }
    private void forgetPassword() {

        progressBar.showProgress(ForgetPasswordActivity.this);

        final String email = edtEmail.getText().toString().trim();


        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.forgetPassword(email);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    Intent intent=new Intent(ForgetPasswordActivity.this,OtpActivity.class);
                    intent.putExtra("email",email);
                    startActivity(intent);

                    Toast.makeText(ForgetPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    int statusCode = response.code();
                    if (statusCode==403){
                        startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
                    }
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ForgetPasswordActivity.this);
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
            case R.id.btn_proceed:
                forgetPassword();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
