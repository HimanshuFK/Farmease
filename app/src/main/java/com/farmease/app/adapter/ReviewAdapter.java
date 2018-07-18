package com.farmease.app.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farmease.app.R;
import com.farmease.app.model.Category;
import com.farmease.app.model.User;
import com.farmease.app.model.UserReview;
import com.farmease.app.utility.ClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private Activity activity;
    private ClickListener clickListener=null;
    private ArrayList<UserReview> mDataList;

    public ReviewAdapter(Activity activity, ArrayList<UserReview> mDataList) {
        this.activity = activity;
        this.mDataList = mDataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public UserReview getItem(int position) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_review, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.userName.setText(mDataList.get(position).getFirst_name()+" "+mDataList.get(position).getLast_name());
        holder.userReview.setText(mDataList.get(position).getReview());
        holder.userrating.setRating(mDataList.get(position).getRating());
        String dtStart = mDataList.get(position).getCreated_at();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'.000Z'");
        SimpleDateFormat output = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);
        try {
            Date date = format.parse(dtStart);
            holder.txtDate.setText(output.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView userImg;
        private TextView userName,userReview,txtReadMore,txtDate;
        private RatingBar userrating;

        public ViewHolder(View itemView) {
            super(itemView);

            userImg=itemView.findViewById(R.id.user_review_img);
            userName=itemView.findViewById(R.id.txt_reviewname);
            userReview=itemView.findViewById(R.id.txt_review);
            userrating=itemView.findViewById(R.id.ratingbar);
            txtDate=itemView.findViewById(R.id.txt_date);
            txtReadMore=itemView.findViewById(R.id.txt_readmore);
            txtReadMore.setVisibility(View.GONE);
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