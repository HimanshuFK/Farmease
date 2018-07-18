package com.farmease.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.adapter.HomeAdapter;
import com.farmease.app.bean.BeanHome;
import com.farmease.app.bean.BeanProducts;
import com.farmease.app.helper.ItemDecoration;
import com.farmease.app.model.Product;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
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

public class ProductListActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview_list)
    RecyclerView rcViewList;
    @BindView(R.id.txt_noData)
    TextView txtNoData;
    @BindView(R.id.backimg)
    ImageView imgBack;
    private Unbinder unbinder;
    private HomeAdapter mAdapter;
    private ArrayList<Product> mdataList;
    private CustomProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        mdataList = new ArrayList<>();
        progressBar = CustomProgressBar.getInstance();
        unbinder = ButterKnife.bind(this);
        rcViewList.setHasFixedSize(true);
        rcViewList.setLayoutManager(new GridLayoutManager(ProductListActivity.this, 2));
        ItemDecoration itemDecoration = new ItemDecoration(ProductListActivity.this, R.dimen.item_offset);
        rcViewList.addItemDecoration(itemDecoration);
        getProductList(getIntent().getExtras().getString("type"));
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getProductList(final String type) {

        progressBar.showProgress(ProductListActivity.this);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String lat = Utility.getPref(ProductListActivity.this, Constants.lat, "0");
        String lng = Utility.getPref(ProductListActivity.this, Constants.lng, "0");

        Call<BeanProducts> call = service.productList("28.7494716", "77.05653329999996", type);

        call.enqueue(new Callback<BeanProducts>() {
            @Override
            public void onResponse(Call<BeanProducts> call, Response<BeanProducts> response) {

                progressBar.hideProgress();

                if (response.isSuccessful()) {

                    mdataList = response.body().getResult();

                    mAdapter = new HomeAdapter(ProductListActivity.this, mdataList);

                    rcViewList.setAdapter(mAdapter);
                    txtNoData.setVisibility(View.GONE);
                    rcViewList.setVisibility(View.VISIBLE);

                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        txtNoData.setVisibility(View.VISIBLE);
                        rcViewList.setVisibility(View.GONE);
                    }
                    /*RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ProductListActivity.this);
                    errorHandler.responseOnError(statusCode);*/
                }
            }

            @Override
            public void onFailure(Call<BeanProducts> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(ProductListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
