package com.farmease.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.activity.SubCategoryActivity;
import com.farmease.app.adapter.CategoryAdapter;
import com.farmease.app.bean.BeanCategory;
import com.farmease.app.helper.ItemDecoration;
import com.farmease.app.model.Category;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.ClickListener;
import com.farmease.app.utility.CustomProgressBar;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentCategory extends Fragment implements ClickListener{

    @BindView(R.id.recycleview_category)
    RecyclerView rvcategory;
    private Unbinder unbinder;
    private CategoryAdapter mAdapter;
    private ArrayList<Category> mDataList;
    private CustomProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder= ButterKnife.bind(this,view);
        progressBar=CustomProgressBar.getInstance();
        mDataList=new ArrayList<>();
        rvcategory.setHasFixedSize(true);
        rvcategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        ItemDecoration itemDecoration = new ItemDecoration(getActivity(), R.dimen.item_offset);
       // rvcategory.addItemDecoration(itemDecoration);
        getCategory();
        return view;
    }

    private void getCategory() {

        progressBar.showProgress(getActivity());
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanCategory> call = service.category("categories");

        call.enqueue(new Callback<BeanCategory>() {
            @Override
            public void onResponse(Call<BeanCategory> call, Response<BeanCategory> response) {

                progressBar.hideProgress();
                if (response.isSuccessful()) {

                    mDataList = new ArrayList<>(Arrays.asList(response.body().getResult()));
                    mAdapter=new CategoryAdapter(getActivity(),mDataList);
                    rvcategory.setAdapter(mAdapter);
                    mAdapter.setClickListener(FragmentCategory.this);

                } else {
                    int statusCode = response.code();
                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(getActivity());
                    errorHandler.responseOnError(statusCode);
                }
            }

            @Override
            public void onFailure(Call<BeanCategory> call, Throwable t) {
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
        Intent intent=new Intent(getActivity(), SubCategoryActivity.class);
        intent.putExtra("cat_name",mDataList.get(position).getCategory_name());
        intent.putExtra("cat_img",mDataList.get(position).getCategory_image());
        intent.putExtra("cat_id",mDataList.get(position).getId());
        startActivity(intent);
    }
}
