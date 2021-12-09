package com.java.jinxuyang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.java.jinxuyang.R;
import com.java.jinxuyang.service.TrendService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TrendDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend_detail);
        Intent intent = getIntent();
        TrendService.TrendRec trend = (TrendService.TrendRec) intent.getSerializableExtra("trend");

        Toolbar toolBar = (Toolbar) findViewById(R.id.trend_detail_toolbar);
        toolBar.setTitle(trend.title);

        LineChart chartView = findViewById(R.id.trend_detail_chart);
        XAxis xAxis = chartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                long millis_offset = TimeUnit.DAYS.toMillis((long) value);
                long millis_begin = 0;
                try {
                    millis_begin = mFormat.parse(trend.begin).getTime();
                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
                return mFormat.format(new Date(millis_offset + millis_begin));
            }
        });
        YAxis rightAxis = chartView.getAxisRight();
        rightAxis.setEnabled(false);

        ArrayList<Entry> confirmedList, curedList, deadList;
        confirmedList = new ArrayList<>();
        curedList = new ArrayList<>();
        deadList = new ArrayList<>();
        for (int i = 0; i < trend.confirmed.size(); i++) {
            confirmedList.add(new Entry(i, trend.confirmed.get(i)));
            curedList.add(new Entry(i, trend.cured.get(i)));
            deadList.add(new Entry(i, trend.dead.get(i)));
        }

        LineDataSet confirmedDset = new LineDataSet(confirmedList, "confirmed");
        confirmedDset.setColor(Color.RED);
        confirmedDset.setCircleColor(Color.RED);
        LineDataSet curedDset = new LineDataSet(curedList, "cured");
        curedDset.setColor(Color.GREEN);
        curedDset.setCircleColor(Color.GREEN);
        LineDataSet deadDset = new LineDataSet(deadList, "dead");
        deadDset.setColor(Color.BLACK);
        deadDset.setCircleColor(Color.BLACK);

        LineData lineData = new LineData(confirmedDset, curedDset, deadDset);
        chartView.setData(lineData);
        chartView.getDescription().setEnabled(false);
        Legend l = chartView.getLegend();
        l.setFormSize(15);
        l.setTextSize(15);

//        lineDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
//        lineDataSet.setValueTextColor(Color.BLACK);
//        lineDataSet.setValueTextSize(18f);
    }
}