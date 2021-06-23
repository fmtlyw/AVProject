package com.lyw.avproject.parttwo;

import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Build;

import com.lyw.avproject.utils.FileUtils;
import com.lyw.avproject.utils.Pcm2WavUtil;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import androidx.annotation.RequiresApi;


/**
 * 功能描述:
 * Created on 2021/6/21.
 *
 * @author lyw
 */
public class AudioManger {

    private volatile static AudioManger mAudioManger;

    /**
     * 音频源：音频输入-麦克风（一般是麦克风）
     */
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;

    /**
     * 采样率
     * 44100是目前的标准，但是某些设备仍然支持22050，16000，11025
     * 采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
     */
    private final static int AUDIO_SAMPLE_RATE = 44100;

    /**
     * 音频通道 单声道
     */
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;

    //指定捕获音频的声道数目。在AudioFormat类中指定用于此的常量
    private static final int mChannelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO; //单声道

    /**
     * 音频格式：PCM编码
     */
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;


    //音频流类型
    private static final int mStreamType = AudioManager.STREAM_MUSIC;


    //STREAM的意思是由用户在应用程序通过write方式把数据一次一次得写到audiotrack中。这个和我们在socket中发送数据一样，
    // 应用层从某个地方获取数据，例如通过编解码得到PCM数据，然后write到audiotrack。
    private static int mMode = AudioTrack.MODE_STREAM;


    private final String pcmFileName = "data/data/com.lyw.avproject" + "/record.pcm";
    private final String wavFileName = "data/data/com.lyw.avproject" + "/record.wav";


    private int minBufferSize;
    private AudioRecord audioRecord;
    private AudioTrack mAudioTrack;
    private boolean mRecording;
    private boolean mPlaying;
    private AudioRecordThread mAudioRecordThread;
    private AudioTrackThread mAudioTrackThread;
    private DataInputStream dis;


    public static AudioManger getInstance() {
        if (mAudioManger == null) {
            synchronized (AudioManger.class) {
                if (mAudioManger == null) {
                    mAudioManger = new AudioManger();
                }
            }
        }
        return mAudioManger;
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        mAudioRecordThread = new AudioRecordThread();
        mAudioRecordThread.start();
    }

    private class AudioRecordThread extends Thread {
        @Override
        public void run() {

            mRecording = true;
            minBufferSize = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
            audioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, minBufferSize);
            audioRecord.startRecording();
            byte[] recordData = new byte[minBufferSize];

            while (mRecording && !isInterrupted()) {
                int read = audioRecord.read(recordData, 0, minBufferSize);
                if (read > 0) {
                    FileUtils.writeByte(recordData, pcmFileName);
                }
            }

            audioRecord.stop();

            mRecording = false;
            Pcm2WavUtil.pcmToWav(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, minBufferSize, pcmFileName, wavFileName);
        }
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        if (mAudioRecordThread != null) {
            mAudioRecordThread.interrupt();
            mAudioRecordThread = null;
        }
        mRecording = false;
    }

    /**
     * 开始播放
     */
    public void startPaly() {
        mAudioTrackThread = new AudioTrackThread();
        mAudioTrackThread.start();
    }


    public class AudioTrackThread extends Thread {
        int mMinBufferSize;

        /**
         * 1、获取容器大小
         * 2、创建AudioTrack
         * 3、创建容器
         * 4、获取PCM文件,转成DataInputStream
         * 5、AudioTrack播放
         */

        @RequiresApi(api = Build.VERSION_CODES.M)
        public AudioTrackThread() {
            mMinBufferSize = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_OUT_STEREO, AUDIO_ENCODING);

            mAudioTrack = new AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AUDIO_ENCODING)
                            .setSampleRate(AUDIO_SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                            .build())
                    .setBufferSizeInBytes(mMinBufferSize)
                    .build();

//            mAudioTrack = new AudioTrack(mStreamType,AUDIO_SAMPLE_RATE,mChannelConfig, AUDIO_ENCODING, mMinBufferSize, mMode);
        }

        @Override
        public void run() {
            byte[] trackData = new byte[mMinBufferSize];
            try {
                mPlaying = true;
//                mAudioTrack.play();
                File file = new File(wavFileName);
                dis = new DataInputStream(new FileInputStream(file));

                //可以找到，有数据
                while (mPlaying && dis.available() > 0) {

                    int read = dis.read(trackData);

                    //若读取有错则跳过
                    if (AudioTrack.ERROR_INVALID_OPERATION == read
                            || AudioTrack.ERROR_BAD_VALUE == read) {
                        continue;
                    }
                    if (read != 0 && read != -1) {
                        //把数据写入AudioTrack
                        mAudioTrack.play();
                        mAudioTrack.write(trackData,0,mMinBufferSize);
                    }
                }
                mPlaying = false;
                mAudioTrack.stop();
                mAudioTrack.release();
                dis.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPlay(){
        if (mAudioTrackThread != null) {
            mAudioTrackThread.interrupt();
            mAudioTrackThread = null;
        }
        mPlaying = false;
    }
}
