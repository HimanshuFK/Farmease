package com.farmease.app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.CustomProgressBar;
import com.farmease.app.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.btn_proceed)
    Button btnProceed;
    @BindView(R.id.edt1)
    EditText edt1;
    @BindView(R.id.edt2)
    EditText edt2;
    @BindView(R.id.edt3)
    EditText edt3;
    @BindView(R.id.edt4)
    EditText edt4;
    @BindView(R.id.edt5)
    EditText edt5;
    @BindView(R.id.edt6)
    EditText edt6;
    private Unbinder unbinder;
    private CustomProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        unbinder= ButterKnife.bind(this);
        progressBar=CustomProgressBar.getInstance();
        btnProceed.setOnClickListener(this);
        init();

    }

    public void init(){
        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt1.getText().toString().length() == 0) {

                } else {
                    edt2.requestFocus();
                }
            }
        });

        edt2.addTextChangedListener(new OtpActivity.CustomTextWatcher(edt2, edt3, edt1));
        edt3.addTextChangedListener(new OtpActivity.CustomTextWatcher(edt3, edt4, edt2));
        edt4.addTextChangedListener(new OtpActivity.CustomTextWatcher(edt4, edt5, edt3));
        edt5.addTextChangedListener(new OtpActivity.CustomTextWatcher(edt5, edt6, edt4));

        edt6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (edt6.getText().toString().length() == 0) {
                    edt5.requestFocus();
                }
            }
        });

    }

    private void forgetPassword() {

        progressBar.showProgress(OtpActivity.this);

        final String otp = edt1.getText().toString() + edt2.getText().toString() + edt3.getText().toString() + edt4.getText().toString()
                +edt5.getText().toString()+edt6.getText().toString();

        final String email=getIntent().getExtras().getString("email");
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.verifyOtp("verifyOTP/"+email+"/"+otp);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {
                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    Intent intent=new Intent(OtpActivity.this,ChangePasswordActivity.class);
                    intent.putExtra("screen","forget");
                    intent.putExtra("email",email);
                    intent.putExtra("otp",otp);
                    startActivity(intent);
                    Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    int statusCode = response.code();

                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(OtpActivity.this);
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

    public static class CustomTextWatcher implements TextWatcher {
        private EditText mEditText1, mEditText2, mEditText3;

        public CustomTextWatcher(EditText e, EditText e2, EditText e3) {
            mEditText1 = e;
            mEditText2 = e2;
            mEditText3 = e3;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {

            if (mEditText1.getText().toString().length() == 0) {
                mEditText3.requestFocus();
            } else {
                mEditText2.requestFocus();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_proceed:
                if (Utility.isInternetConnected(OtpActivity.this)){
                    forgetPassword();
                }else {
                    AppToast.showToast(OtpActivity.this,"No Internet Found",Toast.LENGTH_SHORT);
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
