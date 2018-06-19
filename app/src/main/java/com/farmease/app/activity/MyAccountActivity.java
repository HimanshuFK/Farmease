package com.farmease.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farmease.app.R;
import com.farmease.app.utility.Constants;
import com.farmease.app.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccountActivity extends AppCompatActivity {

    @BindView(R.id.user_image)
    CircleImageView img_profile;
    @BindView(R.id.user_name)
    TextView txt_name;
    @BindView(R.id.user_email)
    TextView txt_email;
    @BindView(R.id.imgback)
    ImageView img_back;
    private Unbinder unbinder;
    private String name,email,image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        unbinder= ButterKnife.bind(this);
        name= Utility.getPref(MyAccountActivity.this, Constants.username,null);
        email= Utility.getPref(MyAccountActivity.this, Constants.useremail,null);
        image=Utility.getPref(MyAccountActivity.this, Constants.userimg,null);
        txt_name.setText(name);
        txt_email.setText(email);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
