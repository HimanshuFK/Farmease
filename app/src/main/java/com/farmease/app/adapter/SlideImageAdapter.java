package com.farmease.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.farmease.app.R;
import com.farmease.app.activity.VideoPlayerActivity;
import com.farmease.app.model.Banner;
import com.farmease.app.utility.Constants;

import java.util.ArrayList;


public class SlideImageAdapter extends PagerAdapter {


    private ArrayList<Banner> IMAGES;
    private LayoutInflater inflater;
    private Context context;


    public SlideImageAdapter(Context context,ArrayList<Banner> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        if (position==1){
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, VideoPlayerActivity.class));
                }
            });
        }

        Glide.with(context).load(IMAGES.get(position).getBanner_image()).into(imageView);



        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
