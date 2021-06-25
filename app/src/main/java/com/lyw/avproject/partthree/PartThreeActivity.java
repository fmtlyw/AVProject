package com.lyw.avproject.partthree;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lyw.avproject.BaseActivity;
import com.lyw.avproject.R;
import com.lyw.avproject.utils.PermissionUtil;


/**
 * 功能描述:打卡三、使用Camera API进行视频的采集，分别使用SurfaceView、TextureView来预览Camera数据，取到NV21的数据回调
 * <p>
 * Created on 2021/6/23.
 *
 * @author lyw
 * <p>
 * 开发步骤：
 * 1、创建Camera
 * 2、设置Camera参数
 * 3、监听预览回调
 * 4、设置画布（把camera采集的数据渲染到SurfaceView（TextureView））
 * 5、开始预览
 */
public class PartThreeActivity extends BaseActivity implements View.OnClickListener {
    private FrameLayout mCameraPreview;
    private TextView mSwitchBtn;
    private Camera mCamera;

    private int mCameraId = 0;
    private MySurfaceView mySurfaceView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_part_three;
    }

    @Override
    protected void initView() {
        mCameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
        mSwitchBtn = (TextView) findViewById(R.id.switch_btn);
        mSwitchBtn.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        if (!PermissionUtil.isHasCameraPermission(this)) {
            PermissionUtil.requestsCameraPermission(this);
            return;
        }

        if (!checkCameraHardware(this)) {
            Toast.makeText(this, "没有相机硬件", Toast.LENGTH_SHORT).show();
            return;
        }
        new InitCameraThread().start();
    }

    /**
     * 检查是否有相机硬件
     *
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.switch_btn:
                switchCamera();
                break;
        }

    }

    /**
     * 切换摄像头
     */
    private void switchCamera() {
        releaseCamera();
        if (mCameraId == 0) {
            mCameraId = 1;
        }else {
            mCameraId = 0;
        }
        mCamera = Camera.open(mCameraId);
        mySurfaceView.switchCamera(mCamera);
    }

    private class InitCameraThread extends Thread {
        @Override
        public void run() {
            if (!isOpenCamera()) {
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mySurfaceView = new MySurfaceView(PartThreeActivity.this, mCamera);
                    mCameraPreview.addView(mySurfaceView);
                }
            });
        }
    }

    /**
     * 判断相机是否打开
     * @return
     */
    private boolean isOpenCamera() {
        boolean isOpen = false;
        try {
            releaseCamera();
            mCamera = Camera.open(mCameraId);
            isOpen = mCamera != null;
        } catch (Exception e) {
        }
        return isOpen;
    }

    /**
     * 是否相机
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }
}
