package com.farmease.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.BottomSheetDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farmease.app.R;
import com.farmease.app.adapter.HomeAdapter;
import com.farmease.app.adapter.SlideImageAdapter;
import com.farmease.app.bean.BeanProduct;
import com.farmease.app.model.Banner;
import com.farmease.app.model.Product;
import com.farmease.app.network.RetrofitErrorHandler;
import com.farmease.app.network.RetrofitFactory;
import com.farmease.app.services.APIService;
import com.farmease.app.utility.ClickListener;

import java.util.ArrayList;
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

public class ProductActivity extends AppCompatActivity implements View.OnClickListener, ClickListener {
    @BindView(R.id.backimg)
    ImageView img_back;
    @BindView(R.id.category_title)
    TextView txt_categoryname;
    @BindView(R.id.pager)
    ViewPager viewPager;
    @BindView(R.id.txt_noreview)
    TextView txt_noreview;
    @BindView(R.id.layout_description)
    RelativeLayout layout_description;
    @BindView(R.id.layout_specification)
    RelativeLayout layout_specification;
    @BindView(R.id.recyclerview_similiar)
    RecyclerView rcView_similiar;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.user_review_img)
    ImageView imgUserReview;
    @BindView(R.id.txt_reviewname)
    TextView txtReviewName;
    @BindView(R.id.txt_nosimilar)
    TextView txt_nosimilar;
    @BindView(R.id.txt_review)
    TextView txtReview;
    @BindView(R.id.txt_readmore)
    TextView txtReadMore;
    @BindView(R.id.txt_product_name)
    TextView txtProName;
    @BindView(R.id.txt_oldprice)
    TextView txtOldPrice;
    @BindView(R.id.txt_newprice)
    TextView txtNewPrice;
    private TextView txtDescribe;
    @BindView(R.id.layout_review_main)
    RelativeLayout layoutReview;
    private Unbinder unbinder;
    private Product mDataList;
    private Integer[] IMAGES = {R.drawable.bannernew, R.drawable.bannernew, R.drawable.bannernew, R.drawable.bannernew};
    private ArrayList<Banner> bannerList = new ArrayList<Banner>();
    private ArrayList<Product> similarData;
    private int currentPage = 0;
    private HomeAdapter mAdapter;
    private int NUM_PAGES = 0;
    private Timer swipeTimer;
    private String describe;
    private boolean hasStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        unbinder = ButterKnife.bind(this);
        similarData=new ArrayList<>();
        rcView_similiar.setHasFixedSize(true);
        rcView_similiar.setLayoutManager(new LinearLayoutManager(ProductActivity.this, LinearLayoutManager.HORIZONTAL, false));

        txtProName.setText(getIntent().getExtras().getString("pro_name"));
        txt_categoryname.setText(getIntent().getExtras().getString("name"));
        getProduct(getIntent().getExtras().getInt("id"));
        img_back.setOnClickListener(this);
        txtReadMore.setOnClickListener(this);
        layout_description.setOnClickListener(this);
    }

    public void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.view_bottom_sheet, null);
        txtDescribe=view.findViewById(R.id.txt_describe);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        txtDescribe.setText(describe);
        dialog.setContentView(view);
        dialog.show();
    }

    private void init() {

        indicator.setViewPager(viewPager);

        NUM_PAGES = IMAGES.length;

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
        hasStarted = true;
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

    private void getProduct(final int id) {

        // progressBar.showProgress(getActivity());
        Retrofit retrofit = RetrofitFactory.getInstance();
        APIService service = retrofit.create(APIService.class);

        Call<BeanProduct> call = service.product("product/" + id);

        call.enqueue(new Callback<BeanProduct>() {
            @Override
            public void onResponse(Call<BeanProduct> call, Response<BeanProduct> response) {

                //progressBar.hideProgress();
                if (response.isSuccessful()) {

                    mDataList = response.body().getResult();
                    txtOldPrice.setText("$ " +mDataList.getBase_price());
                    if (mDataList.getDiscount()!=0){
                        txtNewPrice.setText("$ "+mDataList.getDiscounted_price());
                    }else {
                        txtNewPrice.setVisibility(View.GONE);
                    }
                    describe=mDataList.getDescription();
                    if (mDataList.getReviews()!=null){

                        txtReviewName.setText(mDataList.getReviews().getFirst_name()+" "+mDataList.getReviews().getLast_name());
                        txtReview.setText(mDataList.getReviews().getReview());

                    }else {
                        layoutReview.setVisibility(View.GONE);
                        txt_noreview.setVisibility(View.VISIBLE);
                    }

                    similarData=mDataList.getSimilarProducts();

                    if (similarData.size()!=0){
                        txt_nosimilar.setVisibility(View.GONE);
                        mAdapter=new HomeAdapter(ProductActivity.this,similarData);
                        rcView_similiar.setAdapter(mAdapter);
                        mAdapter.setClickListener(ProductActivity.this);
                    }else {
                        rcView_similiar.setVisibility(View.GONE);
                        txt_nosimilar.setVisibility(View.VISIBLE);
                    }
                    bannerList = mDataList.getImage();

                    viewPager.setAdapter(new SlideImageAdapter(ProductActivity.this, bannerList));
                    init();
                } else {
                    // progressBar.hideProgress();
                    int statusCode = response.code();

                    RetrofitErrorHandler errorHandler = new RetrofitErrorHandler(ProductActivity.this);
                    errorHandler.responseOnError(statusCode);
                }
            }

            @Override
            public void onFailure(Call<BeanProduct> call, Throwable t) {
                //   progressBar.hideProgress();
                Toast.makeText(ProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (hasStarted)
            swipeTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backimg:
                onBackPressed();
                break;
            case R.id.layout_description:
                showBottomSheetDialog();
                break;
            case R.id.txt_readmore:
                Intent intent=new Intent(ProductActivity.this,ProductReviewsActivity.class);
                intent.putExtra("id",getIntent().getExtras().getInt("id"));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void itemClicked(View view, int position) {

    }
}
