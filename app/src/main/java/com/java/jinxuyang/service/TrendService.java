package com.java.jinxuyang.service;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrendService {
    public static class TrendRec implements Serializable {
        public String title;
        public String begin;
        public List<Integer> confirmed;
        public List<Integer> cured;
        public List<Integer> dead;
        public TrendRec(String title, String begin, List<Integer> confirmed, List<Integer> cured, List<Integer> dead) {
            this.title = title;
            this.begin = begin;
            this.confirmed = confirmed;
            this.cured = cured;
            this.dead = dead;
        }
    }

    public List<String> chnProvinces, countries;
    private JSONObject jsonObject;

    public TrendService() {
        chnProvinces = new ArrayList<String>();
        countries = new ArrayList<String>();
        getData();
    }

    public void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
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
                        jsonObject = new JSONObject(stringBuilder.toString());
                        Iterator<String> iter = jsonObject.keys();
                        while(iter.hasNext()) {
                            String [] s = iter.next().split("\\|");
                            if (s.length == 2 && s[0].equals("China")) {
                                chnProvinces.add(s[1]);
                            }
                            if (s.length == 1) {
                                countries.add(s[0]);
                            }
                        }
                    } else {
                        throw new Exception("HTTP error, flag=" + flag);
                    }
                } catch(Exception e) {
                    Log.e("exception", e.toString());
                }
            }
        }).start();
    }

    public List<TrendRec> getChnProvinces() {
        List<TrendRec> r = new ArrayList<TrendRec>();
        for (String s : chnProvinces) {
            r.add(getChnProvince(s));
        }
        return r;
    }

    public List<TrendRec> getCountries() {
        List<TrendRec> r = new ArrayList<TrendRec>();
        for (String s : countries) {
            r.add(getCountry(s));
        }
        return r;
    }

    private TrendRec getChnProvince(String chnProvince) {
        return getTrendRec("China|" + chnProvince);
    }

    private TrendRec getCountry(String country) {
        return getTrendRec(country);
    }

    private TrendRec getTrendRec(String entry) {
        List<Integer> confirmed = new ArrayList<Integer>();
        List<Integer> cured = new ArrayList<Integer>();
        List<Integer> dead = new ArrayList<Integer>();
        String begin = "";
        try {
            JSONArray jsonArray = jsonObject.getJSONObject(entry).getJSONArray("data");
            begin = jsonObject.getJSONObject(entry).getString("begin");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray object = jsonArray.getJSONArray(i);
                confirmed.add(object.getInt(0));
                cured.add(object.getInt(2));
                dead.add(object.getInt(3));
            }
        } catch(Exception e) {
            Log.e("exception", e.toString());
        }
        return new TrendRec(entry, begin, confirmed, cured, dead);
    }
}
