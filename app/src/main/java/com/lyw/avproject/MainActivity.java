package com.lyw.avproject;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * MainActivity
 *
 * @author lyw
 * @time 2021/6/1 上午 11:41
 */
public class MainActivity extends BaseActivity implements PartAdapter.OnPartItemClickListener{

    private RecyclerView recyclerView;
    private PartAdapter mPartAdapter;
    private List<PartBeen> mPartList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycle_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initEvent() {
        initData();
        mPartAdapter = new PartAdapter(this, mPartList);
        mPartAdapter.setOnPartItemClickListener(this);
        recyclerView.setAdapter(mPartAdapter);
    }

    /**
     * 创建数据
     */
    private void initData() {
        mPartList = new ArrayList<>();
        mPartList.add(new PartBeen("第一关：绘制图片","com.lyw.avproject.partone.PartOneActivity"));
        mPartList.add(new PartBeen("第二关：使用AudioRecord和AudioTrack API完成音频PCM数据的采集和播放，并实现读写音频wav文件","com.lyw.avproject.parttwo.PartTwoActivity"));
        mPartList.add(new PartBeen("第三关：使用Camera API进行视频的采集，分别使用SurfaceView、TextureView来预览Camera数据，取到NV21的数据回调","com.lyw.avproject.partthree.PartThreeActivity"));
    }

    @Override
    public void onPartItem(String partName, String path) {
        Class partClass = null;
        try {
            partClass = Class.forName(path);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setClass(this,partClass);
        startActivity(intent);
    }
}
