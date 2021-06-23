# AVProject主要记录Android音视频开发学习线路
## 学习线路
1、用ImageView , SurfaceView ，自定义View绘制一张图片

2、使用AudioRecord和AudioTrack API完成音频PCM数据的采集和播放，并实现读写音频wav文件

3、使用Camera API进行视频的采集，分别使用SurfaceView、TextureView来预览Camera 数据，取到NV21的数据回调

4、学习MediaExtractor和MediaMuxer API，知道如何解析和封装mp4文件

5、学习OpenGL ES API，了解OpenGL开发的基本流程，使用OpenGL绘制图形

6、学习OpenGL ES API，学习纹理绘制，能够使用OpenGL显示一张图片

7、学习MediaCodec API，完成音频AAC硬编、硬解

8、学习MediaCodec API，完成视频H.264的硬编、硬解

9、串联整个音视频录制流程，完成音视频的采集、编码、封包成mp4输出

10、串联整个音视频播放流程，完成 mp4 的解析、音视频的解码、播放和渲染

11、进一步学习OpenGL，了解如何实现视频的剪裁、旋转、水印、滤镜，并学习OpenGL高级特性，如∶VBO ，VAO，FBO等等

12、学习Android图形图像架构，能够使用GLSurfaceviw绘制Camera预览画面

13、深入研究音视频相关的网络协议，如rtmp , hls，以及封包格式，如:flv , mp4

14、深入学习一些音视频领域的开源项目，如webrtc , ffmpeg , ijkplayer , librtmp等等

15、将ffmpeg库移植到Android平台，结合上面积累的经验，编写一款简易的音视频播放器

16、将x264库移植到Android 平台，结合上面积累的经验，完成视频数据H264软编功能

17、将librtmp库移植到Android 平台，结合上面积累的经验，完成Android RTMP推流功能

18、上面积累的经验，做一款短视频APP，完成如:断点拍摄、添加水印、本地转码、视频剪辑、视频拼接、MV特效等功能

## 音视频内容
核心：
音视频技术=封装技术+视频压缩编码技术+音频压缩编码技术+流媒体协议技术

播放流程: 获取流–>解码–>播放

录制播放流程: 录制音频视频–>剪辑–>编码–>上传服务器

直播流程: 录制音视频–>编码–>流媒体传输–>服务器—>流媒体传输到其他app–>解码–>播放

几个重要的环节：

录制音视频 AudioRecord/MediaRecord

音视频编辑 mp4parser或ffmpeg

音视频编码 aac&h264

上传大文件 网络框架,进度监听,断点续传

流媒体传输 流媒体传输协议rtmp rtsp hls

音视频解码 aac&h264（MPEG-4Part10，h264的功能分为两层：视频编解码层（VCL）和网络提取层（NAL））

渲染播放 MediaPlayer

视频编辑可行性开源方案

ffmpeg(功能强大，包含libavcodec(音视频解码库)和libavformat（音视频格式转换库）)

MediaCodec （android自带）

ijkplayer (bilibili开源的)

mp4parser （软解软编音视频混合）

Vitamio

## 音视频开发步骤

### 采集

采集，在音视频开发中主要针对的是数据从哪里来的问题。图像、视频的可视化数据来自摄像头这毫无疑问，而音频数据则是来自麦克风，关于采集的知识点涉及到如下内容：

系统的摄像头采集接口是什么，怎么用 ？
如：Windows：DirectShow，Linux：V4L2，Android：Camera

系统的摄像头采集的参数怎么配置，都是什么含义 ？
如：分辨率、帧率、预览方向、对焦、闪光灯 等

系统的摄像头输出的图像/视频数据，是什么格式，不同格式有什么区别 ？
如：图片：JPEG，视频数据：NV21，NV12，I420 等

系统的麦克风采集接口是什么，怎么用 ？
如：Windows：DirectShow，Linux：ALSA & OSS，Android：AudioRecord，iOS：Audio Unit 等

系统的麦克风采集参数怎么配置，都是什么含义 ？
如：采样率，通道号，位宽 等

系统的麦克风输出的音频数据，是什么格式？
如：PCM

### 渲染：

渲染，在音视频开发中主要针对的是数据展现的问题。我们知道，图像、视频最终都是要绘制到视图（View层）上面，而音频最终都是要输出到扬声器，因此，做音视频渲染，就要掌握如下的技术知识：

系统提供了哪些 API 可以绘制一张图片或者一帧 YUV 图像数据的 ？
如：
Windows：DirectDraw, Direct3D, GDI，OpenGL 等
Linux： GDI， OpenGL 等
Android：ImageView，SurfaceView，TextureView，OpenGL 等
系统提供了哪些 API 可以播放一个 mp3 或者 pcm 数据 ？
如：
Windows：DirectSound 等
Linux：ALSA & OSS 等
Android：AudioTrack 等
针对图像和音视频的处理，实现方式除了使用系统的 API，大多数也会使用一些优秀的第三方库，通过掌握这些第三方库的原理和使用方法，基本上就可以满足日常音视频处理工作了，这些库包括但不限于：
图像处理：OpenGL，OpenCV，libyuv，ffmpeg 等
视频编解码：x264，OpenH264，ffmpeg 等
音频处理：speexdsp，ffmpeg 等
音频编解码：libfaac，opus，speex，ffmpeg 等

### 传输：

传输，在音视频开发中主要针对的是数据共享的问题，采集完并处理数据以后，我们如何快速传输数据这一难题又摆在了面前，试想如果一个以音视频为主导业务的APP如果在播放过程中非常卡顿的话，用户体验那会是非常糟糕的。因此，解决传输的问题也就摆在了我们的面前。那么，数据究竟如何实现传输共享呢 ？共享，实现细则最重要的一点，就是协议，因此需要具体掌握的协议如下：

打包，音视频在传输前怎么打包，如：FLV，ts，mpeg4 等
直播推流，有哪些常见的协议，如：RTMP，RTSP 等
直播拉流，有哪些常见的协议，如：RTMP，HLS，HDL，RTSP 等
基于 UDP 的协议有哪些？如：RTP/RTCP，QUIC 等

### 补充
编码格式
H.264:低码率，高质量，高容错
开源实现：openh264、x264

H.265：能达到H.264两倍之压缩率，可支持4k分辨率，最高到8k。
开源实现：libde265、x265、vp9

### 对比：
H.265对H.264在码率节省上有较大的优势，在相同RSNR下分别节省了48.3%和75.8%。
H.264在编码时间上有聚到优势，对比VP9和H.265,H.265是vp9的6倍，vp9是H.264的将近40倍。

### 推送协议

1.RTMP
Real Time Messaging Protocol(实时消息传输协议)，基于 TCP，设计用来进行实时数据通信。
RTMP是目前主流的流媒体传输协议，广泛用于直播领域，市面上绝大多数直播产品都采用了这个协议。

2.HLS
http live streaming是由Apple公司定义的基于http的流媒体实时传输协议，可实现流媒体的直播和点播，主要用于ios系统。
原理是将整个流分为多个小的文件来下载，每次只下载 个。客户端只要不停的按顺序播放从服务器获取到的 件，就实现了直播。
分段推送的特点，决定了HLS的延迟一般会高于普通的流媒体直播协议。

3.WebRTC
web real time communication（网页即时通信），是一个支持网页浏览器进行实时语音或者视频对话的API。


