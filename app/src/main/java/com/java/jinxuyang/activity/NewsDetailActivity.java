package com.java.jinxuyang.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.java.jinxuyang.R;
import com.java.jinxuyang.service.NewsService;


public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        NewsService.NewsRec news = (NewsService.NewsRec) intent.getSerializableExtra("news");
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(news.title);
//        toolBarLayout.setTitle(getTitle());

        TextView tvSource = (TextView) findViewById(R.id.news_detail_source);
        TextView tvTime = (TextView) findViewById(R.id.news_detail_time);
        TextView tvContent = (TextView) findViewById(R.id.news_detail_content);
        tvSource.setText(news.source);
        tvTime.setText(news.time);
        tvContent.setText(news.content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, news.title);
                intent.putExtra(Intent.EXTRA_TEXT, news.content);
                startActivity(Intent.createChooser(intent,"share"));
            }
        });
    }
}