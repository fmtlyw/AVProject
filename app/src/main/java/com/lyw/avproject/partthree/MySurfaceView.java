package com.lyw.avproject.partthree;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * 功能描述:
 * Created on 2021/6/23.
 *
 * @author lyw
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private int mWidth = 1920;
    private int mHeight = 1080;

    public MySurfaceView(Context context) {
        super(context);
    }

    public MySurfaceView(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        //开启预览
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mHolder.getSurface() == null) {
            return;
        }
        // 若需要旋转、更改大小或重新设置，请确保证已停止预览
        mCamera.stopPreview();

        setCameraPara();
    }

    /**
     * 设置相机参数
     */
    private void setCameraPara(){
        //设置相机的参数
        Camera.Parameters parameters = mCamera.getParameters();
        //设置yuv格式
        parameters.setPreviewFormat(ImageFormat.NV21);
//        parameters.setPictureSize(mWidth,mHeight);
        //设置摄像头的显示方向
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mCamera.setDisplayOrientation(90);
        } else {
            mCamera.setDisplayOrientation(0);
        }

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(mHolder);
            // 回调要放在 startPreview() 之前
            mCamera.setPreviewCallback(mCameraPreviewCallback);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 预览数据回调
     */
    private Camera.PreviewCallback mCameraPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.d("lyw", "onPreviewFrame: data.length=" + data.length);
        }
    };

    /**
     *
     *切换摄像头
     */
    public void switchCamera(Camera camera){
        mCamera = camera;
        setCameraPara();
    }
}
