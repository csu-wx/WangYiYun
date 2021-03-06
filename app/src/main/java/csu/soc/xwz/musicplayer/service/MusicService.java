package csu.soc.xwz.musicplayer.service;

import static csu.soc.xwz.musicplayer.utils.StaticValue.CLOSE;
import static csu.soc.xwz.musicplayer.utils.StaticValue.NEXT;
import static csu.soc.xwz.musicplayer.utils.StaticValue.PAUSE;
import static csu.soc.xwz.musicplayer.utils.StaticValue.PLAY;
import static csu.soc.xwz.musicplayer.utils.StaticValue.PREV;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import csu.soc.xwz.musicplayer.SQLite.MusicPlayerHelper;
import csu.soc.xwz.musicplayer.activity.BaseActivity;
import csu.soc.xwz.musicplayer.myView.CircleImageView;
import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.pojo.Music;
import csu.soc.xwz.musicplayer.receiver.NotificationClickReceiver;
import csu.soc.xwz.musicplayer.utils.LetterComparater;
import csu.soc.xwz.musicplayer.utils.MusicResolver;
import csu.soc.xwz.musicplayer.utils.MyBinderInterface;
import csu.soc.xwz.musicplayer.utils.StaticValue;

/**
 * ??????????????????????????????????????????
 */
public class MusicService extends Service{
    public final MyBinder myBinder = new MyBinder();
    /**
     * ?????????
     */
    private static NotificationManager manager;
    private static RemoteViews remoteViews;
    private Notification notification;
    /**
     * ??????ID
     */
    private int NOTIFICATION_ID = 1;
    //?????????
    private MediaPlayer mediaPlayer;
    //??????
    private int seekLength = 0;
    //????????????????????????
    private int currentIndex = 0;
    //?????????????????????
    private ArrayList<Music> musicArrayList;

    //?????????????????????????????????
    public final static String MusicServiceReceiverAction = "localmusic.service.receiver";

    //???????????????
    private MusicServicesBroadcastReceiver musicServicesBroadcastReceiver;

    //?????????????????????
    private LocalBroadcastManager localBroadcastManager;

    /** ??????player **/
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
     * ????????????
     */
    public static View mFloatView;
    /**
     * ?????????
     */
    public static FrameLayout mContentContainer;
    /**
     * ?????????????????????
     */
    public static boolean isPlaying;

    /**
     * ??????????????????
     */
    public class BROADCAST{
        /**
         * ???????????????????????????
         */
        public final static int TYPE_CREATE_MUSIC_VIEW = -1;
        /**
         * ??????
         */
        public final static int TYPE_PLAY = 0;
        /**
         * ??????
         */
        public final static int TYPE_PAUSE = -1;
        /**
         * ?????????
         */
        public final static int TYPE_NEXT = 2;
        /**
         * ????????????????????????
         */
        public final static int TYPE_MAXPROGRESS = 3;
        /**
         * ?????????????????????????????????
         */
        public final static int TYPE_CurrentProgress = 4;

        public final static String TYPE_CurrentTime = "currentTime";
        public final static String TYPE_TotalTime = "totalTime";
        /**
         * ????????????
         */
        public final static int TYPE_STOP = 5;
        /**
         * ??????????????????
         */
        public final static int TYPE_ADDPLAYLIST = 6;
        /**
         * ?????????????????????
         */
        public final static int TYPE_SETPLAYBARVALUE = 7;
        /**
         * ????????????
         */
        public final static String TYPE_IMAGENAME = "music_Image";
        /**
         * ????????????
         */
        public final static String TYPE_MUSICNAME = "music_Name";
        /**
         * ??????
         */
        public final static String TYPE_ARITSTNAME = "music_Artist";
        /**
         * ?????????????????????
         */
        public final static String TYPE_PROGRESSMAX_NAME = "music_ProgressMax";
        /**
         * ?????????????????????????????????????????????
         */
        public final static String TYPE_TYPENAME = "type";
        public final static String TYPE_VALUENAME = "value";
    }

    /**
     * ????????????
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

        // ??????????????????
        initRemoteViews();
        Log.e("??????????????????","??????????????????");

        mediaPlayer = new MediaPlayer();
        currentIndex = 0;
        //???????????????
        musicServicesBroadcastReceiver = new MusicServicesBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //??????activity???????????????????????????????????????action
        intentFilter.addAction(PLAY);
        intentFilter.addAction(PREV);
        intentFilter.addAction(NEXT);
        intentFilter.addAction(CLOSE);
        Log.e("???????????????action",PLAY+" "+PREV+" "+NEXT+" "+CLOSE);
        //??????????????????
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        /**
         * ?????????????????????????????????????????????????????????????????????????????????
         *
         * **/
        registerReceiver(musicServicesBroadcastReceiver,intentFilter);
        Log.e("??????????????????","localBroadcastManager");

        //?????????
        musicArrayList = new ArrayList<>();

        //???????????????
        initNotification();
        Log.e("???????????????","");

        getMusicData();
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.i("??????","onStartCommand: "+isPlaying);
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
        if (musicServicesBroadcastReceiver != null)
            unregisterReceiver(musicServicesBroadcastReceiver);
    }

    private void getMusicData() {
        /**ContextCompat.checkSelfPermission???????????????int????????????
         ??????1??????????????????
         ??????2??????????????????
         **/
        int i = ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        //????????????????????????????????????????????????
        if (i != PackageManager.PERMISSION_GRANTED) {
            /**??????????????????
             ??????1????????????
             ??????2????????????????????????Manifest???
             ??????3???requestCode
             **/
            ActivityCompat.requestPermissions(
                    BaseActivity.activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        MusicResolver resolver = new MusicResolver();
        resolver.getMusic(this.getBaseContext());
        //????????????????????????????????????
        Collections.sort(MusicResolver.musicArrayList, new LetterComparater());
    }

    /**
     * ???????????????
     */
    private void initNotification() {
        String channelId = "play_control";
        String channelName = "????????????";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        createNotificationChannel(channelId, channelName, importance);

        //?????????????????????????????????
        Intent intent = new Intent(getApplicationContext(), NotificationClickReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //???????????????
        notification = new NotificationCompat.Builder(this, "play_control")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_seek_bar)
                .setLargeIcon(MusicResolver.getBitmapFromDrawable(this,R.drawable.ic_music))
                .setCustomContentView(remoteViews)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(false)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .build();
    }


    /**
     * ??????????????????
     *
     * @param channelId   ??????id
     * @param channelName ????????????
     * @param importance  ???????????????
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.enableLights(false);
        channel.enableVibration(false);
        channel.setVibrationPattern(new long[]{0});
        channel.setSound(null, null);
        //????????????????????????
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }

    /**
     * ????????????????????????????????????????????????
     */
    private void initRemoteViews() {
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.music_notification);

        //?????????????????????????????????????????????
        Intent intentPrev = new Intent(PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, intentPrev, 0);
        //???prev??????????????????
        remoteViews.setOnClickPendingIntent(R.id.iv_pre, prevPendingIntent);

        //????????????????????????????????????????????????  //???????????????????????????????????????
        Intent intentPlay = new Intent(PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, intentPlay, 0);
        //???play??????????????????
        remoteViews.setOnClickPendingIntent(R.id.iv_play, playPendingIntent);

        //?????????????????????????????????????????????
        Intent intentNext = new Intent(NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, intentNext, 0);
        //???next??????????????????
        remoteViews.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent);

        //??????????????????????????????????????????
        Intent intentClose = new Intent(CLOSE);
        PendingIntent closePendingIntent = PendingIntent.getBroadcast(this, 0, intentClose, 0);
        //???close??????????????????
        remoteViews.setOnClickPendingIntent(R.id.iv_notification_close, closePendingIntent);

        Log.e("?????????????????????????????????","");
    }

    /**
     *  ???????????????????????????
     */
    public class MusicServicesBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MusicReceiver";
        @Override
        public void onReceive(Context context, Intent intent){
            Log.e("????????????","????????????");
            //UI??????
            UIControl(intent.getAction(), TAG);
        }
    }


    /**
     * ?????????UI ?????? ?????????????????????????????????????????????UI
     *
     * @param state ?????????
     * @param tag
     */
    private void UIControl(String state, String tag) {
        switch (state) {
            case PLAY:
                if (mediaPlayer.isPlaying()) {
                    myBinder.pause();
                }
                else {
                    myBinder.play();
                }
                Log.e(tag,PLAY+" or "+PAUSE);
                break;
            case PREV:
                myBinder.playPrev();
                Log.e(tag,PREV);
                break;
            case NEXT:
                myBinder.playNext();
                Log.e(tag,NEXT);
                break;
            case CLOSE:
                //????????????????????????????????????
                myBinder.pause();
                manager.cancel(NOTIFICATION_ID);
                Log.e(tag,CLOSE);
                break;
            default:
                break;
        }
    }

    /**
     * ????????????????????????UI
     */
    public void updateNotificationShow() {
        //??????????????????
        if (mediaPlayer.isPlaying()) {
            remoteViews.setImageViewResource(R.id.iv_play, R.drawable.ic_noti_pause);
        } else {
            remoteViews.setImageViewResource(R.id.iv_play, R.drawable.ic_noti_play);
        }
        //????????????
        remoteViews.setImageViewBitmap(R.id.iv_music_picture, musicArrayList.get(currentIndex).getAlbumBitmap());
        //?????????
        remoteViews.setTextViewText(R.id.tv_musicName, musicArrayList.get(currentIndex).getName());
        //?????????
        remoteViews.setTextViewText(R.id.tv_singer, musicArrayList.get(currentIndex).getAuthor());
        //????????????
        manager.notify(NOTIFICATION_ID, notification);
    }

    private class MyBinder extends Binder implements MyBinderInterface {
        @Override
        public MusicService getService() {
            return MusicService.this;
        }

        @Override
        public List<String> getRecentlyMusic(String userName) {
            BaseActivity.musicPlayerHelper = new MusicPlayerHelper(getBaseContext());
            BaseActivity.db =  BaseActivity.musicPlayerHelper.getWritableDatabase();
            List<String> recentMusicList = new ArrayList<>();    //?????????????????????
            try(SQLiteDatabase db = BaseActivity.db){
                Cursor cursor = db.rawQuery("select MUSIC_ID from RECENTLY_MUSIC where USER_NAME=?",new String[]{userName});
                if (cursor.getCount() != 0){
                    while (cursor.moveToNext()){
                        String musicId = cursor.getString(0);
                        recentMusicList.add(musicId);
                    }
                }
                cursor.close();
            }catch (SQLiteException e){
                e.printStackTrace();
            }
            return recentMusicList;
        }

        @Override
        public void addRecentlyMusic(String USER_NAME,String musicId) {
            //?????????????????????????????????
            if (!haveMusicId(USER_NAME,musicId)){
                insertRecentMusic(USER_NAME,musicId);//????????????????????????
            }else{
                removeBefore(USER_NAME,musicId);
                insertRecentMusic(USER_NAME,musicId);

            }
        }

        private void removeBefore(String USER_NAME,String musicId){
            BaseActivity.musicPlayerHelper = new MusicPlayerHelper(getBaseContext());
            BaseActivity.db =  BaseActivity.musicPlayerHelper.getWritableDatabase();
            try(SQLiteDatabase db = BaseActivity.db){
                db.delete("RECENTLY_MUSIC",
                        "USER_NAME=? AND MUSIC_ID=?",
                        new String[]{USER_NAME,musicId});
            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        private boolean haveMusicId(String USER_NAME,String musicId) {
            boolean flag = false;
            List<String> list = getRecentlyMusic(USER_NAME);
            for (int i = 0;i < list.size();i++){
                if (list.get(i).equals(musicId)){
                    flag = true;
                    break;
                }
            }
            return flag;
        }

        public void insertRecentMusic(String userName,String musicId){
            BaseActivity.musicPlayerHelper = new MusicPlayerHelper(getBaseContext());
            BaseActivity.db =  BaseActivity.musicPlayerHelper.getWritableDatabase();
            try (SQLiteDatabase db = BaseActivity.db){
                ContentValues selfValues = new ContentValues();
                selfValues.put("USER_NAME",userName);
                selfValues.put("MUSIC_ID",musicId);
                long result = db.insert("RECENTLY_MUSIC",null,selfValues);
                Log.d("sqlite", "insertRecentlyMusic: username " + userName +
                        " musicId "+ musicId);

            }catch (SQLiteException e){
                e.printStackTrace();
            }
        }

        public List<Music> getRecentMusics(String userName) {
            int len = MusicResolver.musicArrayList.size();
            Log.e("getRecentMusics","???????????????????????????"+len);
            List<Music> musicList = new ArrayList<>();
            List<String> idList = myBinder.getRecentlyMusic(userName);
            for (String s: idList) {
                for (Music e: MusicResolver.musicArrayList) {
                    if (e.getMusicID().equals(s)){
                        musicList.add(e);
                    }
                }
            }
            Log.e("getRecentMusics","?????????????????????????????????"+len);
            return musicList;
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
            updateNotificationShow();
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
            updateNotificationShow();
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
            //?????????????????????
//        if (playMode == 0)//????????????
//        {
//            if (position == MusicResolver.musicArrayList.size() - 1)//??????????????????
            if (getCurrentIndex() == musicArrayList.size() - 1)//??????????????????
            {
                currentIndex = 0;// ?????????
                reset();
                play();

            } else {
                currentIndex++;
                reset();
                play();
            }
//        } else if (playMode == 1)//????????????
//        {
//            //position???????????????
//            mediaPlayer.reset();
//            objectAnimator.pause();
//            needleImagv.startAnimation(rotateAnimation2);
//            prevAndnextplaying(Common.musicList.get(position).path);
//        }
//        else if (playMode == 2)//??????
//        {
//            position = (int) (Math.random() * Common.musicList.size());//????????????
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


    //???????????????
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //?????????????????????
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }
}
