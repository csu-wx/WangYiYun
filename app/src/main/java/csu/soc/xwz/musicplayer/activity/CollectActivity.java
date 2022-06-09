package csu.soc.xwz.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import csu.soc.xwz.musicplayer.Adapter.CollectAdapter;
import csu.soc.xwz.musicplayer.Adapter.DetailAdapter;
import csu.soc.xwz.musicplayer.R;
import csu.soc.xwz.musicplayer.databinding.ActivityCollectBinding;
import csu.soc.xwz.musicplayer.myView.CharacterListView;
import csu.soc.xwz.musicplayer.pojo.Music;
import csu.soc.xwz.musicplayer.utils.LetterComparater;
import csu.soc.xwz.musicplayer.utils.MusicResolver;
import csu.soc.xwz.musicplayer.utils.StaticValue;
import csu.soc.xwz.musicplayer.utils.TitleDecoration;

public class CollectActivity extends BaseActivity {
    ActivityCollectBinding binding;
    private CollectAdapter collectAdapter;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCollectBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.collectToolbar);
        binding.collectToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectActivity.this.finish();
            }
        });

        init();

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


    }


    //初始化recycler
    private void init() {
        layoutManager = new LinearLayoutManager(this);
        binding.collectRecycler.setLayoutManager(layoutManager);
        collectAdapter = new CollectAdapter();
        binding.collectRecycler.setAdapter(collectAdapter);

        Collections.sort(MusicResolver.collectMusicArrayList, new LetterComparater());
        collectAdapter.setData(MusicResolver.collectMusicArrayList);

        binding.collectRecycler.addItemDecoration(new TitleDecoration(new TitleDecoration.TitleAttributes(this),"collect"));

        initEvent();

        collectAdapter.setMyOnItemClickListener(new CollectAdapter.MyOnItemClickListener() {
            @Override
            public void OnItemClickListener(int postion) {
                StaticValue.positon = postion;
                myBinder.setMusicList(MusicResolver.collectMusicArrayList);
                myBinder.setCurrentIndex(postion);
                myBinder.seekTo(0);
                myBinder.play();
            }
        });

        collectAdapter.notifyDataSetChanged();
    }

    //设置页面根据字母索引滑动
    private void initEvent() {
        binding.characerList.setOnLetterChangeListener(new CharacterListView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = collectAdapter.getSortLettersFirstPosition(letter);
                if (position != -1) {
                    if (binding.collectRecycler.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) binding.collectRecycler.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        binding.collectRecycler.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_btn_play:
                if (myBinder.isPlaying()) {
                    myBinder.pause();
                    Log.e("点击暂停","监听到啦");
                } else {
                    myBinder.play();
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

}