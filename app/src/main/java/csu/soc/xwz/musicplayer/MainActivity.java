package csu.soc.xwz.musicplayer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout mainTabLayout;
    private ViewPager2 mainViewPager2;
    private MyFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mainViewPager2=findViewById(R.id.main_viewPager2);
        mainTabLayout=findViewById(R.id.main_tabLayout);

        initPager();

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
