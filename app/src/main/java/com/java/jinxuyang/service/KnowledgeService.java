package com.java.jinxuyang.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class KnowledgeService {

    public static class KnowledgeRec implements Serializable {
        public String label;
        public String description;
        public String image;
        public static class RelationRec {
            public String relation;
            public String label;
            public boolean forward;
            public RelationRec(String relation, String label, boolean forward) {
                this.relation = relation;
                this.label = label;
                this.forward = forward;
            }
        }
        public static class PropertyRec {
            public String key;
            public String value;
            public PropertyRec(String key, String value){
                this.key = key;
                this.value = value;
            }
        }
        public List<RelationRec> relations;
        public List<PropertyRec> properties;

        public KnowledgeRec(String label, String description, String image,
                            List<RelationRec> relations,
                            List<PropertyRec> properties) {
            this.label = label;
            this.description = description;
            this.image = image;
            this.relations = relations;
            this.properties = properties;
        }
    }

    public KnowledgeService(){

    }

    public void getKnowledge(String keyWord, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                KnowledgeRec result = null;
                try {
                    URL url = new URL("https://innovaapi.aminer.cn/covid/api/v1/pneumonia/entityquery?entity=" + keyWord);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5 * 1000);
                    int flag = conn.getResponseCode();
                    if(flag == HttpURLConnection.HTTP_OK) {
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
                        JSONObject object = jsonArray.getJSONObject(0);
                        String label = object.getString("label");
                        String description = "";
                        JSONObject abstractInfo = object.getJSONObject("abstractInfo");
                        String enwiki = abstractInfo.getString("enwiki");
                        String baidu = abstractInfo.getString("baidu");
                        String zhwiki = abstractInfo.getString("zhwiki");
                        if (!enwiki.equals("")) {
                            description = enwiki;
                        }
                        else if(!baidu.equals("")){
                            description = baidu;
                        }
                        else if(!zhwiki.equals("")){
                            description = zhwiki;
                        }
                        String img = "";
                        if (object.has("img")) {
                            img = object.getString("img");
                        }
                        JSONObject covid = abstractInfo.getJSONObject("COVID");
                        List<KnowledgeRec.RelationRec> relations = new LinkedList<>();
                        if (covid.has("relations")) {
                            JSONArray res = covid.getJSONArray("relations");
                            for(int j = 0; j < res.length(); j++){
                                JSONObject obj = res.getJSONObject(j);
                                KnowledgeRec.RelationRec T
                                        = new KnowledgeRec.RelationRec(obj.getString("relation"),
                                        obj.getString("label"),
                                        obj.getBoolean("forward"));
                                relations.add(T);
                            }
                        }

                        List<KnowledgeRec.PropertyRec> properties = new LinkedList<>();
                        if (covid.has("properties")) {
                            JSONObject pro = covid.getJSONObject("properties");
                            Iterator<String> iter = pro.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                String value = pro.getString(key);
                                properties.add(new KnowledgeRec.PropertyRec(key, value));
                            }
                        }
                        result = new KnowledgeRec(label, description, img, relations, properties);
                    } else {
                        throw new Exception("HTTP error, flag=" + flag);
                    }
                } catch(Exception e) {
                    Log.e("exception", e.toString());
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putSerializable("result", result);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();
    }



}
