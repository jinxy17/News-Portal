package com.java.jinxuyang.service;

import android.app.Application;
import android.util.Log;

import com.java.jinxuyang.service.NewsService;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MyApplication extends Application {
    public static NewsService newsService;
    public static TrendService trendService;
    public static KnowledgeService knowledgeService;

    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("info","MyApplication: onCreate()");
        newsService = new NewsService(this);
        trendService = new TrendService();
        knowledgeService = new KnowledgeService();
    }
}