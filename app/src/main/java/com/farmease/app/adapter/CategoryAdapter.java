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
import com.farmease.app.model.Category;
import com.farmease.app.utility.ClickListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Activity activity;
    private ClickListener clickListener=null;
    private ArrayList<Category> mDataList;

    public CategoryAdapter(Activity activity, ArrayList<Category> mDataList) {
        this.activity = activity;
        this.mDataList = mDataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public Category getItem(int position) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       holder.productTitle.setText(mDataList.get(position).getCategory_name());
       Glide.with(activity).load(mDataList.get(position).getCategory_image()).into(holder.prodductImage);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView prodductImage;
        private TextView productTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            productTitle=itemView.findViewById(R.id.category_title);
            prodductImage=itemView.findViewById(R.id.category_img);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition());
            }
        }
    }
}