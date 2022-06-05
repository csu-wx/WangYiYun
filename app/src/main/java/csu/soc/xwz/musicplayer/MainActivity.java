package csu.soc.xwz.musicplayer;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import csu.soc.xwz.musicplayer.Adapter.MyFragmentAdapter;

public class MainActivity extends AppCompatActivity {
    private TabLayout mainTabLayout;
    private ViewPager2 mainViewPager2;
    private MyFragmentAdapter adapter;
    private ImageButton musicImageButton;
    //实例化helper对象
    SQLiteOpenHelper musicPlayerDatabaseHelper = new MusicPlayerHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();//隐藏顶部导航栏

        mainViewPager2=findViewById(R.id.main_viewPager2);
        mainTabLayout=findViewById(R.id.main_tabLayout);
        //选项卡页面绑定
        initPager();
        //初始化选项卡
        initTabsText();

        musicImageButton = findViewById(R.id.img_btn_music_msg);
        //设置音乐图片按钮点击事件
        setOnMusicImageButtonClick();
        //获得数据库（只读）的引用 写在try的（）里Java会自动将引用离开try } 时自动关闭
        try (SQLiteDatabase db = musicPlayerDatabaseHelper.getReadableDatabase()){
            //TODO read database
        }catch (SQLException e){
            Log.e("sqlite",e.getMessage());
            //提示框显示数据库不可用
            Toast toast = Toast.makeText(this,"Database unavailable",Toast.LENGTH_SHORT);
            toast.show();
        }


    }

    private void setOnMusicImageButtonClick() {
        musicImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

}
