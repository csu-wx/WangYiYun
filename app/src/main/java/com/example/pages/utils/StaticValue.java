package com.example.pages.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.example.pages.entity.Music;

import java.util.ArrayList;

public class StaticValue {
    public static int positon;

    /**
     * 主活动
     */
    public static Activity MainActivity;
    /**
     * 当前活动
     */
    public static Activity NowActivity;
    /**
     * 主视野
     */
    public static View MainView;
    /**
     * 播放列表是否为空
     */
    public static boolean IsPlayListNull;
//    /**
//     * 屏幕宽
//     */
//    public static int ScreenWidth;
//    /**
//     * 屏幕高
//     */
//    public static int ScreenHeigtht;
    /**
     * 音乐List
     */
    public static ArrayList<Music> musicArrayList;
    /**
     * 主活动结果码
     */
    public static final int MainActivityRequestCode = 0;
    /**
     * 本地音乐列表活动结果码
     */
    public static final int localMusicListRequestCode = 1;
    /**
     * 是否正在播放音乐
     */
    public static boolean Music_IsPlay = false;


    public static Intent MusicServiceIntent;

    /**
     * 活动名称
     */
    public class ActivityName
    {
        /**
         * 主活动名称
         */
        public static final String MainActivityName = "LocalActivity";
        /**
         * 本地音乐活动名称
         */
        //目前用detail代替
        public static final String LocalMusicActivityName = "DetailActivity";
    }
}
