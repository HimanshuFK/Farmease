package com.farmease.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.activity.ProductActivity;
import com.farmease.app.activity.ProductListActivity;
import com.farmease.app.adapter.HomeAdapter;
import com.farmease.app.adapter.SlideImageAdapter;
import com.farmease.app.bean.BeanCategory;
import com.farmease.app.bean.BeanHome;
import com.farmease.app.model.Banner;
import com.farmease.app.model.Home;
import com.farmease.app.model.Product;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.ClickListener;
import com.farmease.app.utility.Constants;
import com.farmease.app.utility.CustomProgressBar;
import com.farmease.app.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentHome extends Fragment implements ClickListener,View.OnClickListener {

    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.recyclerview_recentadd)
    RecyclerView rcviewRecentadd;
    @BindView(R.id.recyclerview_TopRating)
    RecyclerView rcviewTopRated;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.txt_seeall)
    TextView txtSeeAll1;
    @BindView(R.id.txt_seealltop)
    TextView txtSeeAll2;
    @BindView(R.id.layout_main)
    LinearLayout layoutMain;
    private HomeAdapter mAdapter, mAdapter1;
    private Home mDataList;
    private ArrayList<Banner> bannerList;
    private ArrayList<Product> trendProduct;
    private ArrayList<Product> recentProduct;
    private Unbinder unbinder;
    private int currentPage = 0;
    private int NUM_PAGES = 0;
    private Timer swipeTimer;
    private boolean hasstarted = false;
    private CustomProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        bannerList = new ArrayList<>();
        trendProduct = new ArrayList<>();
        recentProduct = new ArrayList<>();
        progressBar = CustomProgressBar.getInstance();
        rcviewRecentadd.setHasFixedSize(true);
        rcviewTopRated.setHasFixedSize(true);
        txtSeeAll1.setOnClickListener(this);
        txtSeeAll2.setOnClickListener(this);
        rcviewRecentadd.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcviewTopRated.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        if (Utility.isInternetConnected(getActivity())){
            getHome();
        }else {
            AppToast.showToast(getActivity(),"No Internet Found",Toast.LENGTH_SHORT);
        }


        return view;
    }

    private void init() {
        //static banners
       /* for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);*/

        viewPager.setAdapter(new SlideImageAdapter(getActivity(), bannerList));
        indicator.setViewPager(viewPager);

        NUM_PAGES = bannerList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {

            public void run() {

                if (currentPage <= NUM_PAGES) {
                    viewPager.setCurrentItem(currentPage, true);
                    currentPage += 1;

                } else {
                    currentPage = 0;
                    viewPager.setCurrentItem(currentPage, true);
                }

            }
        };
        swipeTimer = new Timer();
        hasstarted = true;
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
       /* indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });*/

    }

    private void getHome() {

        progressBar.showProgress(getActivity());
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String lat = Utility.getPref(getActivity(), Constants.lat, "0");
        String lng = Utility.getPref(getActivity(), Constants.lng, "0");

        Call<BeanHome> call = service.home("28.7494716", "77.05653329999996","renter");

        call.enqueue(new Callback<BeanHome>() {
            @Override
            public void onResponse(Call<BeanHome> call, Response<BeanHome> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    layoutMain.setVisibility(View.VISIBLE);
                    mDataList = response.body().getResult();
                    bannerList = mDataList.getBanners();

                    init();
                    trendProduct = mDataList.getTrendingProducts();
                    recentProduct = mDataList.getLatestProducts();
                    mAdapter = new HomeAdapter(getActivity(), trendProduct);
                    mAdapter1 = new HomeAdapter(getActivity(), recentProduct);
                    rcviewRecentadd.setAdapter(mAdapter1);
                    rcviewTopRated.setAdapter(mAdapter);
                    clickListeners();

                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(getActivity());
                    errorHandler.responseOnError(statusCode);
                }
            }

            @Override
            public void onFailure(Call<BeanHome> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (hasstarted) {
            swipeTimer.cancel();
        }
    }

    public void clickListeners(){
        mAdapter1.setClickListener(new ClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("id",mDataList.getTrendingProducts().get(position).getId());
                intent.putExtra("name",mDataList.getTrendingProducts().get(position).getName());
                startActivity(intent);
            }
        });
        mAdapter.setClickListener(new ClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("id",mDataList.getLatestProducts().get(position).getId());
                intent.putExtra("name",mDataList.getLatestProducts().get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void itemClicked(View view, int position) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_seeall:
                Intent intent=new Intent(getActivity(), ProductListActivity.class);
                intent.putExtra("type","latest");
                startActivity(intent);
                break;
            case R.id.txt_seealltop:
                Intent intent1=new Intent(getActivity(), ProductListActivity.class);
                intent1.putExtra("type","trending");
                startActivity(intent1);
                break;
        }
    }
}
