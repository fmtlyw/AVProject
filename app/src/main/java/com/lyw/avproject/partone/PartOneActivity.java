package com.lyw.avproject.partone;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import com.lyw.avproject.BaseActivity;
import com.lyw.avproject.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * 功能描述:第一关:绘制图片
 * Created on 2021/6/18.
 *
 * @author lyw
 */
public class PartOneActivity extends BaseActivity {

    private ImageView imageView;
    private SurfaceView mSv;
    private MyCustomView mMyCustomView;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected int getLayoutId() {
        return R.layout.activity_part_one;
    }

    @Override
    protected void initView() {
        imageView = (ImageView) findViewById(R.id.imageview_iv);
        mSv = (SurfaceView)findViewById(R.id.surfaceview_sv);
        mMyCustomView = (MyCustomView)findViewById(R.id.myCustomView);

//        setSurfaceView();
    }

    @Override
    protected void initEvent() {

    }

    private void setImageView() {

        //第一种方式:通过R.drawabe.xxx加载图片资源
        //imageView.setImageResource(R.mipmap.tupian);

        //第二种方式：加载assests路径的资源
        //imageView.setImageBitmap(getImageFromAssets(this, "tupian.jpg"));

        //第三种方式：加载手机本地的图片
        //imageView.setImageBitmap(getLoacalBitmap());

        //第四种方式：从网络获取
        getNetworkBimap("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fwww.soumeitu.com%2Fwp-content%2Fuploads%2F2019%2F10%2F56f656e22c267.jpg&refer=http%3A%2F%2Fwww.soumeitu.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1625301702&t=80fb08fb2ab844916e5c652301f2acde");
    }

    private void setSurfaceView() {
        mSv.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
                if (surfaceHolder == null) {
                    return;
                }

                //要在子线程绘制

                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.STROKE);

                Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath() + File.separator + "tupian.jpg");

                Canvas canvas = surfaceHolder.lockCanvas();  // 先锁定当前surfaceView的画布
                canvas.drawBitmap(getImageFromAssets(PartOneActivity.this, "tupian.jpg"), 0, 0, paint); //执行绘制操作
                surfaceHolder.unlockCanvasAndPost(canvas); // 解除锁定并显示在界面上
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
            }
        });
    }

    /**
     * 从assests获取图片
     *
     * @param context
     * @param fileName ：图片名称
     * @return
     */
    private static Bitmap getImageFromAssets(Context context, String fileName) {
        if (null == context) {
            return null;
        }
        Bitmap bitmap = null;
        AssetManager assets = context.getResources().getAssets();
        try {
            InputStream inputStream = assets.open(fileName);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 加载本地图片
     *
     * @return
     */
    public static Bitmap getLoacalBitmap() {
        String url = Environment.getExternalStorageDirectory().getPath() + File.separator + "tupian.jpg";
        try {
            FileInputStream fis = new FileInputStream(url);
            ///把流转化为Bitmap图片
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设置网络图片
     * @param url
     */
    public void getNetworkBimap(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL mUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection)mUrl.openConnection();
                    //使用GET方法访问网络
                    conn.setRequestMethod("GET");
                    //超时时间为10秒
                    conn.setConnectTimeout(10000);
                    conn.setDoInput(true);
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }else {
                        Log.d("lyw","请求图片失败");
                    }
                } catch (IOException e) {
                }
            }
        }).start();
    }



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    public static void verifyStoragePermissions(Activity activity) {
        try {
            // 检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
