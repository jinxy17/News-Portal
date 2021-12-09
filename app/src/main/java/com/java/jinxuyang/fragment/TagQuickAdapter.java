package com.java.jinxuyang.fragment;


import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class TagQuickAdapter<T> extends RecyclerView.Adapter<TagQuickAdapter.VH>{
    public List<T> mData;
    public TagQuickAdapter(List<T> data){
        this.mData = data;
    }

    public void switchToNewData(List<T> data){
        this.mData = data;
        notifyDataSetChanged();
    }

    public abstract int getLayoutId(int viewType);

    @Override
    public TagQuickAdapter.VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return TagQuickAdapter.VH.get(parent,getLayoutId(viewType));
    }

    @Override
    public void onBindViewHolder(TagQuickAdapter.VH holder, int position) {
        convert(holder, mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public abstract void convert(TagQuickAdapter.VH holder, T data, int position);

    public static class VH extends RecyclerView.ViewHolder{
        private SparseArray<View> mViews;
        private View mConvertView;

        private VH(View v){
            super(v);
            mConvertView = v;
            mViews = new SparseArray<>();
        }

        public static TagQuickAdapter.VH get(ViewGroup parent, int layoutId){
            View convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new TagQuickAdapter.VH(convertView);
        }

        public <T extends View> T getViewWidget(int id){
            View v = mViews.get(id);
            if(v == null){
                v = mConvertView.findViewById(id);
                mViews.put(id, v);
            }
            return (T)v;
        }

        public void setText(int id, String value){
            TextView view = getViewWidget(id);
            view.setText(value);
        }
    }
}