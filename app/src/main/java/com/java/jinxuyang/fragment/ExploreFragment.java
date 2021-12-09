package com.java.jinxuyang.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.java.jinxuyang.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        NewsFragment newsFragment = NewsFragment.newInstance();
        fragmentTransaction.add(R.id.explore_frame, newsFragment);
        fragmentTransaction.commit();


        ImageView ivHistory = view.findViewById(R.id.explore_history);
        ImageView ivSearch = view.findViewById(R.id.explore_search);
        ivHistory.setOnClickListener((View v) ->{
            if (newsFragment.mode == NewsFragment.Mode.HISTORY) {
                newsFragment.switchTo(NewsFragment.Mode.ALL);
                ivHistory.setImageResource(R.mipmap.icon_history);
                ivSearch.setImageResource(R.mipmap.icon_search);
            } else {
                newsFragment.switchTo(NewsFragment.Mode.HISTORY);
                ivHistory.setImageResource(R.mipmap.icon_history_pressed);
                ivSearch.setImageResource(R.mipmap.icon_search);
            }
        });
        ivSearch.setOnClickListener((View v) ->{
            if (newsFragment.mode == NewsFragment.Mode.SEARCH) {
                newsFragment.switchTo(NewsFragment.Mode.ALL);
                ivHistory.setImageResource(R.mipmap.icon_history);
                ivSearch.setImageResource(R.mipmap.icon_search);
            } else {
                ivHistory.setImageResource(R.mipmap.icon_history);
                ivSearch.setImageResource(R.mipmap.icon_search_pressed);
                EditText editText = new EditText(view.getContext());
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(view.getContext());
                inputDialog.setTitle("搜索关键字：").setView(editText);
                inputDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newsFragment.keyWord = editText.getText().toString();
                                newsFragment.switchTo(NewsFragment.Mode.SEARCH);
                            }
                        }).show();
            }
        });
        return view;
    }
}