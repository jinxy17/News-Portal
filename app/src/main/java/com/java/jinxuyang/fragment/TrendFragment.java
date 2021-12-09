package com.java.jinxuyang.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.java.jinxuyang.R;
import com.java.jinxuyang.activity.TrendDetailActivity;
import com.java.jinxuyang.service.TrendService;

import static com.java.jinxuyang.service.MyApplication.newsService;
import static com.java.jinxuyang.service.MyApplication.trendService;

/**
 * A fragment representing a list of Items.
 */
public class TrendFragment extends Fragment {

    private boolean isProv = true;

    public TrendFragment() {
    }


    public static TrendFragment newInstance(int columnCount) {
        TrendFragment fragment = new TrendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trend, container, false);
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.trend_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        TrendRecyclerViewAdapter mAdapter = new TrendRecyclerViewAdapter(trendService.getChnProvinces());


        mAdapter.setOnItemClickListener(new TrendRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, TrendService.TrendRec trend) {
                Intent intent = new Intent(context, TrendDetailActivity.class);
                intent.putExtra("trend", trend);
                startActivity(intent);
            }
        });

        ImageView ivSwitch = view.findViewById(R.id.trend_switch);
        ivSwitch.setOnClickListener((View v) ->{
            if (isProv) {
                isProv = false;
                ivSwitch.setImageResource(R.mipmap.icon_country);
                mAdapter.resetItem(trendService.getCountries());
                recyclerView.scrollToPosition(0);
                mAdapter.notifyDataSetChanged();
            } else {
                isProv = true;
                ivSwitch.setImageResource(R.mipmap.icon_prov);
                mAdapter.resetItem(trendService.getChnProvinces());
                recyclerView.scrollToPosition(0);
                mAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        return view;
    }
}