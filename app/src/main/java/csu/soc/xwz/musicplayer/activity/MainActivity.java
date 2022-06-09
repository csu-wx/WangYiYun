package csu.soc.xwz.musicplayer.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import csu.soc.xwz.musicplayer.Adapter.MyFragmentAdapter;
import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.SQLite.MusicPlayerHelper;
import csu.soc.xwz.musicplayer.pojo.Music;
import csu.soc.xwz.musicplayer.service.MusicService;
import csu.soc.xwz.musicplayer.utils.MusicResolver;

public class MainActivity extends BaseActivity {

    private TabLayout mainTabLayout; //顶部选项卡
    private ViewPager2 mainViewPager2;//选项卡页面
    private MyFragmentAdapter adapter;//Fragment适配器
    private ImageButton musicImageButton;//正在播放的音乐 图片


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();//隐藏顶部状态栏

        mainViewPager2=findViewById(R.id.main_viewPager2);
        mainTabLayout=findViewById(R.id.main_tabLayout);
        musicImageButton = findViewById(R.id.img_btn_music_msg);


        //选项卡页面绑定
        initPager();
        //选项卡
        initTabsText();
        // 底部播放控件
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if(myBinder.getMusicList() == null || myBinder.getMusicList().size() == 0) {
                        Toast.makeText(MainActivity.this,"播放列表为空",Toast.LENGTH_SHORT).show();
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


        Log.i("sql", "onCreate: 开始插入数据");
        ContentValues contentValues = new ContentValues();
        contentValues.put("USER_NAME","test");
        contentValues.put("MENU_NAME","中文");
        contentValues.put("MENU_IMAGE_RESOURCE_ID",R.drawable.night);

        db.insert("SELF_BUILD_MUSIC_MENU",null,contentValues);

//        Toast toast = Toast.makeText(this,"数据添加成功",Toast.LENGTH_SHORT);
        Log.i("sqlite____", "插入数据成功");


    }

    private void initTabsText() {
        //初始化选项卡文字
        mainTabLayout.addTab(mainTabLayout.newTab().setText("我的"));
        mainTabLayout.addTab(mainTabLayout.newTab().setText("乐库"));
        mainTabLayout.addTab(mainTabLayout.newTab().setText("电台"));
    }

    private void initPager(){
        //viewPager和fragment绑定
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new MyFragmentAdapter(fragmentManager,getLifecycle());
        mainViewPager2.setAdapter(adapter);

        //点击tab更改viewpager属性
        mainTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //滑动viewPager修改tab属性
        mainViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mainTabLayout.selectTab(mainTabLayout.getTabAt(position));
            }
        });

    }
    //底部音乐播放控制
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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


    /**
     * 设置状态栏颜色
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void statusBarTintColor(Activity activity, int color) {
        // 代表 5.0 及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(color);
            return;
        }

        // versionCode > 4.4  and versionCode < 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup androidContainer = (ViewGroup) activity.findViewById(android.R.id.content);
            // 留出高度 setFitsSystemWindows  true代表会调整布局，会把状态栏的高度留出来
            View contentView = androidContainer.getChildAt(0);
            if (contentView != null) {
                contentView.setFitsSystemWindows(true);
            }
            // 在原来的位置上添加一个状态栏
            View statusBarView = createStatusBarView(activity);
            androidContainer.addView(statusBarView, 0);
            statusBarView.setBackgroundColor(color);
        }
    }

    /**
     * 创建一个需要填充statusBarView
     */
    private static View createStatusBarView(Activity activity) {
        View statusBarView = new View(activity);
        ViewGroup.LayoutParams statusBarParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(statusBarParams);
        return statusBarView;
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}
