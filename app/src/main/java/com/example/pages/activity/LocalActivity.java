//package com.example.pages.activity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager2.widget.ViewPager2;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.os.Message;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.example.pages.myView.CircleImageView;
//import com.example.pages.service.MusicService;
//import com.example.pages.entity.Music;
//import com.example.pages.fragment.AlbumFragement;
//import com.example.pages.fragment.DirectoryFragment;
//import com.example.pages.R;
//import com.example.pages.fragment.SingerFragment;
//import com.example.pages.fragment.SingleSongFragment;
//import com.example.pages.adapter.TabAdapter;
//import com.example.pages.utils.MusicResolver;
//import com.example.pages.utils.MyBinderInterface;
//import com.example.pages.utils.StaticValue;
//import com.google.android.material.tabs.TabLayout;
//import com.google.android.material.tabs.TabLayoutMediator;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
//
//public class LocalActivity extends AppCompatActivity implements View.OnClickListener, SingleSongFragment.FragmentCallBack {
//    //活动对象
//    public static LocalActivity activity;
//
//    /**
//     * 活动名称
//     */
//    public static String ActivityName;
//
//    private Toolbar localtoolbar;
//
//    private ViewPager2 viewPager;
//    private TabLayout tabLayout;
//    protected static ArrayList<Fragment> fragments = new ArrayList<>();
//    protected static ArrayList<String> tabs = new ArrayList<>();
//    private SingleSongFragment singleSongFragment = SingleSongFragment.newInstance("","");
//    private AlbumFragement albumFragement = AlbumFragement.newInstance("","");
//    private SingerFragment singerFragment = SingerFragment.newInstance("","");
//    private DirectoryFragment directoryFragment = DirectoryFragment.newInstance("","");
//
//    /** 下方player **/
//    private ImageButton playerButton;
//    private ImageButton nextButton;
//    private ImageButton previousButton;
//    private ImageButton settingButton;
//    private CircleImageView musicPicture;
//    private TextView currentTimeText;
//    private TextView totalTimeText;
//    private TextView singerName;
//    private TextView musicName;
//    private SeekBar seekBar;
//
////    private int position = 0;
////    private MediaPlayer mediaPlayer;
//    private boolean isStop;
//    private int i = 0;
//    private int buttonWitch = 0;
//
//    //自定义Binder对象 用于调用服务中的方法
//    private MyBinderInterface myBinder;
//    //自定义服务连接对象
//    private MyServiceConnection conn;
//
//    //Handler实现向主线程进行传值
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//
//            super.handleMessage(msg);
//            seekBar.setProgress((int) (msg.what));
//            currentTimeText.setText(formatTime(msg.what));
//        }
//    };
//
//    //定义服务连接
//    private class MyServiceConnection implements ServiceConnection {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            myBinder = (MyBinderInterface)service;
//
//            myBinder.setMusicList(MusicResolver.musicArrayList);
//
////            if (myBinder.isStart()) {
////                resumePlaying();
////            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//
//        }
//    }
//
//    //接口，获取fragment传过来的值
//    @Override
//    public void getPosition(int position) {
////        this.position = position;
//        StaticValue.positon = position;
//        myBinder.setCurrentIndex(position);
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_local_ativity);
//
//        activity = this;
//        ActivityName = StaticValue.ActivityName.MainActivityName;
//        StaticValue.MainActivity = this;
//        StaticValue.MainView = findViewById(R.id.view_pager);
//
//        bindIdAndListener();
//
//        /*** tab的切换 ***/
//        initData();
//        initView();
//        conn = new MyServiceConnection();
//
//        Intent intent = new Intent(LocalActivity.this, MusicService.class);
//
//        bindService(intent,conn,Context.BIND_AUTO_CREATE);
//
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {
//                    myBinder.seekTo(progress);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
//    }
//
//    /** 绑定控件和监听事件 **/
//    public void bindIdAndListener() {
//        localtoolbar = findViewById(R.id.local_toolbar);
//        localtoolbar.inflateMenu(R.menu.toolbar_menu);
//
//        //设置返回按钮的事件
//        localtoolbar.setNavigationOnClickListener(this);
//
//        playerButton = findViewById(R.id.img_btn_play);
//        playerButton.setOnClickListener(this);
//
//        nextButton = findViewById(R.id.img_btn_next);
//        nextButton.setOnClickListener(this);
//
//        previousButton = findViewById(R.id.img_btn_previous);
//        previousButton.setOnClickListener(this);
//
//        singerName = findViewById(R.id.text_singer_name);
//        singerName.setOnClickListener(this);
//
//        musicName = findViewById(R.id.text_music_name);
//        musicName.setOnClickListener(this);
//
//        currentTimeText = findViewById(R.id.tv_current_time);
//        currentTimeText.setOnClickListener(this);
//
//        totalTimeText = findViewById(R.id.tv_total_time);
//        totalTimeText.setOnClickListener(this);
//
//        seekBar = findViewById(R.id.seekbar_controller);
//        seekBar.setOnClickListener(this);
//
//        settingButton = findViewById(R.id.img_btn_setting);
//        settingButton.setOnClickListener(this);
//
//        musicPicture = findViewById(R.id.img_btn_music_msg);
//        musicPicture.setOnClickListener(this);
//    }
//
//    /***
//     * tab的切换
//     */
//    private void initData() {
//        if (tabs.contains("单曲"))
//            return;
//        tabs.add("单曲");
//        tabs.add("专辑");
//        tabs.add("歌手");
//        tabs.add("文件夹");
//        fragments.add(singleSongFragment);
//        fragments.add(albumFragement);
//        fragments.add(singerFragment);
//        fragments.add(directoryFragment);
//    }
//
//    private void initView() {
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        viewPager = (ViewPager2) findViewById(R.id.view_pager);
//        //设置TabLayout的模式
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(),getLifecycle(),fragments));
//        //TabLayout和Viewpager2进行关联
//        new TabLayoutMediator(tabLayout, viewPager, true,true,new TabLayoutMediator.TabConfigurationStrategy() {
//            @Override
//            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
//                tab.setText(tabs.get(position));
//            }
//        }).attach();
//        tabLayout.getTabAt(0).select();
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.local_toolbar :
//                //顶部toolbar的返回按钮事件
//
//                break;
//            case R.id.img_btn_play:
//                //暂停和播放按钮
////                if (mediaPlayer.isPlaying()) {
////                    mediaPlayer.pause();
////                    playerButton.setImageResource(R.drawable.play_icon);
////                } else {
////                    mediaPlayer.start();
////                    playerButton.setImageResource(R.drawable.play_pause);
////                }
//                if (myBinder.isPlaying()) {
//                    myBinder.pause();
//                    playerButton.setImageResource(R.drawable.play_icon);
//                } else {
//                    myBinder.play();
//                    playerButton.setImageResource(R.drawable.play_pause);
//                }
//                break;
//            case R.id.img_btn_previous:
//                //若此时未在播放
////                if (!mediaPlayer.isPlaying()) {
////                    playerButton.setImageResource(R.drawable.play_pause);
////                }
//                if (!myBinder.isPlaying()) {
//                    playerButton.setImageResource(R.drawable.play_pause);
//                }
//                buttonWitch = 1;
//                setBtnMode();
//                break;
//
//            case R.id.img_btn_next:
//                //若此时未在播放
////                if (!mediaPlayer.isPlaying()) {
////                    playerButton.setImageResource(R.drawable.play_pause);
////                }
//                if (!myBinder.isPlaying()) {
//                    playerButton.setImageResource(R.drawable.play_pause);
//                }
//                buttonWitch = 2;
//                setBtnMode();
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    //格式化数字
//    private String formatTime(int length) {
//        Date date = new Date(length);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
//        String totaltime = simpleDateFormat.format(date);
//        return totaltime;
//    }
//
//    //prevAndnext() 实现页面的展现
//    public void prevAndnextplaying() {
//        int index = myBinder.getCurrentIndex();
//        musicName.setText(MusicResolver.musicArrayList.get(index).getName());
//        singerName.setText(MusicResolver.musicArrayList.get(index).getAuthor());
//        musicPicture.setImageBitmap(MusicResolver.musicArrayList.get(index).getAlbumBitmap());
//        playerButton.setImageResource(R.drawable.play_pause);
//        isStop = false;
//        myBinder.play();
//        totalTimeText.setText(MusicResolver.musicArrayList.get(index).getDuration());
//        seekBar.setMax(MusicResolver.musicArrayList.get(index).getLength());
//
//        MusicThread musicThread = new MusicThread();                                         //启动线程
//        new Thread(musicThread).start();
//    }
//
//    //用于界面的恢复
//    public void resumePlaying() {
//        int index = myBinder.getCurrentIndex();
//        musicName.setText(MusicResolver.musicArrayList.get(index).getName());
//        singerName.setText(MusicResolver.musicArrayList.get(index).getAuthor());
//        musicPicture.setImageBitmap(MusicResolver.musicArrayList.get(index).getAlbumBitmap());
//        if (myBinder.isPlaying())
//            playerButton.setImageResource(R.drawable.play_pause);
//        isStop = false;
//        totalTimeText.setText(MusicResolver.musicArrayList.get(index).getDuration());
//        seekBar.setMax(MusicResolver.musicArrayList.get(index).getLength());
//        seekBar.setProgress(myBinder.getCurrentPosition());
//
//        MusicThread musicThread = new MusicThread();                                         //启动线程
//        new Thread(musicThread).start();
//    }
//
//    //目前是循环播放
//    private void setPlayMode() {
////        if (playMode == 0)//全部循环
////        {
////            if (position == MusicResolver.musicArrayList.size() - 1)//默认循环播放
//        if (myBinder.getCurrentIndex() == MusicResolver.musicArrayList.size() - 1)//默认循环播放
//            {
//                StaticValue.positon = 0;// 第一首
//                myBinder.setCurrentIndex(StaticValue.positon);
//                myBinder.reset();
//                prevAndnextplaying();
//
//            } else {
//            StaticValue.positon++;
//                myBinder.setCurrentIndex(StaticValue.positon);
//                myBinder.reset();
//                prevAndnextplaying();
//            }
////        } else if (playMode == 1)//单曲循环
////        {
////            //position不需要更改
////            mediaPlayer.reset();
////            objectAnimator.pause();
////            needleImagv.startAnimation(rotateAnimation2);
////            prevAndnextplaying(Common.musicList.get(position).path);
////        }
////        else if (playMode == 2)//随机
////        {
////            position = (int) (Math.random() * Common.musicList.size());//随机播放
////            mediaPlayer.reset();
////            objectAnimator.pause();
////            needleImagv.startAnimation(rotateAnimation2);
////            prevAndnextplaying(Common.musicList.get(position).path);
////        }
//    }
//
//    private void setBtnMode() {
////        if (playMode == 0)//全部循环
////        {
//            if (StaticValue.positon == MusicResolver.musicArrayList.size() - 1)//默认循环播放
//            {
//                if (buttonWitch == 1) {
//                    StaticValue.positon--;
//                    myBinder.setCurrentIndex(StaticValue.positon);
//                    myBinder.reset();
//                    prevAndnextplaying();
//                } else if (buttonWitch == 2) {
//                    StaticValue.positon = 0;// 第一首
//                    myBinder.setCurrentIndex(StaticValue.positon);
//                    myBinder.reset();
//                    prevAndnextplaying();
//                }
//            } else if (StaticValue.positon == 0) {
//                if (buttonWitch == 1) {
//                    StaticValue.positon = MusicResolver.musicArrayList.size() - 1;
//                    myBinder.setCurrentIndex(StaticValue.positon);
//                    myBinder.reset();
//                    prevAndnextplaying();
//                } else if (buttonWitch == 2) {
//                    StaticValue.positon++;
//                    myBinder.setCurrentIndex(StaticValue.positon);
//                    myBinder.reset();
//                    prevAndnextplaying();
//                }
//            }
//            else {
//                if(buttonWitch ==1){
//                    StaticValue.positon--;
//                    myBinder.setCurrentIndex(StaticValue.positon);
//                    myBinder.reset();
//                    prevAndnextplaying();
//
//                }else if(buttonWitch ==2){
//                    StaticValue.positon++;
//                    myBinder.setCurrentIndex(StaticValue.positon);
//                    myBinder.reset();
//                    prevAndnextplaying();
//                }
//            }
////        }
////        else if (playMode == 1)//单曲循环
////        {
////            //position不需要更改
////            mediaPlayer.reset();
////            objectAnimator.pause();
////            needleImagv.startAnimation(rotateAnimation2);
////            prevAndnextplaying(Common.musicList.get(position).path);
////        } else if (playMode == 2)//随机
////        {
////            position = (int) (Math.random() * Common.musicList.size());//随机播放
////            mediaPlayer.reset();
////            objectAnimator.pause();
////            needleImagv.startAnimation(rotateAnimation2);
////            prevAndnextplaying(Common.musicList.get(position).path);
////        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        for (Music music : MusicResolver.musicArrayList
//        ) {
//            music.setPlaying(false);
//        }
//        MusicResolver.musicArrayList.get(myBinder.getCurrentIndex()).setPlaying(true);
//        resumePlaying();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        i = 0;
//        isStop = false;
//        if (myBinder.isPlaying()) {
//            myBinder.release();
//        }
//        if (myBinder != null) {
//            myBinder.release();
//        }
//        //取消服务绑定
//        unbindService(conn);
//    }
//
//
//
//    //创建一个类MusicThread实现Runnable接口，实现多线程
//    class MusicThread implements Runnable {
//        @Override
//        public void run() {
//            while (!isStop && MusicResolver.musicArrayList.get(StaticValue.positon) != null) {
//                try {
//                    //让线程睡眠1000毫秒
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                //放送给Handler现在的运行到的时间，进行ui更新
//                handler.sendEmptyMessage(myBinder.getCurrentPosition());
//            }
//        }
//    }
//}





package com.example.pages.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.pages.databinding.ActivityLocalAtivityBinding;
import com.example.pages.entity.Music;
import com.example.pages.fragment.AlbumFragement;
import com.example.pages.fragment.DirectoryFragment;
import com.example.pages.R;
import com.example.pages.fragment.SingerFragment;
import com.example.pages.fragment.SingleSongFragment;
import com.example.pages.adapter.TabAdapter;
import com.example.pages.utils.MusicResolver;
import com.example.pages.utils.StaticValue;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class LocalActivity extends BaseActivity implements SingleSongFragment.FragmentCallBack {
    //活动对象
    public static BaseActivity activity;
    private Toolbar localtoolbar;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    protected static ArrayList<Fragment> fragments = new ArrayList<>();
    protected static ArrayList<String> tabs = new ArrayList<>();
    private SingleSongFragment singleSongFragment = SingleSongFragment.newInstance("","");
    private AlbumFragement albumFragement = AlbumFragement.newInstance("","");
    private SingerFragment singerFragment = SingerFragment.newInstance("","");
    private DirectoryFragment directoryFragment = DirectoryFragment.newInstance("","");

    //接口，获取fragment传过来的值
    @Override
    public void getPosition(int position) {
//        this.position = position;
        StaticValue.positon = position;
        myBinder.setMusicList(MusicResolver.musicArrayList);
        myBinder.setCurrentIndex(position);
        myBinder.seekTo(0);
        myBinder.play();
        Log.e("点击播放",position+"");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local_ativity);

        activity = LocalActivity.this;
        ActivityName = StaticValue.ActivityName.MainActivityName;
        StaticValue.MainActivity = this;
        StaticValue.MainView = findViewById(R.id.view_pager);
        StaticValue.NowActivity = this;

		bindIdAndListener();

        /*** tab的切换 ***/
        initData();
        initView();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if(myBinder.getMusicList() == null || myBinder.getMusicList().size() == 0) {
                        Toast.makeText(LocalActivity.this,"播放列表为空",Toast.LENGTH_SHORT).show();
                        seekBar.setProgress(0);
                    }
                    else
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

    }

    /** 绑定控件和监听事件 **/
    public void bindIdAndListener() {
        localtoolbar = findViewById(R.id.local_toolbar);
        localtoolbar.inflateMenu(R.menu.toolbar_menu);

        //设置返回按钮的事件
        localtoolbar.setNavigationOnClickListener(this);
    }

    /***
     * tab的切换
     */
    private void initData() {
        if (tabs.contains("单曲"))
            return;
        tabs.add("单曲");
        tabs.add("专辑");
        tabs.add("歌手");
        tabs.add("文件夹");
        fragments.add(singleSongFragment);
        fragments.add(albumFragement);
        fragments.add(singerFragment);
        fragments.add(directoryFragment);
    }

    private void initView() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager2) findViewById(R.id.view_pager);

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager(),getLifecycle(),fragments));
        //TabLayout和Viewpager2进行关联
        new TabLayoutMediator(tabLayout, viewPager, true,true,new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabs.get(position));
            }
        }).attach();
        tabLayout.getTabAt(0).select();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_toolbar:
                //顶部toolbar的返回按钮事件
                break;

            case R.id.img_btn_play:
                if(myBinder.getMusicList() == null || myBinder.getMusicList().size() == 0) {
                    Toast.makeText(this,"播放列表为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (myBinder.isPlaying()) {
                        myBinder.pause();
                        Log.e("点击暂停","监听到啦");
                    } else {
                        myBinder.play();
                    }
                }
                break;
            case R.id.img_btn_previous:
                if(myBinder.getMusicList() == null || myBinder.getMusicList().size() == 0) {
                    Toast.makeText(this,"播放列表为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!myBinder.isPlaying()) {
                        playerButton.setImageResource(R.drawable.play_pause);
                    }
                    myBinder.playPrev();
                }
                break;

            case R.id.img_btn_next:
                if(myBinder.getMusicList() == null || myBinder.getMusicList().size() == 0) {
                    Toast.makeText(this,"播放列表为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!myBinder.isPlaying()) {
                        playerButton.setImageResource(R.drawable.play_pause);
                    }
                    myBinder.playNext();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (Music music : MusicResolver.musicArrayList
        ) {
            music.setPlaying(false);
        }
        MusicResolver.musicArrayList.get(myBinder.getCurrentIndex()).setPlaying(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}