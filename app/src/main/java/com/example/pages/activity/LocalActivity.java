package com.example.pages.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pages.CircleImageView;
import com.example.pages.entity.Music;
import com.example.pages.fragment.AlbumFragement;
import com.example.pages.fragment.DirectoryFragment;
import com.example.pages.R;
import com.example.pages.fragment.SingerFragment;
import com.example.pages.fragment.SingleSongFragment;
import com.example.pages.adapter.TabAdapter;
import com.example.pages.utils.MusicResolver;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class LocalActivity extends AppCompatActivity implements View.OnClickListener, SingleSongFragment.FragmentCallBack {
    private Toolbar localtoolbar;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    protected static ArrayList<Fragment> fragments = new ArrayList<>();
    protected static ArrayList<String> tabs = new ArrayList<>();
    private SingleSongFragment singleSongFragment = SingleSongFragment.newInstance("","");
    private AlbumFragement albumFragement = AlbumFragement.newInstance("","");
    private SingerFragment singerFragment = SingerFragment.newInstance("","");
    private DirectoryFragment directoryFragment = DirectoryFragment.newInstance("","");

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

    private int position = 0;
    private MediaPlayer mediaPlayer;
    private boolean isStop;
    private int i = 0;
    private int buttonWitch = 0;

    //Handler实现向主线程进行传值
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            seekBar.setProgress((int) (msg.what));
            currentTimeText.setText(formatTime(msg.what));
        }
    };

    //接口，获取fragment传过来的值
    @Override
    public void getPosition(int position) {
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local_ativity);

        bindIdAndListener();

        /*** tab的切换 ***/
        initData();
        initView();


//        Intent intent = getIntent();                                                    //通过getIntent()方法实现intent信息的获取
//        position = intent.getIntExtra("position", 0);            //获取position

        mediaPlayer = new MediaPlayer();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
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

        playerButton = findViewById(R.id.img_btn_play);
        playerButton.setOnClickListener(this);

        nextButton = findViewById(R.id.img_btn_next);
        nextButton.setOnClickListener(this);

        previousButton = findViewById(R.id.img_btn_previous);
        previousButton.setOnClickListener(this);

        singerName = findViewById(R.id.text_singer_name);
        singerName.setOnClickListener(this);

        musicName = findViewById(R.id.text_music_name);
        musicName.setOnClickListener(this);

        currentTimeText = findViewById(R.id.tv_current_time);
        currentTimeText.setOnClickListener(this);

        totalTimeText = findViewById(R.id.tv_total_time);
        totalTimeText.setOnClickListener(this);

        seekBar = findViewById(R.id.seekbar_controller);
        seekBar.setOnClickListener(this);

        settingButton = findViewById(R.id.img_btn_setting);
        settingButton.setOnClickListener(this);

        musicPicture = findViewById(R.id.img_btn_music_msg);
        musicPicture.setOnClickListener(this);
    }

    /***
     * tab的切换
     */
    private void initData() {
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
            case R.id.local_toolbar :
                //顶部toolbar的返回按钮事件

                break;
            case R.id.img_btn_play:
                //暂停和播放按钮
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playerButton.setImageResource(R.drawable.play_icon);
                } else {
                    mediaPlayer.start();
                    playerButton.setImageResource(R.drawable.play_pause);
                }
                break;
            case R.id.img_btn_previous:
                //若此时未在播放
                if (!mediaPlayer.isPlaying()) {
                    playerButton.setImageResource(R.drawable.play_pause);
                }
                buttonWitch = 1;
                setBtnMode();
                break;

            case R.id.img_btn_next:
                //若此时未在播放
                if (!mediaPlayer.isPlaying()) {
                    playerButton.setImageResource(R.drawable.play_pause);
                }
                buttonWitch = 2;
                setBtnMode();
                break;

            default:
                break;
        }
    }

    //格式化数字
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");    //规定固定的格式
        String totaltime = simpleDateFormat.format(date);
        return totaltime;
    }

    //prevAndnext() 实现页面的展现
    public void prevAndnextplaying(String path) {
        isStop = false;
        mediaPlayer.reset();
        musicName.setText(MusicResolver.musicArrayList.get(position).getName());
        singerName.setText(MusicResolver.musicArrayList.get(position).getAuthor());
        musicPicture.setImageBitmap(MusicResolver.musicArrayList.get(position).getAlbumBitmap());
        Log.d("position",position+"");
        playerButton.setImageResource(R.drawable.play_pause);

        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();                   // 准备
            mediaPlayer.start();                        // 启动
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(!mediaPlayer.isPlaying()){
                        setPlayMode();
                    }
                }
            });
        } catch (IllegalArgumentException | SecurityException | IllegalStateException
                | IOException e) {
            e.printStackTrace();
        }


        totalTimeText.setText(MusicResolver.musicArrayList.get(position).getDuration());

        seekBar.setMax(MusicResolver.musicArrayList.get(position).getLength());

        MusicThread musicThread = new MusicThread();                                         //启动线程
        new Thread(musicThread).start();
    }


    //目前是循环播放
    private void setPlayMode() {
//        if (playMode == 0)//全部循环
//        {
            if (position == MusicResolver.musicArrayList.size() - 1)//默认循环播放
            {
                position = 0;// 第一首
                mediaPlayer.reset();
                prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());

            } else {
                position++;
                mediaPlayer.reset();
                prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());
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

    private void setBtnMode() {
//        if (playMode == 0)//全部循环
//        {
            if (position == MusicResolver.musicArrayList.size() - 1)//默认循环播放
            {
                if (buttonWitch == 1) {
                    position--;
                    mediaPlayer.reset();
                    prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());
                } else if (buttonWitch == 2) {
                    position = 0;// 第一首
                    mediaPlayer.reset();
                    prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());
                }
            } else if (position == 0) {
                if (buttonWitch == 1) {
                    position = MusicResolver.musicArrayList.size() - 1;
                    mediaPlayer.reset();
                    prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());
                } else if (buttonWitch == 2) {
                    position++;
                    mediaPlayer.reset();
                    prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());
                }
            }
            else {
                if(buttonWitch ==1){
                    position--;
                    mediaPlayer.reset();
                    prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());

                }else if(buttonWitch ==2){
                    position++;
                    mediaPlayer.reset();
                    prevAndnextplaying(MusicResolver.musicArrayList.get(position).getUrl());
                }
            }
//        }
//        else if (playMode == 1)//单曲循环
//        {
//            //position不需要更改
//            mediaPlayer.reset();
//            objectAnimator.pause();
//            needleImagv.startAnimation(rotateAnimation2);
//            prevAndnextplaying(Common.musicList.get(position).path);
//        } else if (playMode == 2)//随机
//        {
//            position = (int) (Math.random() * Common.musicList.size());//随机播放
//            mediaPlayer.reset();
//            objectAnimator.pause();
//            needleImagv.startAnimation(rotateAnimation2);
//            prevAndnextplaying(Common.musicList.get(position).path);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (Music music : MusicResolver.musicArrayList
        ) {
            music.setPlaying(false);
        }
        MusicResolver.musicArrayList.get(position).setPlaying(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        i = 0;
        isStop = false;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }



    //创建一个类MusicThread实现Runnable接口，实现多线程
    class MusicThread implements Runnable {
        @Override
        public void run() {
            while (!isStop && MusicResolver.musicArrayList.get(position) != null) {
                try {
                    //让线程睡眠1000毫秒
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //放送给Handler现在的运行到的时间，进行ui更新
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
            }
        }
    }
}