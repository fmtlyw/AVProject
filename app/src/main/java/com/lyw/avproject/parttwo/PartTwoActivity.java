package com.lyw.avproject.parttwo;
import android.Manifest;
import android.view.View;
import android.widget.TextView;

import com.lyw.avproject.BaseActivity;
import com.lyw.avproject.R;
import com.lyw.avproject.utils.PermissionUtil;

/**
 * 功能描述:第二关：使用AudioRecord和AudioTrack API完成音频PCM数据的采集和播放，并实现读写音频wav文件
 * Created on 2021/6/18.
 *
 * @author lyw
 */
public class PartTwoActivity extends BaseActivity implements View.OnClickListener{
    private TextView recordCollectTv, recordPlayTv, recordStopTv, playStopTv;

    private static final int MY_PERMISSIONS_REQUEST = 1001;

    /**
     * 需要申请的运行时权限
     */
    private String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_part_two;
    }

    @Override
    protected void initView() {
        recordCollectTv = (TextView) findViewById(R.id.record_collect_tv);
        recordPlayTv = (TextView) findViewById(R.id.record_play_tv);

        recordStopTv = (TextView) findViewById(R.id.record_stop_tv);
        playStopTv = (TextView) findViewById(R.id.play_stop_tv);

        recordCollectTv.setOnClickListener(this);
        recordPlayTv.setOnClickListener(this);
        recordStopTv.setOnClickListener(this);
        playStopTv.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        PermissionUtil.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.record_collect_tv:
                AudioManger.getInstance().startRecord();
                break;

            case R.id.record_stop_tv:
                AudioManger.getInstance().stopRecord();
                break;

            case R.id.play_stop_tv:
                AudioManger.getInstance().stopPlay();
                break;

            case R.id.record_play_tv:
                AudioManger.getInstance().startPaly();
                break;
            default:
                break;
        }
    }
}
