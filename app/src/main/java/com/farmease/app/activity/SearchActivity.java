package com.farmease.app.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.adapter.CartAdapter;
import com.farmease.app.adapter.SearchAdapter;
import com.farmease.app.bean.BeanCart;
import com.farmease.app.bean.BeanSearch;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
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

public class SearchActivity extends AppCompatActivity implements ClickListener{

    @BindView(R.id.txt_noItems)
    TextView txtNoItems;
    @BindView(R.id.recyclerview_search)
    RecyclerView rvSearch;
    @BindView(R.id.edtxt_search)
    EditText edtSearch;
    @BindView(R.id.backimg)
    ImageView imgback;
    private CustomProgressBar progressBar;
    private Unbinder unbinder;
    private ArrayList<BeanSearch.SearchItem> mDatalist;
    private SearchAdapter madapter;
    private String searchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        unbinder= ButterKnife.bind(this);
        progressBar=CustomProgressBar.getInstance();
        rvSearch.setHasFixedSize(true);
        rvSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchKey=edtSearch.getText().toString();
                if (searchKey.length()>=3){
                    getSearchItems(searchKey);
                }else if (searchKey.length()==0){
                    rvSearch.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getSearchItems(final String searchKey) {

        progressBar.showProgress(SearchActivity.this);
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String lat = Utility.getPref(SearchActivity.this, Constants.lat, "0");
        String lng = Utility.getPref(SearchActivity.this, Constants.lng, "0");

        Call<BeanSearch> call = service.search("28.7494716", "77.05653329999996",searchKey);

        call.enqueue(new Callback<BeanSearch>() {
            @Override
            public void onResponse(Call<BeanSearch> call, Response<BeanSearch> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    rvSearch.setVisibility(View.VISIBLE);
                    txtNoItems.setVisibility(View.GONE);
                    mDatalist = response.body().getResult();
                    madapter = new SearchAdapter(SearchActivity.this, mDatalist);
                    madapter.setClickListener(SearchActivity.this);
                    rvSearch.setAdapter(madapter);

                } else {

                    int statusCode = response.code();
                    if (statusCode == 404) {
                        rvSearch.setVisibility(View.GONE);
                        txtNoItems.setVisibility(View.VISIBLE);

                    } else {
                        RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(SearchActivity.this);
                        errorHandler.responseOnError(statusCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<BeanSearch> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(SearchActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
        Intent intent=new Intent(SearchActivity.this,ProductActivity.class);
        intent.putExtra("id",mDatalist.get(position).getId());
        intent.putExtra("name",mDatalist.get(position).getName());
        startActivity(intent);
    }
}
