package com.farmease.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class TaskActivity extends AppCompatActivity {

    private ImageView imgTop,imgBottom,imgLeft,imgRight,imgMiddle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        imgTop=findViewById(R.id.top);
        imgBottom=findViewById(R.id.bottom);
        imgLeft=findViewById(R.id.left);
        imgRight=findViewById(R.id.right);
        imgMiddle=findViewById(R.id.middle);

        imgLeft.startAnimation(inFromLeftAnimation());
        imgRight.startAnimation(inFromRightAnimation());
        imgTop.startAnimation(inFromTopAnimation());
        imgBottom.startAnimation(inFromBottomAnimation());
    }

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(2000);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }
    private Animation inFromLeftAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(2000);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }
    private Animation inFromTopAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(2000);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }
    private Animation inFromBottomAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromRight.setDuration(2000);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }
}
