package com.java.jinxuyang.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.java.jinxuyang.R;
import com.java.jinxuyang.activity.MainActivity;
import com.java.jinxuyang.activity.SplashActivity;
import com.java.jinxuyang.activity.TagActivity;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.java.jinxuyang.service.MyApplication.newsService;


public class TypeFragment extends Fragment {
    private TypeFragmentPagerAdapter adapter;

    public TypeFragment() {
        // Required empty public constructor
    }

    public static TypeFragment newInstance(String param1, String param2) {
        TypeFragment fragment = new TypeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_type, container, false);

        newsService.activeTags.clear();
        newsService.activeTags.add("news");
        newsService.activeTags.add("paper");
        newsService.inactiveTags.clear();
        newsService.inactiveTags.add("all");
        adapter = new TypeFragmentPagerAdapter(getChildFragmentManager(), newsService.activeTags);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.type_tablayout);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.type_viewpager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        ImageView iv_additem = (ImageView) view.findViewById(R.id.type_addtype);
        iv_additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TagActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}