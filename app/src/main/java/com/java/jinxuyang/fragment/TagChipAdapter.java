package com.java.jinxuyang.fragment;

import android.view.View;

import com.java.jinxuyang.R;
import com.pchmn.materialchips.ChipView;

import java.util.ArrayList;
import java.util.List;

public class TagChipAdapter extends TagQuickAdapter<String> {
    public TagChipAdapter(List<String> data) {
        super(data);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_tag;
    }

    @Override
    public void convert(TagQuickAdapter.VH holder, String data, int position) {

        ChipView tag = (ChipView) holder.getViewWidget(R.id.item_tag);
        tag.setLabel(data);
        tag.setDeletable(true);

        tag.setOnDeleteClicked((View view) -> {
            deleteItem(holder.getLayoutPosition());
        });
    }

    public void addNewItem(String input) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(mData.size(), input);
        notifyItemInserted(mData.size() - 1);
    }

    public void deleteItem(int position) {
        if (mData == null || mData.isEmpty()) {
            return;
        }
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public String getItem(int position) {
        return mData.get(position);
    }
}