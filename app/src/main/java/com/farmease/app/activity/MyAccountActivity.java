package com.farmease.app.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.farmease.app.R;
import com.farmease.app.services.APIService;
import com.farmease.app.bean.BeanLogin;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.Constants;
import com.farmease.app.utility.CustomProgressBar;
import com.farmease.app.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.user_image)
    CircleImageView img_profile;
    @BindView(R.id.user_name)
    TextView txt_name;
    @BindView(R.id.user_email)
    TextView txt_email;
    @BindView(R.id.imgback)
    ImageView img_back;
    @BindView(R.id.txt_changepassword)
    TextView txtChangePassword;
    private Unbinder unbinder;
    private String name,email,image;
    private CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        progressBar=CustomProgressBar.getInstance();
        unbinder= ButterKnife.bind(this);
        name= Utility.getPref(MyAccountActivity.this, Constants.username,null);
        email= Utility.getPref(MyAccountActivity.this, Constants.useremail,null);
        image=Utility.getPref(MyAccountActivity.this, Constants.userimg,null);
        txt_name.setText(name);
        txt_email.setText(email);
        txtChangePassword.setOnClickListener(this);
        //dummy();
        if (image!=null){
            Glide.with(this).load(image).into(img_profile);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void dummy() {

        progressBar.showProgress(MyAccountActivity.this);
        String token=Utility.getPref(MyAccountActivity.this,Constants.token,null);
        Retrofit retrofit = RetrofitFactory.getRetrofitInstance(token);
        APIService service = retrofit.create(APIService.class);

        Call<BeanLogin> call = service.dummy("report/users/today");

        call.enqueue(new Callback<BeanLogin>() {
            @Override
            public void onResponse(Call<BeanLogin> call, Response<BeanLogin> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    finish();
                    AppToast.showToast(MyAccountActivity.this,"Success",Toast.LENGTH_SHORT);
                    //SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(MyAccountActivity.this);
                    errorHandler.responseOnError(statusCode);
                    // Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BeanLogin> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_changepassword:
                Intent intent=new Intent(MyAccountActivity.this,ChangePasswordActivity.class);
                intent.putExtra("screen","change");
                startActivity(intent);
                break;
        }
    }
}
