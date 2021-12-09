package com.java.jinxuyang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.java.jinxuyang.R;
import com.java.jinxuyang.fragment.TagChipAdapter;
import com.java.jinxuyang.fragment.TagQuickAdapter;
import com.pchmn.materialchips.ChipView;

import java.util.ArrayList;
import java.util.List;

import static com.java.jinxuyang.service.MyApplication.newsService;

public class TagActivity extends AppCompatActivity {
    RecyclerView recyclerView_active_tag;
    RecyclerView recyclerView_inactive_tag;
    TagChipAdapter adapter_active_tag;
    TagChipAdapter adapter_inactive_tag;
    ChipsLayoutManager chipsLayoutManager_active_tag;
    ChipsLayoutManager chipsLayoutManager_inactive_tag;

    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        layout = (LinearLayout)findViewById(R.id.tag_pop_layout);
        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.tag_kill_frame).setOnClickListener((View v)->{
            finish();
        });
        

        adapter_active_tag = new TagChipAdapter(newsService.activeTags){
            @Override
            public void convert(TagQuickAdapter.VH holder, String data, int position) {
                ChipView tag = (ChipView) holder.getViewWidget(R.id.item_tag);
                tag.setLabel(data);
                if(newsService.activeTags.size() == 1)
                    tag.setDeletable(false);
                else
                    tag.setDeletable(true);
                tag.setLabelColor(getResources().getColor(R.color.colorDetail));

                tag.setOnDeleteClicked((View view) -> {
                    int p = holder.getLayoutPosition();
                    String item = getItem(p);
                    adapter_inactive_tag.addNewItem(item);
                    deleteItem(p);
                });
            }

            @Override
            public void addNewItem(String input) {
                if (mData == null) {
                    mData = new ArrayList<>();
                }
                mData.add(mData.size(), input);
                notifyItemInserted(mData.size() - 1);
                if(newsService.activeTags.size() == 2)
                    notifyItemChanged(0);
            }
            @Override
            public void deleteItem(int position) {
                if (mData == null || mData.isEmpty()) {
                    return;
                }
                mData.remove(position);
                notifyItemRemoved(position);
                if(newsService.activeTags.size() == 1)
                    notifyItemChanged(0);
            }
        };
        adapter_inactive_tag = new TagChipAdapter(newsService.inactiveTags){
            public void convert(TagQuickAdapter.VH holder, String data, int position) {
                ChipView tag = (ChipView) holder.getViewWidget(R.id.item_tag);
                tag.setLabel(data);
                tag.setDeletable(false);
                tag.setLabelColor(getResources().getColor(R.color.colorDetail));

                tag.setOnChipClicked((View view) -> {
                    int p = holder.getLayoutPosition();
                    String item = getItem(p);
                    adapter_active_tag.addNewItem(item);
                    deleteItem(p);
                });
            }

            @Override
            public void addNewItem(String input) {
                if (mData == null) {
                    mData = new ArrayList<>();
                }
                mData.add(mData.size(), input);
                notifyItemInserted(mData.size() - 1);
            }
            @Override
            public void deleteItem(int position) {
                if (mData == null || mData.isEmpty()) {
                    return;
                }
                mData.remove(position);
                notifyItemRemoved(position);
            }
        };

        chipsLayoutManager_active_tag = getChipsLayoutManager();
        chipsLayoutManager_inactive_tag = getChipsLayoutManager();
        recyclerView_active_tag = (RecyclerView) findViewById(R.id.tag_active_view);
        recyclerView_inactive_tag = (RecyclerView) findViewById(R.id.tag_inactive_view);
        recyclerView_active_tag.setLayoutManager(chipsLayoutManager_active_tag);
        recyclerView_inactive_tag.setLayoutManager(getChipsLayoutManager());
        recyclerView_active_tag.setAdapter(adapter_active_tag);
        recyclerView_inactive_tag.setAdapter(adapter_inactive_tag);
        recyclerView_active_tag.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        recyclerView_inactive_tag.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    private ChipsLayoutManager getChipsLayoutManager() {
        return ChipsLayoutManager.newBuilder(this)
                .setChildGravity(Gravity.TOP)
                .setScrollingEnabled(true)
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.CENTER;
                    }
                })
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build();
    }
}