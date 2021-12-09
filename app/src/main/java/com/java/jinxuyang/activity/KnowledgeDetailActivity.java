package com.java.jinxuyang.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.java.jinxuyang.R;
import com.java.jinxuyang.service.KnowledgeService;
import com.java.jinxuyang.service.NewsService;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

import static com.java.jinxuyang.service.MyApplication.knowledgeService;

public class KnowledgeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Context context = this.getApplicationContext();
        Intent intent = getIntent();
        String keyWord = intent.getStringExtra("keyWord");
        toolBarLayout.setTitle("加载中...");

        @SuppressLint("HandlerLeak")
        Handler imageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                Bitmap bitmap = (Bitmap) data.getParcelable("result");
                ImageView iv = findViewById(R.id.knowledge_detail_img);
                if (bitmap != null) {
                    iv.setImageBitmap(bitmap);
                } else {
                    iv.setVisibility(View.GONE);
                }
            }
        };
        @SuppressLint("HandlerLeak")
        Handler serviceHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                KnowledgeService.KnowledgeRec knowledge =
                        (KnowledgeService.KnowledgeRec) data.getSerializable("result");
                if (knowledge == null) {
                    toolBarLayout.setTitle("查找不到关键词");
                } else {
                    toolBarLayout.setTitle(knowledge.label);
                    TextView tv = findViewById(R.id.knowledge_detail_content);
                    tv.setText(knowledge.description);
                    getHttpBitmap(knowledge.image, imageHandler);
                    LinearLayout layout = findViewById(R.id.knowledge_detail_layout);
                    for (KnowledgeService.KnowledgeRec.PropertyRec r : knowledge.properties) {
                        TextView tvb = new TextView(context);
                        tvb.setHeight(40);
                        layout.addView(tvb);

                        CardView cv = new CardView(context);
                        cv.setRadius(2);
                        cv.setCardElevation(2);
                        LinearLayout ll = new LinearLayout(context);
                        ll.setOrientation(ll.HORIZONTAL);
                        ll.setPadding(40, 20, 40, 20);
                        TextView tv1 = new TextView(context);
                        tv1.setText(r.key);
                        tv1.setWidth(200);
                        tv1.setHeight(80);
                        tv1.setTextSize(15);
                        tv1.setTextColor(getColor(R.color.colorTitle));
                        tv1.setGravity(Gravity.CENTER_VERTICAL);
                        TextView tv2 = new TextView(context);
                        tv2.setText(r.value);
//                        tv2.setHeight(80);
                        tv2.setTextSize(15);
                        tv2.setGravity(Gravity.CENTER_VERTICAL);
                        ll.addView(tv1);
                        ll.addView(tv2);
                        cv.addView(ll);
                        layout.addView(cv);
                    }
                    for (KnowledgeService.KnowledgeRec.RelationRec r : knowledge.relations) {
                        TextView tvb = new TextView(context);
                        tvb.setHeight(40);
                        layout.addView(tvb);

                        CardView cv = new CardView(context);
                        cv.setRadius(2);
                        cv.setCardElevation(2);
                        LinearLayout ll = new LinearLayout(context);
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setPadding(40, 20, 40, 20);
                        TextView tv1 = new TextView(context);
                        tv1.setText(r.relation);
                        tv1.setWidth(230);
                        tv1.setHeight(80);
                        tv1.setTextSize(15);
                        tv1.setTextColor(getColor(R.color.colorTitle));
                        tv1.setGravity(Gravity.CENTER_VERTICAL);
                        TextView tv2 = new TextView(context);
                        tv2.setText(r.label);
                        tv2.setTextSize(15);
                        tv2.setGravity(Gravity.CENTER_VERTICAL);
                        ImageView iv1 = new ImageView(context);
                        if (!r.forward) {
                            iv1.setImageResource(R.mipmap.icon_left);
                        } else {
                            iv1.setImageResource(R.mipmap.icon_right);
                        }
                        LinearLayout.LayoutParams iv1ll = new LinearLayout.LayoutParams(120, 80);
                        iv1ll.gravity = Gravity.CENTER;
                        iv1.setLayoutParams(iv1ll);
                        ll.addView(tv1);
                        ll.addView(iv1);
                        ll.addView(tv2);
                        cv.addView(ll);
                        layout.addView(cv);
                    }
                }
            }
        };
        knowledgeService.getKnowledge(keyWord, serviceHandler);
    }

    static public void getHttpBitmap(String url, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = null;
                try {
                    URL myurl = new URL(url);
                    // 获得连接
                    HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
                    conn.setConnectTimeout(5000);//设置超时
                    conn.setDoInput(true);
                    conn.setUseCaches(false);//不缓存
                    conn.connect();
                    InputStream is = conn.getInputStream();//获得图片的数据流
                    bmp = BitmapFactory.decodeStream(is);
                    is.close();
                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putParcelable("result", bmp);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        }).start();

    }
}