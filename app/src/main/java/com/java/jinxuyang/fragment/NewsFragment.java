package com.java.jinxuyang.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.java.jinxuyang.R;
import com.java.jinxuyang.activity.NewsDetailActivity;
import com.java.jinxuyang.service.NewsService;

import java.util.LinkedList;

import static com.java.jinxuyang.service.MyApplication.newsService;

/**
 * A fragment representing a list of Items.
 */
public class NewsFragment extends Fragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NewsFragment newInstance() {
        NewsFragment fragment = new NewsFragment();
//        Bundle args = new Bundle();
//        args.putInt("arg", 0);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    enum Mode {
        ALL, HISTORY, SEARCH;
    }
    public Mode mode = Mode.ALL;
    public String type = "all";
    public String keyWord = "";
    private int page = 1;
    private NewsRecyclerViewAdapter mAdapter;
    private RecyclerView recyclerView;
    private Handler refreshHandler, addMoreHandler;
    private SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        Context context = view.getContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.news_list);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_swipe_refresh);
        mAdapter = new NewsRecyclerViewAdapter();
        addMoreHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                LinkedList<NewsService.NewsRec> result =
                        (LinkedList<NewsService.NewsRec>) data.getSerializable("result");
                mAdapter.addAllItem(result);
                if (mode == Mode.SEARCH && mAdapter.mValues.size() < 10) {
                    page += 1;
                    swipeRefreshLayout.setRefreshing(true);
                    newsService.searchNews(keyWord, page, addMoreHandler);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        refreshHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                LinkedList<NewsService.NewsRec> result =
                        (LinkedList<NewsService.NewsRec>) data.getSerializable("result");
                mAdapter.resetItem(result);
                recyclerView.scrollToPosition(0);
                if (mode == Mode.SEARCH && result.size() < 10) {
                    page += 1;
                    swipeRefreshLayout.setRefreshing(true);
                    newsService.searchNews(keyWord, page, addMoreHandler);
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        };

        swipeRefreshLayout.setRefreshing(true);
        newsService.getNews(type, page, refreshHandler);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mode == Mode.ALL) {
                swipeRefreshLayout.setRefreshing(true);
                page = 1;
                newsService.getNews(type, page, refreshHandler);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        RecyclerView.OnScrollListener loadingMoreListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mode == Mode.HISTORY)
                    return;
                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = layoutManager.getChildCount();    //得到显示屏幕内的list数量
                    int totalItemCount = layoutManager.getItemCount();    //得到list的总数量
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();//得到显示屏内的第一个list的位置数position
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        page += 1;
                        if (mode == Mode.ALL) {
                            newsService.getNews(type, page, addMoreHandler);
                        } else {
                            newsService.searchNews(keyWord, page, addMoreHandler);
                        }
                        Toast toast = Toast.makeText(getContext(), "加载更多", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        };
        mAdapter.setOnItemClickListener(new NewsRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NewsService.NewsRec news, int posi) {
                newsService.setNewsRead(news);
                mAdapter.notifyItemChanged(posi);
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("news", news);
                startActivity(intent);
            }
        });
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(loadingMoreListener);
        return view;
    }

    public void switchTo(Mode to) {
        if (to == Mode.ALL) {
            mode = Mode.ALL;
            keyWord = "";
            page = 1;
            swipeRefreshLayout.setRefreshing(true);
            newsService.getNews(type, page, refreshHandler);
        } else if (to == Mode.HISTORY) {
            mode = Mode.HISTORY;
            swipeRefreshLayout.setRefreshing(false);
            mAdapter.resetItem(newsService.getHistoryNews());
            recyclerView.scrollToPosition(0);
            mAdapter.notifyDataSetChanged();
        } else {
            mode = Mode.SEARCH;
            page = 1;
            swipeRefreshLayout.setRefreshing(true);
            newsService.searchNews(keyWord, page, refreshHandler);
        }
    }

}