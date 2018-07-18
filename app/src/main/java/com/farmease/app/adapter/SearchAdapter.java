package com.farmease.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farmease.app.R;
import com.farmease.app.bean.BeanSearch;
import com.farmease.app.model.CartProduct;
import com.farmease.app.utility.ClickListener;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Activity activity;
    private ClickListener clickListener=null;
    private ArrayList<BeanSearch.SearchItem> mDataList;

    public SearchAdapter(Activity activity, ArrayList<BeanSearch.SearchItem> mDataList) {
        this.activity = activity;
        this.mDataList = mDataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public BeanSearch.SearchItem getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public void setClickListener(ClickListener clicklistener) {
        this.clickListener = clicklistener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_cart_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
      holder.productTitle.setText(mDataList.get(position).getName());
       Glide.with(activity).load(mDataList.get(position).getImage()).into(holder.prodductImage);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView prodductImage,imgDelete;
        private TextView productTitle,productPrice;

        public ViewHolder(View itemView) {
            super(itemView);

            productTitle=itemView.findViewById(R.id.cart_title);
            prodductImage=itemView.findViewById(R.id.cart_img);
            productPrice=itemView.findViewById(R.id.cart_price);
            imgDelete=itemView.findViewById(R.id.img_delete);
            itemView.setOnClickListener(this);
            imgDelete.setVisibility(View.GONE);
            productPrice.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }
}