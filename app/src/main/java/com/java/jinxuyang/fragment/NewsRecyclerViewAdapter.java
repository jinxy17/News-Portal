package com.java.jinxuyang.fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.java.jinxuyang.R;
import com.java.jinxuyang.service.NewsService;

import java.util.LinkedList;
import java.util.List;

import static com.java.jinxuyang.service.MyApplication.newsService;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder> {

    public List<NewsService.NewsRec> mValues;
    private OnItemClickListener onItemClickListener;

    public NewsRecyclerViewAdapter() {
        mValues = new LinkedList<>();
    }

//    public void setmValues(List<NewsService.NewsRec> items) {
//        mValues = items;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mTitleView.setText(holder.mItem.title);
        if (newsService.getNewsRead(holder.mItem)) {
            holder.mTitleView.setTextColor(Color.GRAY);
        } else {
            holder.mTitleView.setTextColor(Color.BLACK);
        }
        holder.mSourceView.setText(holder.mItem.source);
        holder.mTimeView.setText(holder.mItem.time);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, holder.mItem, holder.getAdapterPosition());
//                    onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addAllItem(List<NewsService.NewsRec> list) {
        int pStart = mValues.size();
        int iCount = list.size();
        mValues.addAll(list);
        notifyItemRangeInserted(pStart,iCount);
    }

    public void resetItem(List<NewsService.NewsRec> list) {
        mValues = list;
        notifyDataSetChanged();
    }
//
//    public void removeItem(int position) {
//        mValues.remove(position);
//        notifyItemRemoved(position);
//    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, NewsService.NewsRec news, int posi);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView, mSourceView, mTimeView;
        public final ImageView mThumbnailView;
        public NewsService.NewsRec mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.news_entry_title);
            mSourceView = (TextView) view.findViewById(R.id.news_entry_source);
            mTimeView = (TextView) view.findViewById(R.id.news_entry_time);
            mThumbnailView = (ImageView) view.findViewById(R.id.news_entry_thumbnail);
        }
    }
}