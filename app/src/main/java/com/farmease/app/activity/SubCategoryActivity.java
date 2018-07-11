package com.farmease.app.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.farmease.app.R;
import com.farmease.app.adapter.CategoryAdapter;
import com.farmease.app.bean.BeanCategory;
import com.farmease.app.bean.BeanSubCategory;
import com.farmease.app.fragment.FragmentSubCategory;
import com.farmease.app.model.Category;
import com.farmease.app.model.SubCategory;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.CustomProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SubCategoryActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.backimg)
    ImageView img_back;
    @BindView(R.id.txt_categoryname)
    TextView txtCatName;
    @BindView(R.id.collapse_toolbar)
    CollapsingToolbarLayout layout;
    @BindView(R.id.htab_toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_category)
    ImageView imgCategory;
    private ArrayList<SubCategory> mDataList;
    private Unbinder unbinder;
    private CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        unbinder = ButterKnife.bind(this);
        progressBar = CustomProgressBar.getInstance();
        setSupportActionBar(toolbar);
        mDataList = new ArrayList<>();


        getSubCategory(getIntent().getExtras().getInt("cat_id"));
        tabLayout.setupWithViewPager(viewPager);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txtCatName.setText(getIntent().getExtras().getString("cat_name"));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.back);

        Glide.with(SubCategoryActivity.this).load(getIntent().getExtras().getString("cat_img")).into(imgCategory);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant =
                        palette.getVibrantSwatch();
                if (vibrant != null) {
                    // If we have a vibrant color
                    // update the title TextView

                    int mutedColor = palette.getMutedColor(R.color.colorPrimary);
                    layout.setStatusBarScrimColor(palette.getDarkMutedColor(mutedColor));
                    layout.setContentScrimColor(palette.getMutedColor(mutedColor));
                    layout.setBackgroundColor(mutedColor);
                }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager, ArrayList<SubCategory> mDataList) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < mDataList.size(); i++) {
            adapter.addFragment(new FragmentSubCategory(), mDataList.get(i).getsubcategory_name(),
                    String.valueOf(getIntent().getExtras().getInt("cat_id"))+"/"+String.valueOf(mDataList.get(i).getId()),
                    getIntent().getExtras().getString("cat_name"));
        }
        viewPager.setAdapter(adapter);
    }

    private void setupEmptyViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentSubCategory(), "Category", String.valueOf(getIntent().getExtras().getInt("cat_id")),
                getIntent().getExtras().getString("cat_name"));
        viewPager.setAdapter(adapter);
    }

    private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final List<String> mFragmentId = new ArrayList<>();
        private String name;

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("id", mFragmentId.get(position).toString());
            bundle.putString("name",name);
            mFragmentList.get(position).setArguments(bundle);
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title,String id,String name) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
           mFragmentId.add(id);
           this.name=name;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void getSubCategory(final int id) {

        progressBar.showProgress(SubCategoryActivity.this);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanSubCategory> call = service.subcategory("category/" + id);

        call.enqueue(new Callback<BeanSubCategory>() {
            @Override
            public void onResponse(Call<BeanSubCategory> call, Response<BeanSubCategory> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    mDataList = new ArrayList<>(Arrays.asList(response.body().getResult()));
                    if (mDataList != null) {
                        setupViewPager(viewPager, mDataList);
                    }
                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {

                        tabLayout.setVisibility(View.GONE);
                        setupEmptyViewPager(viewPager);
                        setMargins(toolbar,0,0,0,0);
                    }
                   /* RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(SubCategoryActivity.this);
                    errorHandler.responseOnError(statusCode);*/
                }
            }

            @Override
            public void onFailure(Call<BeanSubCategory> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(SubCategoryActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
