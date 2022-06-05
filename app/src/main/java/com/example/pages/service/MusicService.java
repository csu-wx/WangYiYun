package com.example.pages.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.pages.R;
import com.example.pages.entity.Music;
import com.example.pages.myView.CircleImageView;
import com.example.pages.utils.MyBinderInterface;
import com.example.pages.utils.StaticValue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 音乐播放服务，绑定下方播放栏
 */
public class MusicService extends Service implements View.OnClickListener{
    public final MyBinder myBinder = new MyBinder();

    //播放器
    private MediaPlayer mediaPlayer;
    //进度
    private int seekLength = 0;
    //当前播放列表下标
    private int currentIndex = 0;
    //播放的音乐列表
    private ArrayList<Music> musicArrayList;

    //音乐服务广播动作字符串
    public final static String MusicServiceReceiverAction = "localmusic.service.receiver";

    //广播接收器
    private MusicServicesBroadcastReceiver musicServicesBroadcastReceiver;

    //本地广播接收器
    private LocalBroadcastManager localBroadcastManager;

    /** 下方player **/
    private ImageButton playerButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private ImageButton settingButton;
    private CircleImageView musicPicture;
    private TextView currentTimeText;
    private TextView totalTimeText;
    private TextView singerName;
    private TextView musicName;
    private SeekBar seekBar;

    /**
     * 浮动视野
     */
    public static View mFloatView;
    /**
     * 根视野
     */
    public static FrameLayout mContentContainer;
    /**
     * 音乐是否在播放
     */
    public static boolean isPlaying;

    /**
     * 定义广播常量
     */
    public class BROADCAST{
        /**
         * 创建音乐底部浮动栏
         */
        public final static int TYPE_CREATE_MUSIC_VIEW = -1;
        /**
         * 播放
         */
        public final static int TYPE_PLAY = 0;
        /**
         * 暂停
         */
        public final static int TYPE_PAUSE = -1;
        /**
         * 下一曲
         */
        public final static int TYPE_NEXT = 2;
        /**
         * 播放进度条最大值
         */
        public final static int TYPE_MAXPROGRESS = 3;
        /**
         * 播放进度条中的当前进度
         */
        public final static int TYPE_CurrentProgress = 4;

        public final static String TYPE_CurrentTime = "currentTime";
        public final static String TYPE_TotalTime = "totalTime";
        /**
         * 停止播放
         */
        public final static int TYPE_STOP = 5;
        /**
         * 增加播放列表
         */
        public final static int TYPE_ADDPLAYLIST = 6;
        /**
         * 设置播放栏的值
         */
        public final static int TYPE_SETPLAYBARVALUE = 7;
        /**
         * 专辑图片
         */
        public final static String TYPE_IMAGENAME = "music_Image";
        /**
         * 音乐名称
         */
        public final static String TYPE_MUSICNAME = "music_Name";
        /**
         * 歌手
         */
        public final static String TYPE_ARITSTNAME = "music_Artist";
        /**
         * 音乐进度最大值
         */
        public final static String TYPE_PROGRESSMAX_NAME = "music_ProgressMax";
        /**
         * 推送消息的时候键值对中的值名称
         */
        public final static String TYPE_TYPENAME = "type";
        public final static String TYPE_VALUENAME = "value";
    }

    /**
     * 内容容器
     */
    private ContentResolver contentResolver;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isPlaying = false;

        mediaPlayer = new MediaPlayer();
        currentIndex = 0;
//        CreateFloatView();
//        if(LocalActivity.activity != null) {
            //初始化广播
            musicServicesBroadcastReceiver = new MusicServicesBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(MusicServiceReceiverAction);
            //本地广播注册
            localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(musicServicesBroadcastReceiver,intentFilter);

            //初始化
            musicArrayList = new ArrayList<>();
//        } else {
//            Log.i("错误","LocalActivity.activity未被赋值！");
//        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.i("服务","onStartCommand: "+isPlaying);
//        CreateFloatView();
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        return super.onUnbind(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(musicServicesBroadcastReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.seekbar_controller:
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            myBinder.seekTo(progress);
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                break;

            case R.id.img_btn_play:
                if (myBinder.isPlaying()) {
                    myBinder.pause();
                    playerButton.setImageResource(R.drawable.play_icon);
                } else {
                    myBinder.play();
                    playerButton.setImageResource(R.drawable.play_pause);
                }
                break;
            case R.id.img_btn_previous:
                if (!myBinder.isPlaying()) {
                    playerButton.setImageResource(R.drawable.play_pause);
                }
                myBinder.playPrev();
                break;

            case R.id.img_btn_next:
                if (!myBinder.isPlaying()) {
                    playerButton.setImageResource(R.drawable.play_pause);
                }
                myBinder.playNext();
                break;

            default:
                break;
        }
    }

    /**
     *  播放音乐广播接收器
     */
    public class MusicServicesBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){

        }
    }

    private class MyBinder extends Binder implements MyBinderInterface {
        @Override
        public MusicService getService() {
            return MusicService.this;
        }

        @Override
        public void reset() {
            mediaPlayer.reset();
        }

        @Override
        public void pause() {
            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                seekLength = mediaPlayer.getCurrentPosition();
            }
            StaticValue.Music_IsPlay = false;
            Intent intent = new Intent(MusicServiceReceiverAction);
            intent.putExtra(BROADCAST.TYPE_TYPENAME, BROADCAST.TYPE_PAUSE);
            localBroadcastManager.sendBroadcast(intent);
        }

        @Override
        public void resume() {
            mediaPlayer.seekTo(seekLength);
            mediaPlayer.start();
            StaticValue.Music_IsPlay = true;
        }

        @Override
        public void play() {
            mediaPlayer.reset();

            try {
                mediaPlayer.setDataSource(musicArrayList.get(currentIndex).getUrl());
                mediaPlayer.prepare();
                mediaPlayer.seekTo(seekLength);
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(!mediaPlayer.isPlaying()){
//                        setPlayMode();
                        playNext();
                    }
                }
            });
            } catch (IOException e) {
                e.printStackTrace();
            }
            StaticValue.Music_IsPlay = true;
            isPlaying = true;
            Intent intent = new Intent(MusicServiceReceiverAction);
            intent.putExtra(BROADCAST.TYPE_TYPENAME, BROADCAST.TYPE_SETPLAYBARVALUE);
            localBroadcastManager.sendBroadcast(intent);
        }

        @Override
        public void playNext() {
            currentIndex += 1;
            if (currentIndex >= musicArrayList.size()){
                currentIndex = 0;
            }
            seekLength = 0;
            play();
        }

        @Override
        public void playPrev() {
            currentIndex -= 1;
            if (currentIndex < 0){
                currentIndex = musicArrayList.size() - 1;
            }
            seekLength = 0;
            play();
        }

        @Override
        public void release() {
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        @Override
        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int length) {
            seekLength = length;
            mediaPlayer.seekTo(length);
            Intent intent = new Intent(MusicServiceReceiverAction);
            intent.putExtra(BROADCAST.TYPE_TYPENAME, BROADCAST.TYPE_SETPLAYBARVALUE);
            localBroadcastManager.sendBroadcast(intent);
        }

        @Override
        public int getCurrentIndex() {
            return currentIndex;
        }

        @Override
        public void setCurrentIndex(int currentIdx) {
            currentIndex = currentIdx;
        }

        @Override
        public void setPlayMode() {
            //目前是循环播放
//        if (playMode == 0)//全部循环
//        {
//            if (position == MusicResolver.musicArrayList.size() - 1)//默认循环播放
                if (getCurrentIndex() == musicArrayList.size() - 1)//默认循环播放
                {
                    currentIndex = 0;// 第一首
                    reset();
                    play();

                } else {
                    currentIndex++;
                    reset();
                    play();
                }
//        } else if (playMode == 1)//单曲循环
//        {
//            //position不需要更改
//            mediaPlayer.reset();
//            objectAnimator.pause();
//            needleImagv.startAnimation(rotateAnimation2);
//            prevAndnextplaying(Common.musicList.get(position).path);
//        }
//        else if (playMode == 2)//随机
//        {
//            position = (int) (Math.random() * Common.musicList.size());//随机播放
//            mediaPlayer.reset();
//            objectAnimator.pause();
//            needleImagv.startAnimation(rotateAnimation2);
//            prevAndnextplaying(Common.musicList.get(position).path);
//        }
        }

        @Override
        public void setMusicList(ArrayList<Music> musicList) {
            musicArrayList = musicList;
        }

        @Override
        public ArrayList<Music> getMusicList() {
            return musicArrayList;
        }

    }


    //格式化数字
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }
}