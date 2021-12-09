package com.java.jinxuyang.fragment;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.java.jinxuyang.R;
import com.java.jinxuyang.service.TrendService;

import java.util.List;


public class TrendRecyclerViewAdapter extends RecyclerView.Adapter<TrendRecyclerViewAdapter.ViewHolder> {

    private List<TrendService.TrendRec> mValues;
    private OnItemClickListener onItemClickListener;

    public TrendRecyclerViewAdapter(List<TrendService.TrendRec> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(holder.mItem.title);
        holder.mBeginView.setText("Record begins: " + holder.mItem.begin);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, holder.mItem);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, TrendService.TrendRec trend);
    }

    public void resetItem(List<TrendService.TrendRec> items) {
        mValues = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView, mBeginView;
        public TrendService.TrendRec mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.trend_entry_title);
            mBeginView = (TextView) view.findViewById(R.id.trend_entry_begin);
        }
    }
}