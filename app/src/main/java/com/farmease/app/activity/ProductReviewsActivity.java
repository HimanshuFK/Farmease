package com.farmease.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.adapter.HomeAdapter;
import com.farmease.app.adapter.ReviewAdapter;
import com.farmease.app.adapter.SlideImageAdapter;
import com.farmease.app.bean.BeanProduct;
import com.farmease.app.bean.BeanUserReview;
import com.farmease.app.model.Product;
import com.farmease.app.model.UserReview;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.CustomProgressBar;
import com.farmease.app.utility.Utility;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductReviewsActivity extends AppCompatActivity {
    @BindView(R.id.recyclerview_reviews)
    RecyclerView rcViewReviews;
    @BindView(R.id.backimg)
    ImageView imgBack;
    private Unbinder unbinder;
    private ReviewAdapter madapter;
    private ArrayList<UserReview> mDatalist;
    private CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_reviews);
        unbinder= ButterKnife.bind(this);
        mDatalist=new ArrayList<>();
        progressBar=CustomProgressBar.getInstance();
        rcViewReviews.setHasFixedSize(true);
        rcViewReviews.setLayoutManager(new LinearLayoutManager(ProductReviewsActivity.this, LinearLayoutManager.VERTICAL, false));
        if (Utility.isInternetConnected(ProductReviewsActivity.this)){
            getUserReviews(getIntent().getExtras().getInt("id"));
        }else {
            AppToast.showToast(ProductReviewsActivity.this,"No Internet Found",Toast.LENGTH_SHORT);
        }


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getUserReviews(final int id) {

        progressBar.showProgress(ProductReviewsActivity.this);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanUserReview> call = service.productReviews("product/reviews/" + id);

        call.enqueue(new Callback<BeanUserReview>() {
            @Override
            public void onResponse(Call<BeanUserReview> call, Response<BeanUserReview> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    mDatalist=response.body().getResult();
                    madapter=new ReviewAdapter(ProductReviewsActivity.this,mDatalist);
                    rcViewReviews.setAdapter(madapter);

                } else {

                    int statusCode = response.code();

                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ProductReviewsActivity.this);
                    errorHandler.responseOnError(statusCode);
                }
            }

            @Override
            public void onFailure(Call<BeanUserReview> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(ProductReviewsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
