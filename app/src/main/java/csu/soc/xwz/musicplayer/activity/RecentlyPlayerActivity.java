package csu.soc.xwz.musicplayer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import csu.soc.xwz.musicplayer.Adapter.DetailAdapter;
import csu.soc.xwz.musicplayer.Adapter.RecentlyListAdapter;
import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.databinding.ActivityDetailBinding;
import csu.soc.xwz.musicplayer.databinding.ActivityRecentlyPlayerBinding;
import csu.soc.xwz.musicplayer.myView.CharacterListView;
import csu.soc.xwz.musicplayer.pojo.Music;
import csu.soc.xwz.musicplayer.utils.MusicResolver;
import csu.soc.xwz.musicplayer.utils.StaticValue;
import csu.soc.xwz.musicplayer.utils.TitleDecoration;

public class RecentlyPlayerActivity extends BaseActivity {
    private ActivityRecentlyPlayerBinding binding;
    private ListView recentlyListView;//组件对象
    private List<Music> recentMusicList; //数据源
    Toolbar toolbar;
    private RecentlyListAdapter recentlyListAdapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Music> musicArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecentlyPlayerBinding.inflate(getLayoutInflater());
        getSupportActionBar().hide();
        View view = binding.getRoot();
        setContentView(view);
        toolbar = findViewById(R.id.local_toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecentlyPlayerActivity.this.finish();
            }
        });

        if (getIntent().getStringExtra("userName").equals("admin")){
//            recentMusicList = MusicResolver.recentMusicArrayList; //数据源封装实例化
            Log.e("RecentlyPlayerActivity","获取到了intent");
            recentMusicList = myBinder.getRecentMusics("admin");
            reserve();

        }else {
            recentMusicList = new ArrayList<Music>();
        }

        init();
    }
    public void reserve(){

        ArrayList<Music> list = new ArrayList<>();
        for (int i = recentMusicList.size()-1; i >= 0; i--) {
            list.add(recentMusicList.get(i));
        }
        recentMusicList = list;
    }
    public void init() {
        layoutManager = new LinearLayoutManager(this);
        binding.recentlyRecycler.setLayoutManager(layoutManager);
        recentlyListAdapter = new RecentlyListAdapter();
        binding.recentlyRecycler.setAdapter(recentlyListAdapter);


        recentlyListAdapter.setData((ArrayList<Music>) recentMusicList);

//        binding.recentlyRecycler.addItemDecoration(new TitleDecoration(new TitleDecoration.TitleAttributes(this),"recent"));

        recentlyListAdapter.setMyOnItemClickListener(new RecentlyListAdapter.MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(int postion) {
                StaticValue.positon = postion;
                myBinder.setMusicList((ArrayList<Music>) recentMusicList);
                myBinder.setCurrentIndex(postion);
                myBinder.seekTo(0);
                myBinder.play();
            }
        });

        recentlyListAdapter.notifyDataSetChanged();

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


}