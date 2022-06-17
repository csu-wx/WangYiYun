package csu.soc.xwz.musicplayer.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import csu.soc.xwz.musicplayer.R;

public class SongMenuActivity extends BaseActivity {
    private TextView songMenuName;
    private TextView time;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_menu);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {               //seekbar设置监听，实现指哪放到哪
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if(myBinder.getMusicList() == null || myBinder.getMusicList().size() == 0) {
                        Toast.makeText(SongMenuActivity.this,"播放列表为空",Toast.LENGTH_SHORT).show();
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

        toolbar = findViewById(R.id.song_menu_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongMenuActivity.this.finish();
            }
        });

        songMenuName = findViewById(R.id.textView_song_name);
        time = findViewById(R.id.textView_time);

        Intent intent = getIntent();
        songMenuName.setText(intent.getStringExtra("songMenuName"));
        time.setText(intent.getStringExtra("time"));
    }

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