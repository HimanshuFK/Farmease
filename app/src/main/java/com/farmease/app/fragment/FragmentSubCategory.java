package com.farmease.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.activity.ProductActivity;
import com.farmease.app.adapter.CategoryAdapter;
import com.farmease.app.adapter.HomeAdapter;
import com.farmease.app.bean.BeanCategory;
import com.farmease.app.bean.BeanProducts;
import com.farmease.app.model.Product;
import com.farmease.app.helper.ItemDecoration;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentSubCategory extends Fragment implements ClickListener{

    @BindView(R.id.recycleview_subcategory)
    RecyclerView rvcategory;
    @BindView(R.id.txt_nodata)
    TextView txtNoData;
    private Unbinder unbinder;
    private HomeAdapter mAdapter;
    private CustomProgressBar progressBar;
    private ArrayList<Product> mDataList;
    private String id,cat_id,subcat_id,name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub_category, container, false);
        unbinder= ButterKnife.bind(this,view);
        mDataList=new ArrayList<>();
        progressBar=CustomProgressBar.getInstance();
        rvcategory.setHasFixedSize(true);
        mAdapter=new HomeAdapter(getActivity(),mDataList);
        rvcategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvcategory.setAdapter(mAdapter);
        mAdapter.setClickListener(this);
        ItemDecoration itemDecoration = new ItemDecoration(getActivity(), R.dimen.item_offset);
        //rvcategory.addItemDecoration(itemDecoration);
        if (getArguments()!=null){
            name=getArguments().getString("name");
            id=getArguments().getString("id");
            if (Utility.isInternetConnected(getActivity())){
                getProduct(id);
            }else {
                AppToast.showToast(getActivity(),"No Internet Found",Toast.LENGTH_SHORT);
            }
        }
        return view;
    }

    private void getProduct(final String id) {

       // progressBar.showProgress(getActivity());
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String lat= Utility.getPref(getActivity(), Constants.lat,"0");
        String lng=Utility.getPref(getActivity(), Constants.lng,"0");

        if (id.contains("/")){
            cat_id=id.split("/")[0];
            subcat_id=id.split("/")[1];
        }else {
            cat_id=id;
            subcat_id="";
        }
        Call<BeanProducts> call = service.products("28.7494716","77.05653329999996",cat_id,subcat_id,"renter");

        call.enqueue(new Callback<BeanProducts>() {
            @Override
            public void onResponse(Call<BeanProducts> call, Response<BeanProducts> response) {

                //progressBar.hideProgress();
                if (response.isSuccessful()) {

                    mDataList = response.body().getResult();
                    mAdapter=new HomeAdapter(getActivity(),mDataList);
                    rvcategory.setAdapter(mAdapter);
                    txtNoData.setVisibility(View.GONE);
                    rvcategory.setVisibility(View.VISIBLE);
                    mAdapter.setClickListener(FragmentSubCategory.this);

                } else {
                   // progressBar.hideProgress();
                    int statusCode = response.code();
                    if (statusCode==404){
                       txtNoData.setVisibility(View.VISIBLE);
                       rvcategory.setVisibility(View.GONE);
                    }
                   /* RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(getActivity());
                    errorHandler.responseOnError(statusCode);*/
                }
            }

            @Override
            public void onFailure(Call<BeanProducts> call, Throwable t) {
             //   progressBar.hideProgress();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent=new Intent(getActivity(), ProductActivity.class);
        intent.putExtra("id",mDataList.get(position).getId());
        intent.putExtra("name",mDataList.get(position).getName());
        startActivity(intent);
    }
}
