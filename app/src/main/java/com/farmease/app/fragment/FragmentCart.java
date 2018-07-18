package com.farmease.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.activity.CartActivity;
import com.farmease.app.activity.SubCategoryActivity;
import com.farmease.app.adapter.CartAdapter;
import com.farmease.app.adapter.CategoryAdapter;
import com.farmease.app.bean.BeanCart;
import com.farmease.app.bean.BeanCategory;
import com.farmease.app.bean.BeanCommon;
import com.farmease.app.model.CartProduct;
import com.farmease.app.model.Category;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentCart extends Fragment implements ClickListener {

    @BindView(R.id.recyclerview_cart)
    RecyclerView rvcart;
    @BindView(R.id.txt_noItems)
    TextView txtNoItems;
    private Unbinder unbinder;
    private CartAdapter mAdapter;
    private ArrayList<CartProduct> mDataList;
    private CustomProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        progressBar = CustomProgressBar.getInstance();
        mDataList = new ArrayList<>();
        rvcart.setHasFixedSize(true);
        rvcart.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new CartAdapter(getActivity(), mDataList);
        rvcart.setAdapter(mAdapter);

        if (Utility.isInternetConnected(getActivity())) {
            getCartItems();
        } else {
            AppToast.showToast(getActivity(), "No Internet Found", Toast.LENGTH_SHORT);
        }

        return view;
    }

    private void getCartItems() {

        progressBar.showProgress(getActivity());
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String token = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String userId = Utility.getPref(getActivity(), Constants.userId, null);

        Call<BeanCart> call = service.cart(userId, token);

        call.enqueue(new Callback<BeanCart>() {
            @Override
            public void onResponse(Call<BeanCart> call, Response<BeanCart> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    rvcart.setVisibility(View.VISIBLE);
                    txtNoItems.setVisibility(View.GONE);
                    mDataList = response.body().getResult();
                    mAdapter = new CartAdapter(getActivity(), mDataList);
                    rvcart.setAdapter(mAdapter);
                    mAdapter.setClickListener(FragmentCart.this);

                } else {
                    int statusCode = response.code();
                    if (statusCode == 404) {
                        rvcart.setVisibility(View.GONE);
                        txtNoItems.setVisibility(View.VISIBLE);
                    } else {
                        RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(getActivity());
                        errorHandler.responseOnError(statusCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<BeanCart> call, Throwable t) {
                progressBar.hideProgress();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteCartItem(final int id,final int position) {

        progressBar.showProgress(getActivity());
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);
        String token = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        String userId = Utility.getPref(getActivity(), Constants.userId, null);

        Call<BeanCommon> call = service.deleteCart(id, userId, token);

        call.enqueue(new Callback<BeanCommon>() {
            @Override
            public void onResponse(Call<BeanCommon> call, Response<BeanCommon> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {
                    mDataList.remove(position);
                    Toast.makeText(getActivity(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                    if (mDataList.size()==0){
                        rvcart.setVisibility(View.GONE);
                        txtNoItems.setVisibility(View.VISIBLE);
                    }

                    else{
                        mAdapter.notifyDataSetChanged();
                    }
                } else {

                    int statusCode = response.code();
                    if (statusCode == 404) {

                    } else {
                        RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(getActivity());
                        errorHandler.responseOnError(statusCode);
                    }
                }
            }

            @Override
            public void onFailure(Call<BeanCommon> call, Throwable t) {
                progressBar.hideProgress();
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
        switch (view.getId()) {
            case R.id.img_delete:
                deleteCartItem(mDataList.get(position).getProduct_id(),position);

                break;
        }

    }
}
