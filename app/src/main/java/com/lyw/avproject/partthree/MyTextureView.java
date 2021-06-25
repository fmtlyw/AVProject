package com.lyw.avproject.partthree;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.TextureView;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * 功能描述:
 * Created on 2021/6/24.
 *
 * @author lyw
 */
public class MyTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    private Camera mCamera;

    public MyTextureView(@NonNull Context context,Camera camera) {
        super(context);
        this.mCamera = camera;
    }

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            //竖屏
            mCamera.setDisplayOrientation(90);
        }else {
            //横屏
            mCamera.setDisplayOrientation(0);
        }
        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewCallback(mCameraPreviewCallback);
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surfaceTexture) {

    }

    /**
     * 预览回调
     */
    private Camera.PreviewCallback mCameraPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.d("lyw", "onPreviewFrame: data.length=" + data.length);
        }
    };

}
