package com.farmease.app.activity;

import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.adapter.CartAdapter;
import com.farmease.app.adapter.ReviewAdapter;
import com.farmease.app.bean.BeanCart;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.bean.BeanUserReview;
import com.farmease.app.model.CartProduct;
import com.farmease.app.model.Category;
import com.farmease.app.model.Product;
import com.farmease.app.model.UserReview;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.AppToast;
import com.farmease.app.utility.ClickListener;
import com.farmease.app.utility.Constants;
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

public class CartActivity extends AppCompatActivity implements ClickListener {
    @BindView(R.id.recyclerview_cart)
    RecyclerView rcViewReviews;
    @BindView(R.id.backimg)
    ImageView imgBack;
    @BindView(R.id.txt_noItems)
    TextView txtNoItems;
    private Unbinder unbinder;
    private CartAdapter madapter;
    private ArrayList<CartProduct> mDatalist;
    private CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        unbinder = ButterKnife.bind(this);
        mDatalist = new ArrayList<>();
        progressBar = CustomProgressBar.getInstance();
        rcViewReviews.setHasFixedSize(true);
        rcViewReviews.setLayoutManager(new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false));
        madapter = new CartAdapter(CartActivity.this, mDatalist);
        rcViewReviews.setAdapter(madapter);

        if (Utility.isInternetConnected(CartActivity.this)) {

            getCart();
        } else {
            AppToast.showToast(CartActivity.this, "No Internet Found", Toast.LENGTH_SHORT);
        }


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void getCart() {

        progressBar.showProgress(CartActivity.this);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String token = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String userId = Utility.getPref(CartActivity.this, Constants.userId, null);

        Call<BeanCart> call = service.cart(userId, token);

        call.enqueue(new Callback<BeanCart>() {
            @Override
            public void onResponse(Call<BeanCart> call, Response<BeanCart> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    rcViewReviews.setVisibility(View.VISIBLE);
                    txtNoItems.setVisibility(View.GONE);
                    mDatalist = response.body().getResult();
                    madapter = new CartAdapter(CartActivity.this, mDatalist);
                    rcViewReviews.setAdapter(madapter);

                } else {

                    int statusCode = response.code();
                    if (statusCode == 404) {
                        rcViewReviews.setVisibility(View.GONE);
                        txtNoItems.setVisibility(View.VISIBLE);

                    } else {
                        RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(CartActivity.this);
                        errorHandler.responseOnError(statusCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<BeanCart> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteCartItem(final int id,final int position) {

        progressBar.showProgress(CartActivity.this);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String token = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String userId = Utility.getPref(CartActivity.this, Constants.userId, null);

        Call<BeanCommon> call = service.deleteCart(id, userId, token);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    mDatalist.remove(position);
                    Toast.makeText(CartActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    if (mDatalist.size()==0){
                        rcViewReviews.setVisibility(View.GONE);
                        txtNoItems.setVisibility(View.VISIBLE);
                    }

                    else{
                        madapter.notifyDataSetChanged();
                    }
                } else {

                    int statusCode = response.code();
                    if (statusCode == 404) {

                    } else {
                        RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(CartActivity.this);
                        errorHandler.responseOnError(statusCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<BeanCommon> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(CartActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void itemClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.img_delete:
                deleteCartItem(mDatalist.get(position).getProduct_id(),position);

                break;
        }

    }
}
