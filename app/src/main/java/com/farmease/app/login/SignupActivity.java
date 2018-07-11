package com.farmease.app.login;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.HomeActivity;
import com.farmease.app.R;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.bean.BeanLogin;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{
    private Unbinder unbinder;
    private CustomProgressBar progressBar;
    @BindView(R.id.edtxt_email)
    EditText edtEmail;
    @BindView(R.id.edtxt_name)
    EditText edtName;
    @BindView(R.id.edtxt_lname)
    EditText edtLname;
    @BindView(R.id.edtxt_mobile)
    EditText edtMobile;
    @BindView(R.id.edtxt_password)
    EditText edtPassword;
    @BindView(R.id.btn_signup)
    Button btnsignup;
    @BindView(R.id.txt_alreadymember)
    TextView txtSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        unbinder= ButterKnife.bind(SignupActivity.this);
        progressBar = CustomProgressBar.getInstance();
        btnsignup.setOnClickListener(this);
        txtSignin.setOnClickListener(this);

    }

    private void userSignUp() {

        progressBar.showProgress(SignupActivity.this);

        final String name = edtName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();
        final String mobile = edtMobile.getText().toString().trim();
        final String lname = edtLname.getText().toString().trim();
        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanCommon> call = service.userSignup(email, password,name,lname,mobile,"Android",android_id);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {
                progressBar.hideProgress();

                if (response.isSuccessful()) {
                    finish();
                    Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(SignupActivity.this);
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
            case R.id.btn_signup:
                userSignUp();
                break;
            case R.id.txt_alreadymember:
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


}
