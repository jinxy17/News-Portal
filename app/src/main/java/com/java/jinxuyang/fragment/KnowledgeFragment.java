package com.java.jinxuyang.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.java.jinxuyang.R;
import com.java.jinxuyang.activity.KnowledgeDetailActivity;
import com.java.jinxuyang.activity.MainActivity;
import com.java.jinxuyang.activity.SplashActivity;


public class KnowledgeFragment extends Fragment {

    public KnowledgeFragment() {
        // Required empty public constructor
    }

    public static KnowledgeFragment newInstance(String param1, String param2) {
        KnowledgeFragment fragment = new KnowledgeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_knowledge, container, false);

        Button buttonSearch = view.findViewById(R.id.knowledge_search);
        EditText editText = view.findViewById(R.id.knowledge_keyword);
        buttonSearch.setOnClickListener((View v) -> {
            Intent intent = new Intent(view.getContext(), KnowledgeDetailActivity.class);
            intent.putExtra("keyWord", editText.getText().toString());
            startActivity(intent);
        });

        return view;
    }
}