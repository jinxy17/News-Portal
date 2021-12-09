package com.java.jinxuyang.service;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NewsService extends AppCompatActivity {

    public List<String> activeTags = new ArrayList<String>();
    public List<String> inactiveTags = new ArrayList<String>();


    public static class NewsRec implements Serializable {
        public String id;
        public String title;
        public String source;
        public String time;
        public String content;
        public NewsRec(){

        }
        public NewsRec(String id, String title, String source, String time, String content) {
            this.id = id;
            this.title = title;
            this.source = source;
            this.time = time;
            this.content = content;
        }
    }

    public List<NewsRec> History = new LinkedList<>(); // 新建一个用于储存已读文章的链表
    private Application app;



    public NewsService(Application app){
        // 构造函数，数据库初始化操作
        this.app = app;
        try {
            FileInputStream fi = app.openFileInput("history.txt");
            ObjectInputStream si = new ObjectInputStream(fi);
            History = (List<NewsRec>)si.readObject();
            si.close();

//            if(!file.exists()){
//                file.createNewFile();
//            }
//            FileInputStream fi = new FileInputStream(file);
//            ObjectInputStream si = new ObjectInputStream(fi);
//            NewsRec news = new NewsRec();
//            while((news = (NewsRec)si.readObject()) != null){
//                History.add(news);
//            }
//            si.close();
        } catch(Exception e) {
            Log.e("exception in reading file", e.toString());
        }
    }


    public void getNews(String type, int page, Handler handler) {
        // type: 'all' / 'event' / 'points' / 'news' / 'paper（新接口）
        // page: >= 1的整数
        // 需要联网，输出第page页的文章

        new Thread(new Runnable() {
            @Override
            public void run() {
                LinkedList<NewsRec> result = new LinkedList<NewsRec>();
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type="
                            + type + "&page=" + page + "&size=20");
                    //打开网络链接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //使用get方式读取数据
                    conn.setRequestMethod("GET");
                    //设置超时5秒
                    conn.setConnectTimeout(5 * 1000);
                    //设置允许读写POST请求必须输入这两行
                    int flag = conn.getResponseCode();
                    if (flag == HttpURLConnection.HTTP_OK) {//如果返回200 则说明请求成功
                        //取得输入流
                        InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        bufferedReader.close();
                        inputStreamReader.close();
                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            result.add(new NewsRec(object.getString("_id"),
                                    object.getString("title"),
                                    object.getString("source"),
                                    object.getString("time"),
                                    object.getString("content")));
                        }
                    } else {
                        throw new Exception("HTTP error, flag=" + flag);
                    }
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putSerializable("result", result);
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch(Exception e) {
                    Log.e("exception", e.toString());
                }
            }
        }).start();
    }

    public void searchNews(String keyWord, int page, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LinkedList<NewsRec> search = new LinkedList<NewsRec>();
                LinkedList<NewsRec> result = new LinkedList<NewsRec>();
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/events/list?type=all&page=" + page + "&size=100");
                    //打开网络链接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    //使用get方式读取数据
                    conn.setRequestMethod("GET");
                    //设置超时5秒
                    conn.setConnectTimeout(5 * 1000);
                    //设置允许读写POST请求必须输入这两行
                    int flag = conn.getResponseCode();
                    if (flag == HttpURLConnection.HTTP_OK) {//如果返回200 则说明请求成功
                        //取得输入流
                        InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        StringBuilder stringBuilder = new StringBuilder();
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        bufferedReader.close();
                        inputStreamReader.close();
                        JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            result.add(new NewsRec(object.getString("_id"),
                                    object.getString("title"),
                                    object.getString("source"),
                                    object.getString("time"),
                                    object.getString("content")));
                        }
                    } else {
                        throw new Exception("HTTP error, flag=" + flag);
                    }
                    int length = result.size();
                    for(int i = 0; i < length; i++){
                        if(result.get(i).content.contains(keyWord)){
                            search.add(result.get(i));
                        }
                    }
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putSerializable("result", search);
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch(Exception e) {
                    Log.e("exception", e.toString());
                }
            }
        }).start();
    }

    public List<NewsRec> getHistoryNews() {
        return History;
    }

    public void setNewsRead(NewsRec news) {
        // 不需要联网，设置id新闻为已读
        if(getNewsRead(news) == false){
            History.add(0, news); //对于事实上已读的新闻执行该方法不会有影响
        }
        try {
            FileOutputStream fo = app.openFileOutput("history.txt", Context.MODE_PRIVATE);
            ObjectOutputStream so = new ObjectOutputStream(fo);
            so.writeObject(History);
            so.close();
//            if(!file.exists()){
//                file.createNewFile();
//            }
//            FileOutputStream fo = new FileOutputStream(file);
//            ObjectOutputStream so = new ObjectOutputStream(fo);
//            for(int i = 0; i < History.size(); i++)
//            {
//                NewsRec temp = History.get(i);
//                so.writeObject(temp);
//            }
//            so.close();
        } catch (Exception e) {
            Log.e("exception in writing file", e.toString());
        }
    }


    public boolean getNewsRead(NewsRec news) {
        // 不需要联网，返回id新闻是否已读（已读为true，未读为false）
        boolean read = false;
        int length = History.size();
        for(int i = 0; i < length; i++){
            if(History.get(i).id.equals(news.id)){
                read = true;
                break;
            }
        }
        return read;
    }

}